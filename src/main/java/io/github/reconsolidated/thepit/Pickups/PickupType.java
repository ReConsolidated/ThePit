package io.github.reconsolidated.thepit.Pickups;

import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("PickupType")
public class PickupType implements ConfigurationSerializable {
    @Getter
    private final String command;
    @Getter
    private final ItemStack item;
    @Getter
    private final String display;


    public PickupType(String command, ItemStack item, String display) {
        this.command = command;
        this.item = item;
        this.display = display;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("command", command);
        result.put("item", item);
        result.put("display", display);
        return result;
    }

    public static PickupType deserialize(Map<String, Object> args) {
        return new PickupType((String) args.get("command"), (ItemStack) args.get("item"), (String) args.get("display"));
    }
}
