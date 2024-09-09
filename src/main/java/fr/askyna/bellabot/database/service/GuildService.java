package fr.askyna.bellabot.database.service;


import fr.askyna.bellabot.database.DatabaseManager;
import fr.askyna.bellabot.database.entity.GuildEntity;
import fr.askyna.bellabot.utils.Logger;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import java.time.OffsetDateTime;

public class GuildService {

    private static  GuildService INSTANCE;

    public GuildService(){

    }

    public boolean createGuild(Long guildId, String guildName, Long ownerId, OffsetDateTime creationDate) {
        boolean ok = false;
        GuildEntity guild = new GuildEntity(guildId, guildName, ownerId, creationDate);
        try(Session session = DatabaseManager.getInstance().getSessionFactory().openSession()){
            session.beginTransaction();
            session.save(guild);
            session.getTransaction().commit();
            session.close();
            ok = true;
        } catch(ConstraintViolationException e){
            Logger.error("Constraint Violation: ", e );
        } catch (Exception e){
            Logger.error("An error occurred !", e);
        }

        return ok;
    }

    public boolean createGuild(Long guildId, String guildName, Long ownerId, OffsetDateTime creationDate, String logChannelId) {
        boolean ok = false;
        GuildEntity guild = new GuildEntity(guildId, guildName, ownerId, creationDate, logChannelId);
        try(Session session = DatabaseManager.getInstance().getSessionFactory().openSession()){
            session.beginTransaction();
            session.save(guild);
            session.getTransaction().commit();
            session.close();
            ok = true;
        } catch (Exception e){
            Logger.error("An error occurred !", e);
        }

        return ok;
    }

    public GuildEntity getGuildById(String id){
        GuildEntity guild = null;
        try(Session session = DatabaseManager.getInstance().getSessionFactory().openSession()){
            guild = session.get(GuildEntity.class, id) != null ? guild : null;
            session.close();
        } catch(Exception e){
            Logger.error("An error occurred !", e);
        }

        return guild;
    }

    public void deleteGuild(String guildId){
        try(Session session = DatabaseManager.getInstance().getSessionFactory().openSession()){
            session.beginTransaction();
            GuildEntity guildEntity = session.get(GuildEntity.class, guildId);
            session.delete(guildEntity);
            session.getTransaction().commit();
        }
    }

    public static synchronized GuildService getInstance() {
        if(INSTANCE == null){
            INSTANCE = new GuildService();
        }
        return INSTANCE;
    }
}
