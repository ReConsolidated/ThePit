package io.github.reconsolidated.thepit.PitUtilListeners;

import io.github.reconsolidated.thepit.ThePit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class SlimeJumps implements Listener {
    public SlimeJumps() {
        ThePit.plugin.getServer().getPluginManager().registerEvents(this, ThePit.plugin);
    }

    @EventHandler
    public void onStepOnSlime(PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().getName().equals(ThePit.plugin.getWorld().getName())) return;

        World world = event.getPlayer().getWorld();
        if (world.getBlockAt(event.getPlayer().getLocation().clone().add(0, -1, 0)).getType().equals(Material.SLIME_BLOCK)) {
            Vector direction = event.getPlayer().getLocation().getDirection().normalize();
            event.getPlayer().setVelocity(direction.multiply(13).setY(1.3));
        }

    }

}
