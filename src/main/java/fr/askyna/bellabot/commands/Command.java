package fr.askyna.bellabot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Collections;
import java.util.List;

public interface Command {
    String getName();
    String getDescription();
    void execute(SlashCommandInteractionEvent event);

    default boolean isOwnerOnly(){
        return false;
    }

    // Ajout de méthodes pour les permissions et rôles
    default List<Permission> getRequiredPermissions(){
        return List.of();
    }

    default List<String> getRequiredRoles(){
        return List.of();
    }

    default List<OptionData> getOptions(){
        return Collections.emptyList();
    }

    default boolean isEphemeral() {
        return false;
    }
}