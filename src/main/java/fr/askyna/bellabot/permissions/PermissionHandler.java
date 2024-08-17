package fr.askyna.bellabot.permissions;

import fr.askyna.bellabot.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface PermissionHandler {
    boolean hasPermission(SlashCommandInteractionEvent event, Command command);
}