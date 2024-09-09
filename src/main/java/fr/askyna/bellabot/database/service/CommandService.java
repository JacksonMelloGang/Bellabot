package fr.askyna.bellabot.database.service;


import fr.askyna.bellabot.database.DatabaseManager;
import fr.askyna.bellabot.database.entity.CommandEntity;
import fr.askyna.bellabot.utils.Logger;
import org.hibernate.Session;

public class CommandService {

    private static  CommandService INSTANCE;

    public CommandService(){

    }

    public boolean registerCommand(String name, String description){
        boolean ok = false;
        try(Session session = DatabaseManager.getInstance().getSessionFactory().openSession()){
            session.beginTransaction();
            CommandEntity commandEntity = new CommandEntity(name, description);
            session.save(commandEntity);
            session.getTransaction().commit();
            ok = true;
        }
        return ok;
    }

    public CommandEntity getCommand(Long name){
        CommandEntity commandEntity = null;
        try(Session session = DatabaseManager.getInstance().getSessionFactory().openSession()){
            commandEntity = session.get(CommandEntity.class, name);
        }
        return commandEntity;
    }

    public CommandEntity deleteCommand(long name){
        CommandEntity commandEntity = null;
        try(Session session = DatabaseManager.getInstance().getSessionFactory().openSession()){
            session.beginTransaction();
            commandEntity = session.get(CommandEntity.class, name);
            session.delete(commandEntity);
            session.getTransaction().commit();
        } catch(Exception e){
            Logger.error("Couldn't delete command " + name, e);
        }
        return commandEntity;
    }

    public static synchronized CommandService getInstance() {
        if(INSTANCE == null){
            INSTANCE = new CommandService();
        }
        return INSTANCE;
    }


}
