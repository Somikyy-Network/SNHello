package org.snhello;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicReference;

public class QuitListener implements Listener {
    private SNHello plugin;
    private Configuration config;
    private Particle quitParticle;

    public QuitListener(SNHello plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfiguration();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        String goodByeLessage = plugin.getConfiguration().getGoodbyeMessage().replace("%player%", player.getName());
        player.sendMessage(goodByeLessage);

        String soundName = plugin.getConfiguration().getQuitSound();
        if (soundName != null) {
            Sound sound = Sound.valueOf(soundName);
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        }

        String particleName = plugin.getConfiguration().getQuitParticle();
        Particle quitParticle = particleName != null ? Particle.valueOf(particleName) : null;

        AtomicReference<Particle.DustOptions> dustOptionsRef = new AtomicReference<>();
        if (quitParticle == Particle.REDSTONE && !plugin.getConfiguration().getQuitDustColor().equals("none")) {
            int[] color = plugin.getConfiguration().getQuitDustColor();
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
                if (quitParticle == Particle.REDSTONE) {
                    player.getWorld().spawnParticle(quitParticle, player.getLocation(), 100, dustOptionsRef.get());
                } else {
                    player.getWorld().spawnParticle(quitParticle, player.getLocation(), 100, 0.5, 0.5, 0.5, 1);
                }
                particleSeconds++;
                if (particleSeconds >= 30) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
