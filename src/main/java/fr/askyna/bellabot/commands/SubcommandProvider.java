package fr.askyna.bellabot.commands;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;

public interface SubcommandProvider {
    List<SubcommandData> getSubcommands();
}
