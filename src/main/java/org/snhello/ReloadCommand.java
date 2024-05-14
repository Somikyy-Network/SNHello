package org.snhello;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

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
    }

@Override
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (command.getName().equalsIgnoreCase("SNHello")) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("SNHello.reload")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                    return true;
                } else {
                    reloadPlugin();
                    sender.sendMessage(ChatColor.GREEN + "SNHello Plugin reloaded!");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("version")) {
                if (!sender.hasPermission("SNHello.version")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                    return true;
                } else {
                    sender.sendMessage(ChatColor.GREEN + "SNHello Plugin version: " + plugin.getDescription().getVersion());
                    return true;
                }
            }
        }
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
        if (command.getName().equalsIgnoreCase("SNHello")) {
            if (args.length == 1) {
                List<String> list = new ArrayList<>();
                if ("SNHello".startsWith(args[0].toLowerCase())) {
                    list.add("version");
                    list.add("reload");
                }
                return list;
            }
        }
        return null;
    }
}
