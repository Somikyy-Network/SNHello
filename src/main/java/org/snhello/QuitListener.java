package org.snhello;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
//&&
public class QuitListener implements Listener {
    private SNHello plugin;
    private Configuration config;

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
        if (particleName != null) {
            Particle quitParticle = Particle.valueOf(particleName);
            player.spawnParticle(quitParticle, player.getLocation(), 100);
        }
        int onlinePlayers = plugin.getServer().getOnlinePlayers().size() - 1;
        // say in console that the player left the server and say how much players are online color of this message needs to be red
        plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + player.getName() + " left the server. There are " + onlinePlayers + " players online.");
    }
}
