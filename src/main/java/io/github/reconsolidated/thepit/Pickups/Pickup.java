package io.github.reconsolidated.thepit.Pickups;


import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Pickup {
    @Getter
    private final Location location;
    private String command = "";
    private final int refreshTime;
    private ArmorStand as;

    public Pickup(Location location, int refreshTime) {
        this.location = location;
        this.refreshTime = refreshTime;

        createArmorStand();
    }

    private void createArmorStand() {
        as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        as.setInvisible(true);
        as.setInvulnerable(true);
        as.setGravity(false);
        ArmorStandsCleaner.markForCleaning(as);
    }

    private void spawn(ItemStack item, String command) {

    }

    public void onPickup(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }


    public ArmorStand getArmorStand() {
        return as;
    }
}
