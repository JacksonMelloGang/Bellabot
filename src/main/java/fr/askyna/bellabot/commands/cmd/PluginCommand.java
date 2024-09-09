package fr.askyna.bellabot.commands.cmd;

import fr.askyna.bellabot.BellaBot;
import fr.askyna.bellabot.commands.Command;
import fr.askyna.bellabot.commands.CommandManager;
import fr.askyna.bellabot.plugin.Plugin;
import fr.askyna.bellabot.plugin.PluginManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.List;

public class PluginCommand implements Command {
    @Override
    public String getName() {
        return "plugin";
    }

    @Override
    public String getDescription() {
        return "Get a list of plugins";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        List<Plugin> pluginList = BellaBot.getPluginManager().getPlugins();

        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i < pluginList.size(); i++){
            stringBuilder.append(pluginList.get(i));
        }

        if(stringBuilder.isEmpty()){
            stringBuilder.append("no plugins loaded.");
        }

        CommandManager.reply(event, stringBuilder.toString(), false);
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

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }
}
