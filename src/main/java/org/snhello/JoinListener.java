package org.snhello;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.concurrent.atomic.AtomicReference;

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
    Particle joinParticle = particleName != null ? Particle.valueOf(particleName) : null;

    AtomicReference<Particle.DustOptions> dustOptionsRef = new AtomicReference<>();
    if (joinParticle == Particle.REDSTONE && !plugin.getConfiguration().getJoinDustColor().equals("none")) {
        int[] color = plugin.getConfiguration().getJoinDustColor();
        Color dustColor = Color.fromRGB(color[0], color[1], color[2]);
        dustOptionsRef.set(new Particle.DustOptions(dustColor, 1));
    } else {
        Color defaultColor = Color.fromRGB(255, 255, 255); // Default white color
        dustOptionsRef.set(new Particle.DustOptions(defaultColor, 1));
    }

    new BukkitRunnable() {
        int particleSeconds = 0;

        @Override
        public void run() {
            if (joinParticle == Particle.REDSTONE) {
                player.getWorld().spawnParticle(joinParticle, player.getLocation(), 100, dustOptionsRef.get());
            } else {
                player.getWorld().spawnParticle(joinParticle, player.getLocation(), 100, 0.5, 0.5, 0.5, 1);
            }
            particleSeconds++;
            if (particleSeconds >= 30) {
                this.cancel();
            }
        }
    }.runTaskTimer(plugin, 0L, 1L);
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
