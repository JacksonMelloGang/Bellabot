package fr.askyna.bellabot.database.service;


import fr.askyna.bellabot.commands.Command;
import fr.askyna.bellabot.commands.SubcommandProvider;
import fr.askyna.bellabot.database.DatabaseManager;
import fr.askyna.bellabot.database.entity.CommandEntity;
import fr.askyna.bellabot.utils.Logger;

import java.sql.*;

public class CommandService {

    private static  CommandService INSTANCE;

    public CommandService(){
        try(Connection connection = DatabaseManager.getInstance().getConnection()){
            String sql = "CREATE TABLE `commandentity`(" +
                    "`command` varchar(255) PRIMARY KEY," +
                    "`description` varchar(255) NOT NULL," +
                    "`options` varbinary(255) DEFAULT NULL," +
                    "`requiredPermissions` varbinary(255) DEFAULT NULL," +
                    "`requiredRoles` varbinary(255) DEFAULT NULL," +
                    "`subCommands` varbinary(255) DEFAULT NULL" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.execute();

        } catch (SQLException e) {
            Logger.error("Couldn't create CommandEntity Table", e);
            System.exit(0);
        }
    }



    public boolean registerCommand(Command command){
        boolean ok = false;
        try(Connection connection = DatabaseManager.getInstance().getConnection()){
            String sql = "INSERT INTO commandentity VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, command.getName());
            statement.setString(2, command.getDescription());
            statement.setString(3, command.getOptions());
            statement.setString(4, command.getRequiredPermissions());
            statement.setString(5, command.getRequiredRoles());
            ok = statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ok;
    }

    public CommandEntity getCommand(String name){
        CommandEntity commandEntity = null;
        try(Connection connection = DatabaseManager.getInstance().getConnection()){
            String sql = "SELECT * FROM commandentity WHERE command = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(2, name);
            ResultSet resultSet = statement.executeQuery();
            commandEntity = new CommandEntity(resultSet.getString("name"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return commandEntity;
    }


    public CommandEntity getCommand(String name, String description){
        CommandEntity commandEntity = null;
        try(Connection connection = DatabaseManager.getInstance().getConnection()){
            String sql = "SELECT * FROM commandentity WHERE command = ? AND description = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, description);
            ResultSet resultSet = statement.executeQuery();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return commandEntity;
    }

    public boolean deleteCommand(String name){
        boolean ok = false;
        try(Connection connection = DatabaseManager.getInstance().getConnection()){
            String sql = "DELETE FROM commandentity WHERE command = ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            ok = statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ok;
    }

    public static synchronized CommandService getInstance() {
        if(INSTANCE == null){
            INSTANCE = new CommandService();
        }
        return INSTANCE;
    }


}
