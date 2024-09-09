package fr.askyna.bellabot.config;

import fr.askyna.bellabot.utils.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

// config for bellabot.yml
public class BellaConfig {
    private static final String BELLA_PARAMETER = "bellabot.yml";
    private static final String BELLA_PATH = "bellabot.yml";
    private static Map<String, Object> bellaparam;

    public static void loadConfig() throws IOException {
        File file = new File(BELLA_PARAMETER);
        if (!file.exists()) {
            copyFile();
        }

        Yaml yaml = new Yaml();
        try (FileInputStream fis = new FileInputStream(file)) {
            bellaparam = yaml.load(fis);
        }
    }

    private static void copyFile(){
        try(InputStream inputStream = BellaConfig.class.getClassLoader().getResourceAsStream(BELLA_PATH) ){
            if(inputStream == null){
                Logger.fatal("Fichier bellabot.yml non trouvé dans le dossier resources.");
                System.exit(0);
            }

            Files.copy(inputStream, Paths.get(BELLA_PATH));
            Logger.info("creating file bellabot.yml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean ownerPassthrough() {
        return (boolean) bellaparam.get("owner_guild_pass");
    }

    public static boolean errorDisplayLog() {
        return (boolean) bellaparam.get("error_display_log");
    }
}
