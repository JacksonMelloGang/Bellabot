package fr.askyna.bellabot;

import fr.askyna.bellabot.commands.CommandManager;
import fr.askyna.bellabot.commands.cmd.*;
import fr.askyna.bellabot.config.BellaConfig;
import fr.askyna.bellabot.config.ConfigManager;
import fr.askyna.bellabot.cron.CronScheduler;
import fr.askyna.bellabot.database.DatabaseManager;
import fr.askyna.bellabot.database.entity.CommandEntity;
import fr.askyna.bellabot.database.entity.GuildEntity;
import fr.askyna.bellabot.database.entity.UserEntity;
import fr.askyna.bellabot.permissions.DefaultPermissionHandler;
import fr.askyna.bellabot.plugin.PluginManager;
import fr.askyna.bellabot.utils.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class BellaBot {
    private static CommandManager commandManager;
    private static PluginManager pluginManager;
    private static DatabaseManager databaseManager;
    private static JDA jda;
    private static ConfigManager config;
    private static SetupManager setupManager;

    public static void main(String[] args) throws Exception {
        CronScheduler cronScheduler = new CronScheduler();


        // Charger la configuration
        Logger.info("Step 1/5 - Loading Config & stuffs ");
        ConfigManager.loadConfig();
        BellaConfig.loadConfig();

        // Connexion à la base de données
        databaseManager = DatabaseManager.getInstance();

        databaseManager.registerEntity(GuildEntity.class);
        databaseManager.registerEntity(UserEntity.class);
        databaseManager.registerEntity(CommandEntity.class);

        // Initialiser le gestionnaire de commandes avec le système de permissions par défaut
        Logger.info("Step 2/5 - Initializing commands");
        commandManager = new CommandManager(new DefaultPermissionHandler());
        commandManager.setHelpCommand(new HelpCommand());
        commandManager.registerCommand(new PluginCommand());
        commandManager.registerCommand(new SetupCommand());

        // Charger les plugins
        Logger.info("Step 3/5 - Loading plugins");
        pluginManager = new PluginManager();
        pluginManager.loadPlugins();

        Logger.info("Step 4/5 - Connecting to the database");
        databaseManager.connect();

        Logger.info("Step 5/5 - Login to Discord..");
        // Initialiser JDA
        jda = JDABuilder.createDefault(config.getToken()).build();

        // Enregistrer le gestionnaire d'événements
        jda.addEventListener(commandManager);

        // Créer des instances des tâches cron personnalisées
//        Cron myFirstTask = new MyFirstCronTask();
//        Cron mySecondTask = new MySecondCronTask();

        // Planifier les tâches avec différentes fréquences
//        cronScheduler.scheduleCronTask(myFirstTask, 0, 10, TimeUnit.SECONDS); // Toutes les 10 secondes
//        cronScheduler.scheduleCronTask(mySecondTask, 0, 15, TimeUnit.SECONDS); // Toutes les 15 secondes

        // Enregistrer les commandes avec JDA
        Logger.info("Registering all commands");
        commandManager.registerAllCommands(jda);

        Runtime.getRuntime().addShutdownHook(new Thread(BellaBot::shutdown));
    }

    public static void shutdown() {
        if (jda != null) {
            jda.shutdown();
        }
        if (databaseManager != null) {
            databaseManager.shutdown();
        }
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static PluginManager getPluginManager() {
        return pluginManager;
    }

    public static JDA getJDA() {
        return jda;
    }

    public static ConfigManager getConfig() {
        return config;
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }



}
