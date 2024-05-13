package org.snhello;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
//&&
public final class SNHello extends JavaPlugin {
    private Configuration config;

    @Override
    public void onEnable() {
        config = new Configuration(this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new QuitListener(this), this);
        // Plugin startup logic
    }
    public Configuration getConfiguration() {
        return config;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
