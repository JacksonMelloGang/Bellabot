package fr.askyna.bellabot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public class PluginCommand implements Command {
    @Override
    public String getName() {
        return "plugins";
    }

    @Override
    public String getDescription() {
        return "Get a list of plugins";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("test").queue();
    }

    @Override
    public boolean isOwnerOnly() {
        return false;
    }

    @Override
    public List<Permission> getRequiredPermissions() {
        return List.of(Permission.ADMINISTRATOR);
    }

    @Override
    public List<String> getRequiredRoles() {
        return List.of();
    }
}
