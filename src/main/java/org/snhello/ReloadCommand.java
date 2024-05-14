package org.snhello;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReloadCommand implements CommandExecutor, TabCompleter {
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
                } else if (args[0].equalsIgnoreCase("config")) {
                    if (!sender.hasPermission("SNHello.config")) {
                        sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.GREEN + "SNHello Plugin config: " + plugin.getConfig().getKeys(true));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("setconfig")) {
                    if (!sender.hasPermission("SNHello.setconfig")) {
                        sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                        return true;
                    } else {
                        if (args.length < 3) {
                            sender.sendMessage(ChatColor.RED + "Usage: /SNHello setconfig <key> <value>");
                            return true;
                        }
                        String key = args[1];
                        // Join all arguments after the key into a single string
                        String value = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

                        // Check if the key is "join-particle", "quit-particle", "join-sound", or "quit-sound" and validate the value
                        if (key.equalsIgnoreCase("join-particle") || key.equalsIgnoreCase("quit-particle")) {
                            try {
                                Particle.valueOf(value.toUpperCase());
                            } catch (IllegalArgumentException e) {
                                sender.sendMessage(ChatColor.RED + "Invalid particle type: " + value);
                                return true;
                            }
                        } else if (key.equalsIgnoreCase("join-sound") || key.equalsIgnoreCase("quit-sound")) {
                            try {
                                Sound.valueOf(value.toUpperCase());
                            } catch (IllegalArgumentException e) {
                                sender.sendMessage(ChatColor.RED + "Invalid sound type: " + value);
                                return true;
                            }
                        }

                        plugin.getConfig().set(key, value);
                        plugin.saveConfig();
                        sender.sendMessage(ChatColor.GREEN + "Set " + key + " to " + value + " in the config.");
                        return true;
                    }
                }
            }
        }
        return false;
    } // Closing brace for the onCommand method

    private void reloadPlugin() {
        plugin.reloadPluginConfig();
    } // Closing brace for the reloadPlugin method

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("SNHello")) {
            if (args.length == 1) {
                if ("SNHello".startsWith(args[0].toLowerCase())) {
                    list.add("version");
                    list.add("reload");
                    list.add("config");
                    list.add("setconfig");
                }
                return list;
            } else if (args.length == 2 && args[0].equalsIgnoreCase("setconfig")) {
                for (String key : plugin.getConfig().getKeys(true)) {
                    if (key.startsWith(args[1].toLowerCase())) {
                        list.add(key);
                    }
                }
            }
            return list;
        }
        return null;
    } // Closing brace for the onTabComplete method
} // Closing brace for the ReloadCommand class