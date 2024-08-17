package fr.askyna.bellabot.commands;

import fr.askyna.bellabot.BellaBot;
import fr.askyna.bellabot.permissions.PermissionHandler;
import fr.askyna.bellabot.utils.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class CommandManager extends ListenerAdapter {
    private final Map<String, Command> commands = new HashMap<>();
    private PermissionHandler permissionHandler;

    public CommandManager(PermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
    }

    public void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public void unregisterCommand(String name) {
        commands.remove(name);
    }

    public void setPermissionHandler(PermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Command command = commands.get(event.getName());

        if (command != null) {
            if (!permissionHandler.hasPermission(event, command)) {
                event.reply("Vous n'avez pas la permission d'exécuter cette commande.").setEphemeral(true).queue();
                return;
            }

            command.execute(event);
        } else {
            event.reply("Commande inconnue.").setEphemeral(true).queue();
        }
    }

    public void registerAllCommands(JDA jda) {
        for (Command command : commands.values()) {
            jda.upsertCommand(command.getName(), command.getDescription()).queue();
        }
    }
}