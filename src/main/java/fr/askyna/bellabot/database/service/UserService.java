package fr.askyna.bellabot.database.service;

import fr.askyna.bellabot.database.DatabaseManager;
import fr.askyna.bellabot.database.entity.UserEntity;
import fr.askyna.bellabot.utils.Logger;
import org.hibernate.Session;

public class UserService {

    private static  UserService INSTANCE;

    public UserService(){
        
    }

    public boolean createUser(Long userId, String username, String avatar, String creationDate){
        boolean success = false;

        UserEntity userEntity = new UserEntity(userId, username, avatar, creationDate);
        try(Session session = DatabaseManager.getInstance().getSessionFactory().openSession()){
            session.beginTransaction();
            session.save(userEntity);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e){
            Logger.error("An error occurred !", e);
        }

        return success;
    }

    public UserEntity getUserById(String userId){
        UserEntity userEntity = null;
        try(Session session = DatabaseManager.getInstance().getSessionFactory().openSession()){
            userEntity = session.get(UserEntity.class, userId) != null ? userEntity : null;
            session.close();
        }

        return userEntity;
    }
    

    public static synchronized UserService getInstance() {
        if(INSTANCE == null){
            INSTANCE = new UserService();
        }
        
        return INSTANCE;
    }

}
