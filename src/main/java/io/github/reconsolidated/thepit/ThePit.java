package io.github.reconsolidated.thepit;

import io.github.reconsolidated.thepit.PitUtilListeners.KillingSpree;
import io.github.reconsolidated.thepit.PitUtilListeners.RemoveFallDamage;
import io.github.reconsolidated.thepit.PitUtilListeners.SlimeJumps;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class ThePit extends JavaPlugin {
    public static ThePit plugin;
    private String pitWorld = "pit";
    @Getter
    private Location pitLocation = null;
    @Getter
    private World world;
    private int borderY = 100;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        pitWorld = getConfig().getString("pit_world", "pit");
        world = Bukkit.createWorld(new WorldCreator(pitWorld));
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        if (getConfig().contains("pit_location")) {
            pitLocation = getConfig().getLocation("pit_location", new Location(world, 0, 120, 0));
        } else {
            pitLocation = getConfig().getLocation("pit_location", new Location(world, 0, 120, 0));
            getConfig().set("pit_location", pitLocation);
            saveConfig();
        }
        borderY = getConfig().getInt("border_y", 100);

        getServer().getPluginManager().registerEvents(new RemoveFallDamage(), this);
        new KillingSpree();
        new PitCommand();
        new SlimeJumps();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
