package fr.askyna.bellabot.database.service;


import fr.askyna.bellabot.database.DatabaseManager;
import fr.askyna.bellabot.database.entity.GuildEntity;
import fr.askyna.bellabot.utils.Logger;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class GuildService {

    private static  GuildService INSTANCE;

    public GuildService(){
        try(Connection connection = DatabaseManager.getInstance().getConnection()){
            String sql = "CREATE TABLE `guild`(" +
                    "`guild_id` bigint NOT NULL," +
                    "`create_date` datetime(6) DEFAULT NULL," +
                    "`creationDate` datetime(6) DEFAULT NULL," +
                    "`guildName` varchar(255) DEFAULT NULL," +
                    "`logModChannelId` varchar(255) DEFAULT NULL," +
                    "`modify_date` datetime(6) DEFAULT NULL," +
                    "`ownerId` bigint DEFAULT NULL" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.execute();

        } catch (SQLException e) {
            Logger.error("Couldn't create CommandEntity Table", e);
            System.exit(0);
        }
    }

    public boolean createGuild(Long guildId, String guildName, Long ownerId, OffsetDateTime creationDate) {
        boolean ok = false;
        GuildEntity guild = new GuildEntity(guildId, guildName, ownerId, creationDate);
        try(Connection connection = DatabaseManager.getInstance().getConnection()){


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
