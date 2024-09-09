package fr.askyna.bellabot.commands;

import fr.askyna.bellabot.BellaBot;
import fr.askyna.bellabot.commands.cmd.HelpCommand;
import fr.askyna.bellabot.config.BellaConfig;
import fr.askyna.bellabot.database.DatabaseManager;
import fr.askyna.bellabot.database.entity.CommandEntity;
import fr.askyna.bellabot.database.service.GuildService;
import fr.askyna.bellabot.permissions.PermissionHandler;
import fr.askyna.bellabot.utils.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import java.awt.*;
import java.security.MessageDigest;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager extends ListenerAdapter {
    private final Map<String, Command> commands = new HashMap<>();
    private PermissionHandler permissionHandler;
    private Command helpCommand;

    public CommandManager(PermissionHandler permissionHandler){
        this.permissionHandler = permissionHandler;
    }

    public void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public void unregisterCommand(String name) {
        commands.remove(name);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Command command = commands.get(event.getName());
        Logger.debug(String.format("User %s tried executing %s", event.getUser().getName(), event.getName()));

        // if command is /help : use default helpcommand (if it hasn't been replaced) otherwise use HelpCommand defined by user
        if(event.getName().equalsIgnoreCase("help")){
            if(helpCommand == null) {
                helpCommand = new HelpCommand();
                BellaBot.getJDA().addEventListener(helpCommand);
            }

            registerCommand(helpCommand);
            helpCommand.execute(event);
            return;
        }

        // if command is valid (known by Bellabot)
        if (command != null) {
            // if user doesn't have permission
            if(!permissionHandler.hasPermission(event, command)){
                reply(event, "You don't have permission to execute this command !", true);
                return;
            }

            // try to execute command
            try {
                command.execute(event);
            } catch (Exception e){
                reply(event, "An unknown error has occurred while trying to handle your request ! || " + e.getMessage() + " ||", true);
                Logger.error("An unknown error has occurred while trying to handle a command !", e);

                // warn owner if displayLog enabled
                if(BellaConfig.errorDisplayLog()){
                    String logChannelId = GuildService.getInstance().getGuildById(event.getGuild().getId()).getLogModChannelId();
                    TextChannel textChannel = event.getGuild().getTextChannelById(logChannelId);

                    if(textChannel != null && textChannel.canTalk()){
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setTitle("An unknown error has occurred while trying to handle a command !");
                        embedBuilder.setColor(Color.RED);
                        embedBuilder.setDescription(e.getMessage());
                        embedBuilder.setTimestamp(OffsetDateTime.now());

                        textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
                    }
                }


            }
        } else {
            event.reply("Commande inconnue.").setEphemeral(true).queue();
        }
    }

    public static void reply(SlashCommandInteractionEvent event, String message, boolean ephemeral) {
        event.reply(message).setEphemeral(ephemeral).queue();
    }

    public static String getOption(SlashCommandInteractionEvent event, String optionName) {
        OptionMapping option = event.getOption(optionName);
        return option != null ? option.getAsString() : null;
    }

    public void registerAllCommands(JDA jda) {
        List<CommandData> commandDataList = new ArrayList<>();

        for (Command command : commands.values()) {
            if(command.getName().isEmpty() || command.getDescription().isEmpty()){
                Logger.warn("Couldn't register command " + command.getName() + " - Invalid name or description.");
            } else {

                var commandData = Commands.slash(command.getName(), command.getDescription());
                CommandEntity commandEntity = new CommandEntity(command.getName(), command.getDescription());

                if(!command.getRequiredPermissions().isEmpty()){
                   commandData.setDefaultPermissions(DefaultMemberPermissions.enabledFor(command.getRequiredPermissions()));
                }

                // Check if the command has subcommands
                if (command instanceof SubcommandProvider) {
                    SubcommandProvider subcommandProvider = (SubcommandProvider) command;
                    for (SubcommandData subcommand : subcommandProvider.getSubcommands()) {
                        commandData.addSubcommands(subcommand);
                        commandEntity.addSubCommands(subcommand.toString());
                    }
                }

                // check if commands has no options
                if(!command.getOptions().isEmpty()){
                    for(OptionData option : command.getOptions()){
                        commandEntity.addOptions(option.getName());
                    }
                }

                Logger.info("Registering command " + command.getName());
                commandDataList.add(commandData);

                // save in database
                try(Session session = DatabaseManager.getInstance().getSessionFactory().openSession()){
                    session.beginTransaction();
                    session.save(commandEntity);
                    session.getTransaction().commit();
                    session.close();
                } catch (ConstraintViolationException e){
                    Logger.warn("An error occured while registering command : " + e.getMessage());
                }


                jda.upsertCommand(commandData).queue();
            }
        }

        //jda.updateCommands().addCommands(commandDataList).queue();
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    public void setHelpCommand(Command helpCommand) {
        this.helpCommand = helpCommand;
    }
}
