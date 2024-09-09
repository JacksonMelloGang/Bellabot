package fr.askyna.bellabot.permissions;

import fr.askyna.bellabot.commands.Command;
import fr.askyna.bellabot.config.BellaConfig;
import fr.askyna.bellabot.config.ConfigManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public class DefaultPermissionHandler implements PermissionHandler {

    @Override
    public boolean hasPermission(SlashCommandInteractionEvent event, Command command) {
        boolean allowed = false;
        List<Permission> requiredPermissions = command.getRequiredPermissions();
        List<String> requiredRoles = command.getRequiredRoles();

        if(requiredPermissions.isEmpty()){
            allowed = true;
        }

        if(requiredRoles.isEmpty()){
            allowed = true;
        }


        // if command is owneronly
        if(command.isOwnerOnly() && !allowed) {
            // if user == Config.ownerID
            if(event.getUser().getId().equalsIgnoreCase(ConfigManager.getOwnerID())){
                allowed = true;
            }

            // if user is owner & we allow ownerpassthrough
            System.out.println(event.getMember().isOwner() + "&&" + BellaConfig.ownerPassthrough());
            if(event.getMember().isOwner() && BellaConfig.ownerPassthrough()){
                allowed = true;
            }
        }

        // if a permission is required
        Permission[] permissionsArray = requiredPermissions.toArray(new Permission[0]); // convert list into array (apprently not compatible)
        if (requiredPermissions != null && event.getMember().hasPermission(permissionsArray) && !allowed) {
            allowed = true;
        }



        if (!requiredRoles.isEmpty() && !allowed) {
            boolean hasRequiredRole = requiredRoles.stream()
                    .anyMatch(role -> event.getMember().getRoles().stream()
                            .anyMatch(userRole -> userRole.getId().equalsIgnoreCase(role)));
            if (!hasRequiredRole) {
                allowed = false;
            }
        }

        return allowed;
    }
}