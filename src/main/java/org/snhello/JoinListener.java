package org.snhello;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Particle.DustOptions;

import java.util.Map;
import java.util.UUID;
//&&
public class JoinListener implements Listener {
    private SNHello plugin;

    public JoinListener(SNHello plugin) {
        this.plugin = plugin;
    }

@EventHandler
public void onPlayerJoin(PlayerJoinEvent event) {
    event.setJoinMessage(null);
    Player player = event.getPlayer();
    String playerName = player.getName();
    String welcomeMessage = plugin.getConfiguration().getWelcomeMessage().replace("%player%", player.getName());
    player.sendMessage(welcomeMessage);

    String soundName = plugin.getConfiguration().getJoinSound();
    if (soundName != null) {
        Sound sound = Sound.valueOf(soundName);
        player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
    }

    String particleName = plugin.getConfiguration().getJoinParticle();
    if (particleName != null && !particleName.equals("none")) {
        Particle joinParticle = Particle.valueOf(particleName);

        if (joinParticle == Particle.REDSTONE && !plugin.getConfiguration().getDustColor().equals("none")) {
            DustOptions dustOptions = new DustOptions(plugin.getConfiguration().getDustColor(), 1);
            player.getWorld().spawnParticle(joinParticle, player.getLocation(), 100, 0.5, 0.5, 0.5, 1, dustOptions);
        }

        new BukkitRunnable() {
            int particleSeconds = 0;

        @Override
            public void run() {
            player.getWorld().spawnParticle(joinParticle, player.getLocation(), 100);
            particleSeconds++;
            if (particleSeconds >= 30) {
                this.cancel();
            }
        }
    }.runTaskTimer(plugin, 0L, 1L);
}
    // Check if the player has joined before
    UUID playerId = player.getUniqueId();
    Map<UUID, Boolean> joinedPlayers = plugin.getConfiguration().getJoinedPlayers();
    if (joinedPlayers.containsKey(playerId)) {
        String title = plugin.getConfiguration().getReturningJoinTitle(playerName);
        String subtitle = plugin.getConfiguration().getReturningJoinSubtitle(playerName);
        player.sendTitle(title, subtitle, 10, 70, 20);
    } else {
        String title = plugin.getConfiguration().getFirstJoinTitle(playerName);
        String subtitle = plugin.getConfiguration().getFirstJoinSubtitle(playerName);
        player.sendTitle(title, subtitle, 10, 70, 20);
        joinedPlayers.put(playerId, true);
        plugin.getConfiguration().addPlayer(playerId);
        plugin.getConfiguration().setJoinedPlayers(joinedPlayers); // Save the updated joinedPlayers map back to the configuration
    }
    // say in console that the player joined the server and say how much players are online color of this message needs to be green
    plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + playerName + " joined the server. There are " + plugin.getServer().getOnlinePlayers().size() + " players online.");
    }
}
