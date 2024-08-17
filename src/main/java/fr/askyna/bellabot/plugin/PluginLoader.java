package fr.askyna.bellabot.plugin;


import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class PluginLoader {
    public static Plugin loadPlugin(File file) throws Exception {
        // Ouvrir le jar et lire le fichier plugin.yml
        JarFile jarFile = new JarFile(file);
        InputStream inputStream = jarFile.getInputStream(jarFile.getEntry("plugin.yml"));



        // Parser le YAML
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(inputStream);

        // Récupérer les informations nécessaires
        String mainClass = (String) data.get("main");
        String pluginName = (String) data.get("name");
        String pluginVersion = (String) data.get("version");
        String pluginDescription = (String) data.get("description");

        Objects.requireNonNull(mainClass, "La classe principale du plugin ne peut pas être nulle");
        Objects.requireNonNull(pluginName, "Le nom du plugin ne peut pas être nulle");
        Objects.requireNonNull(pluginVersion, "La version du plugin ne peut pas être nulle");

        System.out.println("Loading plugin: " + pluginName + " v" + pluginVersion);

        // Charger la classe principale du plugin
        URL[] urls = {file.toURI().toURL()};
        URLClassLoader classLoader = new URLClassLoader(urls, PluginLoader.class.getClassLoader());
        Class<?> clazz = Class.forName(mainClass, true, classLoader);

        // Vérifier que la classe implémente Plugin
        if (!Plugin.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("La classe principale " + mainClass + " n'implémente pas l'interface Plugin.");
        }

        return (Plugin) clazz.getDeclaredConstructor().newInstance();
    }
}