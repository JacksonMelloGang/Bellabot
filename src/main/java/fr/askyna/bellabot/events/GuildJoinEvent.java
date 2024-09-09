package fr.askyna.bellabot.events;

import fr.askyna.bellabot.database.DatabaseManager;
import fr.askyna.bellabot.database.entity.GuildEntity;
import fr.askyna.bellabot.database.service.GuildService;
import fr.askyna.bellabot.utils.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

public class GuildJoinEvent extends ListenerAdapter {

    @Override
    public void onGuildJoin(@NotNull net.dv8tion.jda.api.events.guild.GuildJoinEvent event) {
        boolean success = false;
        Logger.info(String.format("Joined Guild %s - %s", event.getGuild().getId(), event.getGuild().getName()));
        Guild guild = event.getGuild();
        GuildService.getInstance().createGuild(guild.getIdLong(), guild.getName(), guild.getOwnerIdLong(), guild.getTimeCreated());

    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        Logger.info(String.format("Left Guild %s - %s", event.getGuild().getIdLong(), event.getGuild().getName()));
        Guild guild = event.getGuild();
        try(Session session = DatabaseManager.getInstance().getSessionFactory().openSession()){
            session.createQuery("DELETE FROM guilds WHERE guild_id = :guildID").setParameter("guildID", guild.getId());
        }catch (Exception e){
            Logger.error("Une erreur est survenue !", e);
        }
    }
}
