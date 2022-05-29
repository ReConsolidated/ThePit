package io.github.reconsolidated.thepit.Pickups;


import io.github.reconsolidated.thepit.ThePit;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("Pickup")
public class Pickup implements ConfigurationSerializable {
    @Getter
    private final Location location;
    private String command = "";
    private final int refreshTime;
    private ArmorStand as;

    private NamespacedKey commandKey;

    public Pickup(Location location, int refreshTime) {
        this.location = location;
        this.refreshTime = refreshTime;
        commandKey = new NamespacedKey(ThePit.plugin, "PICKUP_COMMAND");

        createArmorStand();
    }

    private void createArmorStand() {
        as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        as.setInvisible(true);
        as.setInvulnerable(true);
        as.setGravity(false);
        ArmorStandsCleaner.markForCleaning(as);

        Bukkit.getScheduler().runTaskTimer(ThePit.plugin, () -> {
            if (as != null) {
                as.setRotation(as.getLocation().getYaw() + 3, as.getLocation().getPitch());
            }
        }, 10L, 1L);
    }

    public void spawn(ItemStack item, String command) {
        this.command = command;
        as.getEquipment().setHelmet(item);
    }


    public void onPickup(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("<p>", player.getName()));
    }


    public ArmorStand getArmorStand() {
        return as;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("x", location.getX());
        result.put("y", location.getY());
        result.put("z", location.getZ());
        result.put("world", location.getWorld().getName());
        result.put("refresh_time", refreshTime);
        return result;
    }

    public static Pickup deserialize(Map<String, Object> args) {
        Location loc = new Location(Bukkit.createWorld(new WorldCreator((String) args.get(args.get("world")))),
                (double) args.get("x"),
                (double) args.get("y"),
                (double) args.get("z"));
        int refreshTime = (int) args.get("refresh_time");
        return new Pickup(loc, refreshTime);
    }
}
