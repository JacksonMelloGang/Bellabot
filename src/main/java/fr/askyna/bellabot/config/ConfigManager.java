package fr.askyna.bellabot.config;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.yml";
    private static Map<String, Object> config;

    public static void loadConfig() throws IOException {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            createDefaultConfig();
        }

        Yaml yaml = new Yaml();
        try (FileInputStream fis = new FileInputStream(file)) {
            config = yaml.load(fis);
        }
    }

    private static void createDefaultConfig() throws IOException {
        Yaml yaml = new Yaml();
        config = Map.of(
                "token", "YOUR_DISCORD_BOT_TOKEN",
                "mysql.host", "localhost",
                "mysql.port", 3306,
                "mysql.database", "bellabot",
                "mysql.username", "root",
                "mysql.password", "",
                "ownerID", ""
        );

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            yaml.dump(config, writer);
        }
    }

    public static String getToken() {
        return (String) config.get("token");
    }

    public static String getMySQLHost() {
        return (String) config.get("mysql.host");
    }

    public static int getMySQLPort() {
        return (int) config.get("mysql.port");
    }

    public static String getMySQLDatabase() {
        return (String) config.get("mysql.database");
    }

    public static String getMySQLUsername() {
        return (String) config.get("mysql.username");
    }

    public static String getMySQLPassword() {
        return (String) config.get("mysql.password");
    }

    public static String getOwnerID(){
        return (String) config.get("ownerID");
    }
}