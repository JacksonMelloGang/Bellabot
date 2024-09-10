package fr.askyna.bellabot.plugin;


import fr.askyna.bellabot.BellaBot;
import fr.askyna.bellabot.database.DatabaseManager;
import fr.askyna.bellabot.utils.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class PluginLoader {
    public static Plugin loadPlugin(File file) throws Exception {
        // Read .jar file and get plugin.yml file
        JarFile jarFile = new JarFile(file);
        InputStream inputStream = jarFile.getInputStream(jarFile.getEntry("plugin.yml"));

        if(inputStream == null){
            Logger.error("Couldn't load file");
        }

        // parse plugin.yml
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(inputStream);

        // get infos from plugin.yml
        String mainClass = (String) data.get("main");
        String pluginName = (String) data.get("name");
        String pluginVersion = data.get("version").toString();
        String pluginDescription = (String) data.get("description");

        Objects.requireNonNull(mainClass, "La classe principale du plugin ne peut pas être nulle");
        Objects.requireNonNull(pluginName, "Le nom du plugin ne peut pas être nulle");
        Objects.requireNonNull(pluginVersion, "La version du plugin ne peut pas être nulle");

        Logger.info("Loading plugin: " + pluginName + " v" + pluginVersion);

        // Charger la classe principale du plugin
        URL[] urls = {file.toURI().toURL()};
        URLClassLoader classLoader = new URLClassLoader(urls, PluginLoader.class.getClassLoader());
        Class<?> clazz = Class.forName(mainClass, true, classLoader);

        // Vérifier que la classe implémente Plugin
        if (!Plugin.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("La classe principale " + mainClass + " n'implémente pas l'interface Plugin.");
        }

        addEntitiesToHibernate(classLoader, data);

        return (Plugin) clazz.getDeclaredConstructor().newInstance();
    }

    private static void addEntitiesToHibernate(ClassLoader classLoader, Map<String, Object> data)  {

        if (data.containsKey("entities")) {
            for (String entityClassName : (Iterable<String>) data.get("entities")) {
                try {
                    Class<?> entityClass = Class.forName(entityClassName, true, classLoader);
                    BellaBot.getDatabaseManager().registerEntity(entityClass);  // Ajoute la classe à Hibernate
                    Logger.debug("Added entity class: " + entityClassName);
                    System.out.println(Thread.currentThread().getContextClassLoader());
                } catch (ClassNotFoundException ex){
                    Logger.error("Couldn't find and register entity " + entityClassName, ex);
                } catch (Exception e){
                    Logger.error("Unknown error occurred while registering entity", e);
                }


            }
        } else {
            Logger.info("No entities specified in plugin.yml for plugin" + data.get("name")  );
        }
    }


}