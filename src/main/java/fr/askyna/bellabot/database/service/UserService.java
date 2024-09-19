package fr.askyna.bellabot.database.service;

import fr.askyna.bellabot.database.DatabaseManager;
import fr.askyna.bellabot.database.entity.UserEntity;
import fr.askyna.bellabot.utils.Logger;

public class UserService {

    private static  UserService INSTANCE;

    public UserService(){
        
    }

    public boolean createUser(Long userId, String username, String avatar, String creationDate){
        boolean success = false;

        UserEntity userEntity = new UserEntity(userId, username, avatar, creationDate);
        try {

        } catch (Exception e){
            Logger.error("An error occurred !", e);
        }

        return success;
    }

    public UserEntity getUserById(String userId){
        UserEntity userEntity = null;


        return userEntity;
    }
    

    public static synchronized UserService getInstance() {
        if(INSTANCE == null){
            INSTANCE = new UserService();
        }
        
        return INSTANCE;
    }

}
