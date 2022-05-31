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
import org.bukkit.event.player.PlayerInteractEvent;
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
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof ArmorStand as) {
            Optional<Pickup> pickupOptional = getPickup(as);
            pickupOptional.ifPresent(pickup -> {
                event.setCancelled(true);
                if (pickup.isPickable()) {
                    pickup.onPickup(event.getPlayer());
                    Bukkit.getScheduler().runTaskLater(ThePit.plugin, () -> {
                        PickupType type = getRandomPickupType();
                        pickup.spawn(type);
                    }, pickup.getRefreshTime());
                }


            });
        }


    }

    private PickupType getRandomPickupType() {
        Random random = new Random();
        return pickupTypes.get(random.nextInt(pickupTypes.size()));
    }

    private Optional<Pickup> getPickup(ArmorStand as) {
        for (Pickup pickup : pickupList) {
            if (pickup != null && (pickup.getArmorStand().getUniqueId().equals(as.getUniqueId()) ||
                    (pickup.getArmorStand().getWorld().equals(as.getWorld())
                            && pickup.getArmorStand().getLocation().distanceSquared(as.getLocation()) < 1)) ) {
                return Optional.of(pickup);
            }
        }
        return Optional.empty();
    }


    public boolean isAPickup(ArmorStand as) {
        for (Pickup pickup : pickupList) {
            if (pickup.getArmorStand().getUniqueId().equals(as.getUniqueId())) return true;
            if (pickup.getFirstLineAs().getUniqueId().equals(as.getUniqueId())) return true;
        }
        return false;
    }

    public void setup() {
        pickupList = (List<Pickup>) ThePit.plugin.getConfig().getList("pickups", new ArrayList<>());

        if (pickupList.isEmpty()) {
            pickupList.add(new Pickup(new Location(Bukkit.getWorlds().get(0), 0, 100, 0), 20000));
            savePickupList();
        }

        load();

        for (Pickup pickup : pickupList) {
            PickupType type = getRandomPickupType();
            pickup.spawn(type);
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
        pickup.spawn(type);
        pickupList.add(pickup);
        savePickupList();
    }

    public void load() {
        pickupTypes = (List<PickupType>) ThePit.plugin.getConfig().getList("pickup_types", new ArrayList<>());

        if (pickupTypes.isEmpty()) {
            pickupTypes.add(new PickupType("say Hello", new ItemStack(Material.BEDROCK), "Efekt Testowy"));

            savePickupTypesList();
        }
    }
}
