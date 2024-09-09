package fr.askyna.bellabot.commands.cmd;

import fr.askyna.bellabot.commands.Command;
import fr.askyna.bellabot.database.service.GuildService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class SetupCommand implements Command {
    @Override
    public String getName() {
        return "setup";
    }

    @Override
    public String getDescription() {
        return "Setup";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        assert guild != null;
        GuildService.getInstance().createGuild(guild.getIdLong(), guild.getName(), guild.getOwnerIdLong(), guild.getTimeCreated());
    }

    @Override
    public boolean isOwnerOnly() {
        return true;
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }
}
