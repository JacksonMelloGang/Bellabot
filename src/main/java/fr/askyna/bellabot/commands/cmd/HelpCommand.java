package fr.askyna.bellabot.commands.cmd;

import fr.askyna.bellabot.BellaBot;
import fr.askyna.bellabot.commands.Command;
import fr.askyna.bellabot.commands.CommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HelpCommand extends ListenerAdapter implements Command {

    private final int COMMANDS_PER_PAGE = 5;
    List<Command> commands = new ArrayList<Command>(BellaBot.getCommandManager().getCommands().values());

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Get a list of command";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        commands = new ArrayList<Command>(BellaBot.getCommandManager().getCommands().values()); // Méthode que tu dois créer pour récupérer toutes les commandes
        sendHelpPage(event, 0, commands); // Afficher la première page
    }

    private void sendHelpPage(SlashCommandInteractionEvent event, int page, List<Command> commands) {
        int totalPages = (int) Math.ceil((double) commands.size() / COMMANDS_PER_PAGE);

        if (page < 0 || page >= totalPages) {
            event.reply("Page invalide").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Commandes disponibles (Page " + (page + 1) + "/" + totalPages + ")");
        embed.setColor(Color.BLUE);

        int start = page * COMMANDS_PER_PAGE;
        int end = Math.min(start + COMMANDS_PER_PAGE, commands.size());

        for (int i = start; i < end; i++) {
            Command command = commands.get(i);
            embed.addField("/" + command.getName(), command.getDescription(), false);
        }

        // Créer des boutons de pagination
        Button previousButton = Button.primary("help:previous:" + page, "◀️ Précédent").withDisabled(page == 0);
        Button nextButton = Button.primary("help:next:" + page, "▶️ Suivant").withDisabled(page == totalPages - 1);

        event.replyEmbeds(embed.build())
                .addActionRow(previousButton, nextButton)
                .setEphemeral(true) // Ou false selon si tu veux que ce soit visible uniquement pour l'utilisateur
                .queue();
    }

    private void sendHelpPage(ButtonInteractionEvent event, int page, List<Command> commands) {
        int totalPages = (int) Math.ceil((double) commands.size() / COMMANDS_PER_PAGE);

        if (page < 0 || page >= totalPages) {
            event.reply("Page invalide").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Commandes disponibles (Page " + (page + 1) + "/" + totalPages + ")");
        embed.setColor(Color.BLUE);

        int start = page * COMMANDS_PER_PAGE;
        int end = Math.min(start + COMMANDS_PER_PAGE, commands.size());

        for (int i = start; i < end; i++) {
            Command command = commands.get(i);
            embed.addField("/" + command.getName(), command.getDescription(), false);
        }

        // Créer des boutons de pagination
        Button previousButton = Button.primary("help:previous:" + page, "◀️ Précédent").withDisabled(page == 0);
        Button nextButton = Button.primary("help:next:" + page, "▶️ Suivant").withDisabled(page == totalPages - 1);

        event.editMessageEmbeds(embed.build())
                .setActionRow(previousButton, nextButton)
                .queue();
    }



    @Override
    @SubscribeEvent
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String[] components = event.getComponentId().split(":");
        if (components.length == 3 && components[0].equals("help")) {
            int currentPage = Integer.parseInt(components[2]);
            int newPage = components[1].equals("next") ? currentPage + 1 : currentPage - 1;

            sendHelpPage(event, newPage, commands);
        }
    }
}
