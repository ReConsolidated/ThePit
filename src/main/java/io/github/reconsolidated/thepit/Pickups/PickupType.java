package io.github.reconsolidated.thepit.Pickups;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class PickupType {
    @Getter
    private final String command;
    @Getter
    private final ItemStack item;


    public PickupType(String command, ItemStack item) {
        this.command = command;
        this.item = item;
    }
}
