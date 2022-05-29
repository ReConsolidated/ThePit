package io.github.reconsolidated.thepit.Pickups;

import io.github.reconsolidated.thepit.ThePit;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.persistence.PersistentDataType;

public class ArmorStandsCleaner implements Listener {
    private final World world;

    public ArmorStandsCleaner(World world) {
        this.world = world;
        Bukkit.getScheduler().runTaskTimer(ThePit.plugin, this::clean, 100L, 20 * 60 * 5L);
    }


    private boolean destroyIfArmorStandAndMarked(Entity e) {
        if (e instanceof ArmorStand as) {
            if (isMarkedForCleaning(as) && !PickupsManager.getInstance().isAPickup(as)) {
                as.remove();
                return true;
            }
        }
        return false;
    }

    private void clean() {
        for (Entity e : world.getEntities()) {
            destroyIfArmorStandAndMarked(e);
        }
    }

    public static void markForCleaning(ArmorStand as) {
        as.getPersistentDataContainer().set(getKey(), PersistentDataType.INTEGER, 1);
    }

    private static boolean isMarkedForCleaning(ArmorStand as) {
        return as.getPersistentDataContainer().get(getKey(), PersistentDataType.INTEGER) != null;
    }

    private static NamespacedKey getKey() {
        return new NamespacedKey(ThePit.plugin, "destroy_on_reload");
    }
}
