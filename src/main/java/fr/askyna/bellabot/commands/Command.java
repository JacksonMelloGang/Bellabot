package fr.askyna.bellabot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public interface Command {
    String getName();
    String getDescription();
    void execute(SlashCommandInteractionEvent event);
    boolean isOwnerOnly();

    // Ajout de méthodes pour les permissions et rôles
    List<Permission> getRequiredPermissions();
    List<String> getRequiredRoles();
}