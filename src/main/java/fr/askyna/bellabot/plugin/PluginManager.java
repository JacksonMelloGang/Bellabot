package fr.askyna.bellabot.plugin;

import fr.askyna.bellabot.utils.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PluginManager {
    private final List<Plugin> plugins = new ArrayList<>();

    public void loadPlugins() {
        File pluginDir = new File("plugins");
        if (!pluginDir.exists()) {
            pluginDir.mkdir();
        }

        File[] files = pluginDir.listFiles((dir, name) -> name.endsWith(".jar"));
        if (files != null) {
            for (File file : files) {
                try {
                    Plugin plugin = PluginLoader.loadPlugin(file);
                    plugin.onEnable();
                    plugins.add(plugin);
                    Logger.info("Plugin " + file.getName() + " chargé avec succès.");
                } catch (Exception e) {
                    Logger.error("Erreur lors du chargement du plugin " + file.getName(), e);
                }
            }
        }
    }

    public void reloadPlugins() {
        unloadPlugins();
        loadPlugins();
        Logger.info("Plugins rechargés avec succès.");
    }

    public void unloadPlugins() {
        for (Plugin plugin : plugins) {
            plugin.onDisable();
        }
        plugins.clear();
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }
}