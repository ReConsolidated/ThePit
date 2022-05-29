package io.github.reconsolidated.thepit.Pickups;

import io.github.reconsolidated.thepit.ThePit;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class PickupsManager implements Listener {
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

        ThePit.plugin.getServer().getPluginManager().registerEvents(this, ThePit.plugin);
    }

    @EventHandler
    public void onInteract(PlayerArmorStandManipulateEvent event) {
        Bukkit.getLogger().info("Right click as");
        ArmorStand as = event.getRightClicked();
        Optional<Pickup> pickupOptional = getPickup(as);
        pickupOptional.ifPresent(pickup -> {
            Bukkit.getLogger().info("its a pickup");

            pickup.onPickup(event.getPlayer());
            Bukkit.getScheduler().runTaskLater(ThePit.plugin, () -> {
                PickupType type = getRandomPickupType();
                pickup.spawn(type.getItem(), type.getCommand());
            }, pickup.getRefreshTime());

        });

    }

    private PickupType getRandomPickupType() {
        Random random = new Random();
        return pickupTypes.get(random.nextInt(pickupTypes.size()));
    }

    private Optional<Pickup> getPickup(ArmorStand as) {
        for (Pickup pickup : pickupList) {
            if (pickup != null && pickup.getArmorStand().getUniqueId().equals(as.getUniqueId())) {
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
        load();

        for (Pickup pickup : pickupList) {
            PickupType type = getRandomPickupType();
            pickup.spawn(type.getItem(), type.getCommand());
        }
    }

    private void savePickupTypesList() {
        ThePit.plugin.getConfig().set("pickup_types", pickupTypes);
        ThePit.plugin.saveConfig();
    }

    private void savePickupList() {
        ThePit.plugin.getConfig().set("pickups", pickupList);
        ThePit.plugin.saveConfig();
    }

    public void addPickup(Location location) {
        Pickup pickup = new Pickup(location.clone(), 10000);
        PickupType type = getRandomPickupType();
        pickup.spawn(type.getItem(), type.getCommand());
        pickupList.add(pickup);
        savePickupList();
    }

    public void load() {
        pickupList = (List<Pickup>) ThePit.plugin.getConfig().getList("pickups", new ArrayList<>());

        if (pickupList.isEmpty()) {
            pickupList.add(new Pickup(new Location(Bukkit.getWorlds().get(0), 0, 100, 0), 20000));
            savePickupList();
        }

        pickupTypes = (List<PickupType>) ThePit.plugin.getConfig().getList("pickup_types", new ArrayList<>());

        if (pickupTypes.isEmpty()) {
            pickupTypes.add(new PickupType("say Hello", new ItemStack(Material.BEDROCK)));

            savePickupTypesList();
        }
    }
}
