package io.github.reconsolidated.thepit;

import io.github.reconsolidated.thepit.Pickups.ArmorStandsCleaner;
import io.github.reconsolidated.thepit.Pickups.Pickup;
import io.github.reconsolidated.thepit.Pickups.PickupType;
import io.github.reconsolidated.thepit.Pickups.PickupsManager;
import io.github.reconsolidated.thepit.PitUtilListeners.KillingSpree;
import io.github.reconsolidated.thepit.PitUtilListeners.RemoveFallDamage;
import io.github.reconsolidated.thepit.PitUtilListeners.SlimeJumps;
import io.github.reconsolidated.thepit.PitUtilListeners.SpawnProtection;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class ThePit extends JavaPlugin {
    public static ThePit plugin;
    private String pitWorld = "pit";
    @Getter
    private Location pitLocation = null;
    @Getter
    private World world;
    private int borderY = 100;

    static {
        ConfigurationSerialization.registerClass(PickupType.class, "PickupType");
        ConfigurationSerialization.registerClass(Pickup.class, "Pickup");
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        pitWorld = getConfig().getString("pit_world", "pit");
        world = Bukkit.createWorld(new WorldCreator(pitWorld));
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        pitLocation = world.getSpawnLocation().toCenterLocation();
        pitLocation.setYaw(-90);


        borderY = getConfig().getInt("border_y");
        if (borderY == 0) {
            borderY = 100;
            getConfig().set("border_y", 100);
        }

        saveConfig();
        getServer().getPluginManager().registerEvents(new RemoveFallDamage(), this);
        getServer().getPluginManager().registerEvents(new SpawnProtection(this), this);
        new KillingSpree();
        new PitCommand();
        new SlimeJumps();
        new PickupsManager();
        new ArmorStandsCleaner(world);
        PickupsManager.getInstance().setup();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public int getBorderY() {
        return borderY;
    }
}
