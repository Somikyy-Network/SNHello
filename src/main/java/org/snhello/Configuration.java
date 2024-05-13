package org.snhello;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
//&&
public class Configuration {
    private SNHello plugin;
    private File configFile;
    private FileConfiguration config;

    public Configuration(SNHello plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        loadConfig();
    }

    private void loadConfig() {
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        if (isConfigCorruptedOrOutdated()) {
            File corruptedConfigFile = new File(plugin.getDataFolder(), "corrupted-or-outdated-config.yml");
            configFile.renameTo(corruptedConfigFile);

            plugin.saveDefaultConfig();
            config = YamlConfiguration.loadConfiguration(configFile);
            //show in a console this message: "(red color) [!] Config is corrupted, replacing it by a new one."
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "[!] Config is corrupted, replacing it by a new one.");
        }
    }

private boolean isConfigCorruptedOrOutdated() {
    // List of keys that are expected in the configuration file
    List<String> expectedKeys = Arrays.asList("welcome-message", "joined-players", "first-join-title", "first-join-subtitle", "returning-join-title", "returning-join-subtitle", "goodbye-message", "join-sound", "quit-sound", "join-particle", "quit-particle", "dust-color-join", "dust-color-quit");

    // Check if all expected keys are present in the configuration
    for (String key : expectedKeys) {
        if (!config.contains(key)) {
            System.out.println(ChatColor.DARK_RED + "Missing key: " + key);
            return true;
        }
    }

    return false;
}

public void setJoinedPlayers(Map<UUID, Boolean> joinedPlayers) {
    List<String> joinedPlayerIds = new ArrayList<>();
    for (UUID playerId : joinedPlayers.keySet()) {
        joinedPlayerIds.add(playerId.toString());
    }
    config.set("joined-players", joinedPlayerIds);
    plugin.saveConfig();
}

public Map<UUID, Boolean> getJoinedPlayers() {
    Map<UUID, Boolean> joinedPlayers = new HashMap<>();
    List<String> joinedPlayerIds = config.getStringList("joined-players");
    for (String playerId : joinedPlayerIds) {
        joinedPlayers.put(UUID.fromString(playerId), true);
    }
    return joinedPlayers;
}

    public String getWelcomeMessage() {
        String message = config.getString("welcome-message", "Welcome to the server, %player%!");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public boolean hasJoinedBefore(UUID playerId) {
        List<String> joinedPlayers = config.getStringList("joined-players");
        return joinedPlayers.contains(playerId.toString());
    }

    public void addPlayer(UUID playerId) {
        List<String> joinedPlayers = config.getStringList("joined-players");
        joinedPlayers.add(playerId.toString());
        config.set("joined-players", joinedPlayers);
        plugin.saveConfig();
    }

    public String getFirstJoinTitle(String playerName) {
        String message = config.getString("first-join-title", "Welcome to the server!");
        message = message.replace("%player%", playerName);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getFirstJoinSubtitle(String playerName) {
        String message = config.getString("first-join-subtitle", "This is your first time here!");
        message = message.replace("%player%", playerName);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getReturningJoinTitle(String playerName) {
        String message = config.getString("returning-join-title", "Welcome back to the server!");
        message = message.replace("%player%", playerName);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getReturningJoinSubtitle(String playerName) {
        String message = config.getString("returning-join-subtitle", "");
        message = message.replace("%player%", playerName);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getGoodbyeMessage() {
        String message = config.getString("goodbye-message", "Goodbye, %player%!");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getJoinSound() {
        String sound = config.getString("join-sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
        return "none".equals(sound) ? null : sound;
    }

    public String getQuitSound() {
        String sound = config.getString("quit-sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
        return "none".equals(sound) ? null : sound;
    }

    public String getJoinParticle() {
        String particle = config.getString("join-particle", "EXPLOSION_NORMAL");
        return "none".equals(particle) ? null : particle;
    }

    public int[] getJoinDustColor() {
        String rgb = config.getString("dust-color-join", "255,255,255");
        String[] rgbValues = rgb.split(",");

        int red = Integer.parseInt(rgbValues[0]);
        int green = Integer.parseInt(rgbValues[1]);
        int blue = Integer.parseInt(rgbValues[2]);

        return new int[]{red, green, blue};
    }

    public int[] getQuitDustColor() {
        String rgb = config.getString("dust-color-quit", "255,255,255");
        String[] rgbValues = rgb.split(",");

        int red = Integer.parseInt(rgbValues[0]);
        int green = Integer.parseInt(rgbValues[1]);
        int blue = Integer.parseInt(rgbValues[2]);

        return new int[]{red, green, blue};
    }

    public String getQuitParticle() {
        String particle = config.getString("quit-particle", "VILLAGER_ANGRY");
        return "none".equals(particle) ? null : particle;
    }
}