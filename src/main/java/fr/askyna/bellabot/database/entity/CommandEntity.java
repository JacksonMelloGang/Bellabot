package fr.askyna.bellabot.database.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "CommandEntity")
public class CommandEntity {

    @Id
    @Column(nullable = false, unique = true)
    String command;

    @Column(nullable = false)
    String description;

    @Column(nullable = true)
    Set<Long> requiredRoles; //rolesID

    @Column(nullable = true)
    Set<Integer> requiredPermissions; // permissionID

    @Column(nullable = true)
    List<String> subCommands;

    @Column(nullable = true)
    List<String> options;

    public CommandEntity(){

    }

    public CommandEntity(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public CommandEntity(String command, String description, Set<Long> requiredRoles, Set<Integer> requiredPermissions) {
        this.command = command;
        this.description = description;
        this.requiredRoles = requiredRoles;
        this.requiredPermissions = requiredPermissions;
    }

    public CommandEntity(String command, String description, Set<Long> requiredRoles, Set<Integer> requiredPermissions, List<String> subCommands, List<String> options) {
        this.command = command;
        this.description = description;
        this.requiredRoles = requiredRoles;
        this.requiredPermissions = requiredPermissions;
        this.subCommands = subCommands;
        this.options = options;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Long> getRequiredRoles() {
        return requiredRoles;
    }

    public void addRequiredRoles(Long requiredRole) {
        this.requiredRoles.add(requiredRole);
    }

    public Set<Integer> getRequiredPermissions() {
        return requiredPermissions;
    }

    public void addRequiredPermission(int requiredPermissions) {
        this.requiredPermissions.add(requiredPermissions);
    }

    public List<String> getSubCommands() {
        return subCommands;
    }

    public void addSubCommands(String subCommand) {
        this.subCommands.add(subCommand);
    }

    public List<String> getOptions() {
        return options;
    }

    public void addOptions(String option) {
        this.options.add(option);
    }
}
