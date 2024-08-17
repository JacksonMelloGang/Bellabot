package fr.askyna.bellabot.database;

import fr.askyna.bellabot.config.ConfigManager;
import fr.askyna.bellabot.utils.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DatabaseManager {

    private SessionFactory sessionFactory;

    public void connect() {
        try {
            Configuration config = new Configuration();
            config.setProperty("hibernate.connection.url", "jdbc:mysql://" + ConfigManager.getMySQLHost() + ":" + ConfigManager.getMySQLPort() + "/" + ConfigManager.getMySQLDatabase());
            config.setProperty("hibernate.connection.username", ConfigManager.getMySQLUsername());
            config.setProperty("hibernate.connection.password", ConfigManager.getMySQLPassword());
            config.configure();

            sessionFactory = config.buildSessionFactory();
            Logger.info("Connexion à la base de données réussie.");
        } catch (Exception e) {
            Logger.error("Erreur lors de la connexion à la base de données.", e);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}