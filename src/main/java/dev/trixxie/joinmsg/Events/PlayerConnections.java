package dev.trixxie.joinmsg.Events;

import dev.trixxie.joinmsg.Joinmsg;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PlayerConnections implements Listener {

    private Joinmsg plugin = Joinmsg.get();

    @EventHandler
    public void playerJoinFirstTime(PlayerLoginEvent e){
        Player p = e.getPlayer();
        if(!p.hasPlayedBefore()){
            String s = plugin.getConfig().getString("first-join-message");
            if(!s.equalsIgnoreCase("none")) {
                String welcomeMsg = s.replace("%player%", p.getName());
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', welcomeMsg));
            }
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        String s = plugin.getConfig().getString("join-message");
        if(!s.equalsIgnoreCase("none")) {
            String joinMsg = s.replace("%player%", p.getName());
            e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', joinMsg));
            if(plugin.getConfig().getBoolean("join-sound.enabled")){
                p.playSound(p.getLocation(), Sound.valueOf(plugin.getConfig().getString("join-sound.sound")), 2F, 1F);
            }
            if(plugin.getConfig().getBoolean("join-fireworks.enabled")){
                spawnFireworks(p, plugin.getConfig().getInt("join-fireworks.amount"));
            }
        }
        if(plugin.getConfig().getBoolean("join-message-title.enabled")){
            if(plugin.getConfig().getString("join-message-title.title") != null && plugin.getConfig().getString("join-message-title.subtitle") != null){
                new BukkitRunnable(){ //probs dont need run later?
                    @Override
                    public void run() {
                        //stupidly long thing below, but works lol
                        p.sendTitle(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("join-message-title.title")), ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("join-message-title.subtitle")), plugin.getConfig().getInt("join-message-title.fadeIn") * 20, plugin.getConfig().getInt("join-message-title.stay") * 20, plugin.getConfig().getInt("join-message-title.fadeOut") * 20);
                    }
                }.runTaskLater(plugin, 3);
            } else {
                System.out.println("JoinMSG title or/and subtitle configuration has been setup incorrectly!!!");
                if(p.isOp()){
                    p.sendMessage(ChatColor.RED + "JoinMSG title or/and subtitle configuration has been setup incorrectly!!!");
                }
            }
        }
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        String s = plugin.getConfig().getString("join-message");
        if(!s.equalsIgnoreCase("none")) {
            String quitMsg = s.replace("%player%", p.getName());
            e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', quitMsg));
        }
    }

    private static void spawnFireworks(Player p, Integer amount){
        Location loc = p.getLocation();
        int diameter = 10; //Diameter of the circle centered on loc
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.RED).flicker(true).build());
        fw.setFireworkMeta(fwm);
        fw.detonate();

        for(int i = 0; i < amount; i++){
            Location newLocation = loc.add(new Vector(Math.random()-0.5, 0, Math.random()-0.5).multiply(diameter));
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(newLocation, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }
}
