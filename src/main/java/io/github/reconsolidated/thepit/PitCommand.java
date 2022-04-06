package io.github.reconsolidated.thepit;

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
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ThePit.plugin.getPitLocation() != null) {
                player.teleport(ThePit.plugin.getPitLocation());
                player.sendMessage(ChatColor.GREEN + "Przeteleportowano na Arenę Tytanów.");
            } else {
                player.sendMessage(ChatColor.RED + "Lokalizacja Areny Tytanów nie została ustawiona.");
            }
        }
        return true;
    }
}
