package fr.askyna.bellabot.permissions;

import fr.askyna.bellabot.BellaBot;
import fr.askyna.bellabot.commands.Command;
import fr.askyna.bellabot.config.ConfigManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public class DefaultPermissionHandler implements PermissionHandler {

    @Override
    public boolean hasPermission(SlashCommandInteractionEvent event, Command command) {

        // Vérifier si la commande est réservée au propriétaire
        if (command.isOwnerOnly() && !event.getUser().getId().equals(ConfigManager.getOwnerID())) {
            return false;
        }

        // Vérifier les permissions JDA requises
        List<Permission> requiredPermissions = command.getRequiredPermissions();

        // if empty, user has permission
        if(requiredPermissions.isEmpty()){
            return true;
        }

        if (requiredPermissions != null && !event.getMember().hasPermission(requiredPermissions)) {
            return false;
        }

        // Vérifier les rôles requis
        List<String> requiredRoles = command.getRequiredRoles();

        // if empty, user has permission
        if(requiredRoles.isEmpty()){
            return true;
        }

        if (requiredRoles != null) {
            boolean hasRequiredRole = requiredRoles.stream()
                    .anyMatch(role -> event.getMember().getRoles().stream()
                            .anyMatch(userRole -> userRole.getName().equalsIgnoreCase(role)));
            if (!hasRequiredRole) {
                return false;
            }
        }

        return true;
    }
}