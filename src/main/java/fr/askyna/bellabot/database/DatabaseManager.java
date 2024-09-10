package fr.askyna.bellabot.database;

import fr.askyna.bellabot.BellaBot;
import fr.askyna.bellabot.config.ConfigManager;
import fr.askyna.bellabot.plugin.PluginLoader;
import fr.askyna.bellabot.plugin.PluginManager;
import fr.askyna.bellabot.utils.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.net.ConnectException;
import java.util.HashSet;
import java.util.Set;

public class DatabaseManager {

    private static DatabaseManager instance;

    private SessionFactory sessionFactory;
    private EntityManagerFactory entityManagerFactory;
    private Set<Class<?>> entityClasses;
    private Configuration config;

    private DatabaseManager() {
        this.entityClasses = new HashSet<>();
        config = new Configuration();
    }

    public void connect() {
        try {

            if(config == null){
                Logger.error("Didn't initialize DatabaseManager");
                return;
            }

            config.setProperty("hibernate.connection.url", "jdbc:mysql://" + ConfigManager.getMySQLHost() + ":" + ConfigManager.getMySQLPort() + "/" + ConfigManager.getMySQLDatabase());
            config.setProperty("hibernate.connection.username", ConfigManager.getMySQLUsername());
            config.setProperty("hibernate.connection.password", ConfigManager.getMySQLPassword());
            config.setProperty("hibernate.show_sql", "true");
            //config.setProperty("hibernate.format_sql", "true");
            //config.setProperty("hibernate.use_sql_comments", "true");
            config.setProperty("hibernate.hbm2ddl.auto", "update");
            config.setProperty("hibernate.archive.autodetection", "class,hbm");

            // Ajout des entités enregistrées
            for (Class<?> entityClass : entityClasses) {
                config.addAnnotatedClass(entityClass);
            }

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .addService(ClassLoaderService.class, new ClassLoaderServiceImpl(classLoader))
                    .applySettings(config.getProperties())
                    .build();

            sessionFactory = config.buildSessionFactory(serviceRegistry);
            Logger.info("Connexion à la base de données réussie.");

            // Crée EntityManagerFactory à partir de la SessionFactory
            this.entityManagerFactory = sessionFactory.unwrap(EntityManagerFactory.class);
        } catch (Exception e) {
            Logger.error("Erreur lors de la connexion à la base de données. ", e);
            System.exit(0);
        }
    }

    public void registerEntity(Class<?> entityClass) throws ClassNotFoundException {
        if(instance == null){
            Logger.error("Trying to register entity with a null DatabaseMananger !");
            return;
        }

        entityClasses.add(entityClass);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public EntityManager getEntityManager() {
        if (entityManagerFactory != null) {
            return entityManagerFactory.createEntityManager();
        }
        return null;
    }

    public void shutdown() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public synchronized static DatabaseManager getInstance() {
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }
}
