package fr.askyna.bellabot.database;

import fr.askyna.bellabot.BellaBot;
import fr.askyna.bellabot.config.ConfigManager;
import fr.askyna.bellabot.utils.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {}

    public void connect() {
        try {


            Logger.info("Connexion à la base de données réussie.");

        } catch (Exception e) {
            Logger.error("Erreur lors de la connexion à la base de données. ", e);
            System.exit(0);
        }
    }


    public void registerEntity(Class<?> entityClass) {
        if (instance == null) {
            Logger.error("Trying to register entity with a null DatabaseManager!");
            return;
        }

        entityClasses.add(entityClass);
    }

    public void shutdown() {

    }

    public synchronized static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
