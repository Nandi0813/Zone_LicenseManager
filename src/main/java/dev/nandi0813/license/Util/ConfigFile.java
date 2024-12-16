package dev.nandi0813.license.Util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public abstract class ConfigFile {

    protected final String fileName;

    protected final File file;
    protected final YamlConfiguration config;

    protected ConfigFile(JavaPlugin plugin, String path, String fileName) {
        this.fileName = fileName;

        this.file = new File(plugin.getDataFolder() + path, fileName + ".yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public abstract void setData();

    public abstract void getData();

    public void reloadFile() {
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveFile() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isSet(String loc) { return config.isSet(loc); }
    public Object get(String loc) {
        return config.get(loc);
    }
    public void set(String loc, Object obj) { config.set(loc, obj); }

}