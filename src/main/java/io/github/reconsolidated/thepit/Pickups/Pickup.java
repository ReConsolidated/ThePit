package io.github.reconsolidated.thepit.Pickups;


import io.github.reconsolidated.thepit.ThePit;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SerializableAs("Pickup")
public class Pickup implements ConfigurationSerializable {
    @Getter
    private final Location location;
    private PickupType currentType;
    private final int refreshTime;
    private long lastPickTime;
    private ArmorStand as;
    @Getter
    private ArmorStand firstLineAs;

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

        as.setCustomNameVisible(true);
        as.customName(Component.text(ChatColor.translateAlternateColorCodes('&', "&7Dostępne za: &c14:59")));


        firstLineAs = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0, 0.3, 0), EntityType.ARMOR_STAND);
        firstLineAs.setInvisible(true);
        firstLineAs.setInvulnerable(true);
        firstLineAs.setGravity(false);
        ArmorStandsCleaner.markForCleaning(firstLineAs);

        firstLineAs.setCustomNameVisible(true);
        firstLineAs.customName(Component.text(ChatColor.translateAlternateColorCodes('&', "&8[&b&l⬆&8] &dZwiększony Skok 2 &7(15s)")));

        Bukkit.getScheduler().runTaskTimer(ThePit.plugin, () -> {
            if (as != null) {
                as.setRotation(as.getLocation().getYaw() + 3, as.getLocation().getPitch());
            }
        }, 10L, 1L);

        Bukkit.getScheduler().runTaskTimer(ThePit.plugin, () -> {
            if (as != null && firstLineAs != null) {
                update();
            }
        }, 10L, 4L);
    }

    public void update() {
        if (currentType != null) {
            firstLineAs.customName(Component.text(ChatColor.translateAlternateColorCodes('&', currentType.getDisplay())));
            as.customName(Component.text(ChatColor.translateAlternateColorCodes('&', "&e&lKLIKNIJ PPM")));
        } else {
            long diff = (System.currentTimeMillis() - lastPickTime) / 1000;
            long refreshTimeSeconds = refreshTime / 20;

            long timeLeft = refreshTimeSeconds - diff;
            long minutes = timeLeft/60;
            long seconds = timeLeft%60;
            String sec = "0" + seconds;
            if (seconds > 9) {
                sec = "" + seconds;
            }

            firstLineAs.customName(Component.text(ChatColor.translateAlternateColorCodes('&', "&8[" + getRandomChatColor() + "&l⬆&8] &dLosowy efekt!")));
            as.customName(Component.text(ChatColor.translateAlternateColorCodes('&', "&7Dostępne za: &c%d:%s".formatted(minutes, sec))));
        }
    }

    private ChatColor getRandomChatColor() {
        Random random = new Random();
        List<ChatColor> colors = List.of(ChatColor.RED, ChatColor.WHITE, ChatColor.GREEN, ChatColor.AQUA, ChatColor.BLUE, ChatColor.LIGHT_PURPLE);
        return colors.get(random.nextInt(colors.size()));
    }


    public void spawn(PickupType type) {
        this.currentType = type;
        as.getEquipment().setHelmet(type.getItem());
    }


    public void onPickup(Player player) {
        as.getEquipment().setHelmet(new ItemStack(Material.SPONGE));
        lastPickTime = System.currentTimeMillis();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), currentType.getCommand().replace("<p>", player.getName()));
        currentType = null;
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
        Location loc = new Location(Bukkit.createWorld(new WorldCreator((String) args.get("world"))),
                (double) args.get("x"),
                (double) args.get("y"),
                (double) args.get("z"));
        int refreshTime = (int) args.get("refresh_time");
        return new Pickup(loc, refreshTime);
    }

    public boolean isPickable() {
        return currentType != null;
    }
}
