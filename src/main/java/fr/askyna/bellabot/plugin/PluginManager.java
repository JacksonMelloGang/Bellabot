package fr.askyna.bellabot.plugin;

import fr.askyna.bellabot.utils.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

public class PluginManager {
    private final List<Plugin> plugins = new ArrayList<>();
    private PluginLoader pluginLoader = PluginLoader.getInstance();
    private static PluginManager INSTANCE;

    public void loadPlugins() {
        File pluginDir = new File("plugins");
        if (!pluginDir.exists()) {
            pluginDir.mkdir();
        }

        File[] files = pluginDir.listFiles((dir, name) -> name.endsWith(".jar"));
        if (files != null) {
            for (File file : files) {
                try {
                    Plugin plugin = pluginLoader.loadPlugin(file);
                    plugin.onEnable();
                    plugins.add(plugin);

                    String pluginName = getPluginInfo(file).get("name");
                    File plugins_folder = new File("./plugins/"+pluginName);
                    if(!plugins_folder.exists()){ plugins_folder.mkdirs();}

                    Logger.info("Plugin " + file.getName() + " chargé avec succès.");
                } catch (Exception e) {
                    Logger.error("Error while loading file " + file.getName(), e);
                }
            }
        }
    }

    public Map<String, String> getPluginInfo(File file) throws IOException {
        JarFile jarFile = new JarFile(file);
        InputStream inputStream = jarFile.getInputStream(jarFile.getEntry("plugin.yml"));

        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(inputStream);

        String mainClass = (String) data.get("main");
        String pluginName = (String) data.get("name");
        String pluginVersion = data.get("version").toString();
        String pluginDescription = (String) data.get("description");

        HashMap<String, String> pluginInfos = new HashMap<>();
        pluginInfos.put("main", mainClass);
        pluginInfos.put("name", pluginName);
        pluginInfos.put("version", pluginVersion);
        pluginInfos.put("description", pluginDescription);

        return pluginInfos;
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

    public PluginLoader getPluginLoader() {
        return pluginLoader;
    }


    public static PluginManager getInstance() {
        if(INSTANCE == null){
            INSTANCE = new PluginManager();
        }

        return INSTANCE;
    }

}