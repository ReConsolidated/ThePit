package io.github.reconsolidated.thepit;

import io.github.reconsolidated.thepit.Pickups.PickupsManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PitCommand implements CommandExecutor {

    public PitCommand() {
        ThePit.plugin.getCommand("pit").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0 || !sender.hasPermission("pit.admin")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (ThePit.plugin.getPitLocation() != null) {
                    player.teleport(ThePit.plugin.getPitLocation());
                    player.sendMessage(ChatColor.GREEN + "Przeteleportowano na Arenę Tytanów.");
                } else {
                    player.sendMessage(ChatColor.RED + "Lokalizacja Areny Tytanów nie została ustawiona.");
                }
            }
        } else {
            if (sender instanceof Player player) {
                if (args[0].equalsIgnoreCase("setpickup")) {
                    PickupsManager.getInstance().addPickup(player.getLocation().add(0, -1, 0));
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    PickupsManager.getInstance().load();
                }
            }

        }

        return true;
    }
}
