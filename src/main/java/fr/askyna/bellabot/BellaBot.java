package fr.askyna.bellabot;


import fr.askyna.bellabot.commands.CommandManager;
import fr.askyna.bellabot.commands.PluginCommand;
import fr.askyna.bellabot.config.ConfigManager;
import fr.askyna.bellabot.database.DatabaseManager;
import fr.askyna.bellabot.permissions.DefaultPermissionHandler;
import fr.askyna.bellabot.plugin.PluginManager;
import fr.askyna.bellabot.utils.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class BellaBot {
    private static CommandManager commandManager;
    private static PluginManager pluginManager;
    private static DatabaseManager databaseManager;
    private static JDA jda;
    private static ConfigManager config;

    public static void main(String[] args) throws Exception {
        // Charger la configuration
        ConfigManager.loadConfig();
        databaseManager = new DatabaseManager();

        // Connexion à la base de données
        databaseManager = new DatabaseManager();
        databaseManager.connect();

        // Initialiser JDA
        jda = JDABuilder.createDefault(config.getToken()).build();

        // Initialiser le gestionnaire de commandes avec le système de permissions par défaut
        commandManager = new CommandManager(new DefaultPermissionHandler());
        commandManager.registerCommand(new PluginCommand());

        // Enregistrer le gestionnaire d'événements
        jda.addEventListener(commandManager);

        // Charger les plugins
        pluginManager = new PluginManager();
        pluginManager.loadPlugins();

        // Enregistrer les commandes avec JDA
        commandManager.registerAllCommands(jda);
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
