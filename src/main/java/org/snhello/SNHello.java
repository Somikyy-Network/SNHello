package org.snhello;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
//&&
public final class SNHello extends JavaPlugin {
    private Configuration config;

    @Override
    public void onEnable() {
        config = new Configuration(this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new QuitListener(this), this);

        this.getCommand("SNHello").setExecutor(new ReloadCommand(this));
        // Plugin startup logic
        //write in console with green color: "Plugin enabled!"
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "SNHello Plugin is enabled!");
    }
    public Configuration getConfiguration() {
        return config;
    }

    public void reloadPluginConfig() {
        reloadConfig();
        config = new Configuration(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "SNHello Plugin is disabled!");
    }
}
