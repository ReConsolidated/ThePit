package io.github.reconsolidated.thepit.PitUtilListeners;

import io.github.reconsolidated.thepit.ThePit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class RemoveFallDamage implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL) && event.getEntity().getWorld().equals(ThePit.plugin.getWorld())) {
            event.setCancelled(true);
        }
    }
}
