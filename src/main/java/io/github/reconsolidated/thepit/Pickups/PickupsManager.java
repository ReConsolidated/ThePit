package io.github.reconsolidated.thepit.Pickups;

import io.github.reconsolidated.thepit.ThePit;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PickupsManager {
    @Getter
    private static PickupsManager instance;

    private List<Pickup> pickupList;
    private List<PickupType> pickupTypes;

    public PickupsManager() {
        if (instance == null) {
            instance = this;
        } else {
            throw new RuntimeException("Instance of PickupsManager already exists.");
        }

        setup();
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ArmorStand) {
            ArmorStand as = (ArmorStand) event.getRightClicked();
            Optional<Pickup> pickupOptional = getPickup(as);
            pickupOptional.ifPresent(pickup -> pickup.onPickup(event.getPlayer()));
        }
    }

    private Optional<Pickup> getPickup(ArmorStand as) {
        for (Pickup pickup : pickupList) {
            if (pickup.getArmorStand().getUniqueId().equals(as)) {
                return Optional.of(pickup);
            }
        }
        return Optional.empty();
    }


    public boolean isAPickup(ArmorStand as) {
        for (Pickup pickup : pickupList) {
            if (pickup.getArmorStand().getUniqueId().equals(as.getUniqueId())) return true;
        }
        return false;
    }

    public void setup() {
        pickupList = (List<Pickup>) ThePit.plugin.getConfig().getList("pickups");

        if (pickupList.isEmpty()) {
            pickupList.add(new Pickup(new Location(Bukkit.getWorlds().get(0), 0, 100, 0), 20));

            savePickupList();
        }

        pickupTypes = (List<PickupType>) ThePit.plugin.getConfig().getList("pickup_types");

        if (pickupTypes.isEmpty()) {
            pickupTypes.add(new PickupType("say Hello", new ItemStack(Material.BEDROCK)));

            savePickupTypesList();
        }
    }

    private void savePickupTypesList() {
        ThePit.plugin.getConfig().set("pickup_types", pickupTypes);
    }

    private void savePickupList() {
        ThePit.plugin.getConfig().set("pickups", pickupList);
    }
}
