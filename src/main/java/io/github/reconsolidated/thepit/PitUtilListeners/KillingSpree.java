package io.github.reconsolidated.thepit.PitUtilListeners;

import io.github.reconsolidated.thepit.ThePit;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.TextComponent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class KillingSpree implements Listener {

    Set<PlayerOnSpree> killingSprees;


    public KillingSpree() {
        killingSprees = new HashSet<>();
        ThePit.plugin.getServer().getPluginManager().registerEvents(this, ThePit.plugin);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (!event.getPlayer().getWorld().equals(ThePit.plugin.getWorld())) return;
        event.setRespawnLocation(ThePit.plugin.getPitLocation());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!event.getPlayer().getWorld().equals(ThePit.plugin.getWorld())) return;
        Player dead = event.getPlayer();
        Optional<PlayerOnSpree> plOpt = killingSprees.stream().filter((player) -> player.player.getUniqueId().equals(dead.getUniqueId())).findFirst();
        PlayerOnSpree deadPos;
        deadPos = plOpt.orElseGet(() -> new PlayerOnSpree(dead, 0));
        killingSprees.add(deadPos);
        if (deadPos.kills > 5) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + getPlayerDisplayName(deadPos.player) + ChatColor.YELLOW + " umarł i stracił serię " + deadPos.kills + " zabójstw!");
        }
        deadPos.kills = 0;


        Player killer = event.getPlayer().getKiller();
        if (killer == null) return;
        Optional<PlayerOnSpree> posOptional = killingSprees.stream().filter((player) -> player.player.getUniqueId().equals(killer.getUniqueId())).findFirst();
        PlayerOnSpree pos;
        pos = posOptional.orElseGet(() -> new PlayerOnSpree(killer, 0));
        killingSprees.add(pos);
        pos.kills++;

        pos.player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 0));

        if (pos.kills % 10 != 0 && pos.kills >= 2) {
            pos.player.sendMessage(ChatColor.GREEN + "Seria " + pos.kills + " zabójstw!");
        }
        if (pos.kills % 10 == 0) {
            Bukkit.broadcastMessage(getPlayerDisplayName(pos.player) + " ma serię " + pos.kills + " zabójstw na Arenie Tytanów!");
            pos.player.setAbsorptionAmount(pos.player.getAbsorptionAmount() + 8);
            pos.player.sendMessage(ChatColor.YELLOW + "+ 4 serca absorbcji.");
            pos.player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10 * 20, 0));
            pos.player.sendMessage(ChatColor.DARK_RED + "+ Siła 2 na 10 sekund.");
        }
        else if (pos.kills % 7 == 0) {
            pos.player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 20, 0));
            pos.player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 20, 0));
            pos.player.sendMessage(ChatColor.YELLOW + "+ Odporność na Ogień i Lepszy Skok na 20 sekund.");
        }
        else if (pos.kills % 5 == 0) {
            pos.player.setAbsorptionAmount(pos.player.getAbsorptionAmount() + 4);
            pos.player.sendMessage(ChatColor.YELLOW + "+ 2 serca absorbcji");
        }
        else if (pos.kills % 3 == 0) {
            pos.player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 8 * 20, 0));
            pos.player.sendMessage(ChatColor.YELLOW + "+ Siła 1 na 8 sekund.");
        }
        else if (pos.kills % 2 == 0) {
            pos.player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 0));
            pos.player.sendMessage(ChatColor.RED + "+ Lepsza Regeneracja na 5 sekund.");
        }

    }

    private String getPlayerDisplayName(Player player) {
        player.displayName();
        if (player.displayName() instanceof TextComponent) {
            TextComponent text = (TextComponent) player.displayName();
            if (text.content().length() > 0) {
                return text.content();
            }
        }
        return player.getName();
    }

    @AllArgsConstructor
    private static class PlayerOnSpree {
        Player player;
        int kills;
    }
}
