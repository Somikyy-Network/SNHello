package org.snhello;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand implements CommandExecutor, TabCompleter{
    private final SNHello plugin;

    public ReloadCommand(SNHello plugin) {
        this.plugin = plugin;
        registerCommand();
    }

    private void registerCommand() {
        PluginCommand command = plugin.getCommand("SNHello");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
        command = plugin.getCommand("rsnhp");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("SNHello") && args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            reloadPlugin();
            sender.sendMessage(ChatColor.GREEN + "Plugin reloaded!");
            return true;
        } else if (command.getName().equalsIgnoreCase("rsnhp")) {
            sender.sendMessage("/rsnhp");
            return true;
        }
        return false;
    }

    private void reloadPlugin() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        Plugin pluginInstance = pluginManager.getPlugin("SNHello");
        if (pluginInstance != null) {
            pluginManager.disablePlugin(pluginInstance);
            pluginManager.enablePlugin(pluginInstance);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("SNHello") && args.length == 0) {
            List<String> list = new ArrayList<>();
            list.add("reload");
            return list;
        }
        return null;
    }
}
