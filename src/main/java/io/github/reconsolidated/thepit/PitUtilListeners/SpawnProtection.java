package io.github.reconsolidated.thepit.PitUtilListeners;

import io.github.reconsolidated.thepit.ThePit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class SpawnProtection implements Listener {

    private final ThePit plugin;

    public SpawnProtection(ThePit plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!event.getEntity().getWorld().equals(plugin.getWorld())) return;

        if (event.getEntity().getLocation().getBlockY() > plugin.getBorderY()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!event.getEntity().getWorld().equals(plugin.getWorld())) return;

        if (event.getDamager().getLocation().getBlockY() > plugin.getBorderY()) {
            event.setCancelled(true);
        }
        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() != null && projectile.getShooter() instanceof Player) {
                Player player = (Player) projectile.getShooter();
                if (player.getLocation().getY() > plugin.getBorderY()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().isOp()) return;
        if (event.getBlock().getWorld().equals(plugin.getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockPlaceEvent event) {
        if (event.getPlayer().isOp()) return;
        if (event.getBlock().getWorld().equals(plugin.getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(PlayerBucketEmptyEvent event) {
        if (event.getPlayer().isOp()) return;
        if (event.getBlock().getWorld().equals(plugin.getWorld())) {
            event.setCancelled(true);
        }
    }
}
