package com.cobelpvp;

import net.minecraft.optimizations.util.Options;
import com.google.common.base.Throwables;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class CobelSpigotConfig {
    private static final String HEADER = "This is the main configuration file for CobelPvP Spigot.\nModify with caution, and make sure you know what you are doing.\n";

    public File configFile;

    public YamlConfiguration config;

    private Map<Options, Boolean> optionCache = new HashMap<>();

    public static float potionI = 0.05F;
    public static float potionE = 0.5F;
    public static float potionF = -20.0F;

    public CobelSpigotConfig() {
        this.configFile = new File("CobelSpigot.yml");
        if (!this.configFile.exists())
            try {
                this.configFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        this.config = new YamlConfiguration();
        try {
            this.config.load(this.configFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load CobelSpigot.yml, please correct your syntax errors", (Throwable)ex);
            throw Throwables.propagate(ex);
        }
        this.config.options().header("This is the main configuration file for CobelPvP Spigot.\nModify with caution, and make sure you know what you are doing.\n");
        this.config.options().copyDefaults(true);
        for (Options value : Options.values())
            this.optionCache.put(value, Boolean.valueOf(getBoolean(value.getPath(), value.isDef())));
        loadConfig();
    }

    public boolean getValue(Options option) {
        return ((Boolean)this.optionCache.get(option)).booleanValue();
    }

    public int getChunkThreads() {
        return getInt("chunk-threads", 1);
    }

    public int getPlayersPerThread() {
        return getInt("players-per-thread", 50);
    }

    private void loadConfig() {
        getChunkThreads();
        getPlayersPerThread();
        potionI = this.getFloat("potionI", 0.05F);
        potionE = this.getFloat("potionE", 0.5F);
        potionF = this.getFloat("potionF", -20.0F);
        try {
            this.config.save(this.configFile);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + this.configFile, ex);
        }
    }

    public void save() {
        try {
            this.config.save(this.configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String path, Object val) {
        this.config.set(path, val);
        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getKeys(String path) {
        if (!this.config.isConfigurationSection(path)) {
            this.config.createSection(path);
            return new HashSet<>();
        }
        return this.config.getConfigurationSection(path).getKeys(false);
    }

    public boolean getBoolean(String path, boolean def) {
        this.config.addDefault(path, Boolean.valueOf(def));
        return this.config.getBoolean(path, this.config.getBoolean(path));
    }

    public double getDouble(String path, double def) {
        this.config.addDefault(path, Double.valueOf(def));
        return this.config.getDouble(path, this.config.getDouble(path));
    }

    public float getFloat(String path, float def) {
        return (float)getDouble(path, def);
    }

    public int getInt(String path, int def) {
        this.config.addDefault(path, Integer.valueOf(def));
        return this.config.getInt(path, this.config.getInt(path));
    }

    public <T> List getList(String path, T def) {
        this.config.addDefault(path, def);
        return this.config.getList(path, this.config.getList(path));
    }

    public String getString(String path, String def) {
        this.config.addDefault(path, def);
        return this.config.getString(path, this.config.getString(path));
    }
}
