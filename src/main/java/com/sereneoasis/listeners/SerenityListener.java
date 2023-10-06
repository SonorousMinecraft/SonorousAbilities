package com.sereneoasis.listeners;

import com.sereneoasis.*;
import com.sereneoasis.ability.ComboManager;
import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.board.SerenityBoard;
import com.sereneoasis.classes.ocean.Gimbal;
import com.sereneoasis.classes.ocean.Spikes;
import com.sereneoasis.classes.ocean.Torrent;
import com.sereneoasis.storage.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.UUID;


public class SerenityListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        SerenityPlayer.loadAsync(player.getUniqueId(), player);

        Bukkit.getScheduler().runTaskLater(Serenity.getPlugin(), () -> {
            SerenityBoard board = SerenityBoard.createScore(player);
            board.setTitle("&aSerenity");
            SerenityPlayer serenityPlayer = SerenityPlayer.getSerenityPlayer(player);
            for (int i : serenityPlayer.getAbilities().keySet()) {
                board.setSlot(i, serenityPlayer.getAbilities().get(i));
            }
        }, 150L);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        SerenityBoard.removeScore(player);

        SerenityPlayer serenityPlayer = SerenityPlayer.getSerenityPlayer(player);

        Serenity.getComboManager().removePlayer(player);
        PlayerData oldPlayerData = Serenity.getRepository().get(uuid);
        oldPlayerData.setAbilities(serenityPlayer.getAbilities());
        oldPlayerData.setElement(serenityPlayer.getElement().toString());
        Serenity.getRepository().upsert(oldPlayerData);

        SerenityPlayer.getSerenityPlayerMap().remove(player.getUniqueId());

    }

    @EventHandler
    public void onSwing(PlayerInteractEvent e)
    {
        Player player = e.getPlayer();
        SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayerMap().get(player.getUniqueId());
        if (sPlayer == null)
        {
            return;
        }
        String ability = sPlayer.getHeldAbility();

        if (ability != null)
        {
            Serenity.getComboManager().addRecentlyUsed(player, ability, ClickType.LEFT);
        }
        switch(ability)
        {
            case "Torrent":
                if (CoreAbility.hasAbility(e.getPlayer(), Torrent.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Torrent.class).setHasClicked();
                }
                break;
            case "Gimbal":
                if (CoreAbility.hasAbility(e.getPlayer(), Gimbal.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Gimbal.class).setHasClicked();
                }
                break;
            case "Spikes":
                if (CoreAbility.hasAbility(e.getPlayer(), Gimbal.class)) {
                    CoreAbility.getAbility(e.getPlayer(), Gimbal.class).setHasClicked();
                }
                break;
        }

    }
    @EventHandler
    public void onShift(PlayerToggleSneakEvent e)
    {
        Player player = e.getPlayer();
        SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayerMap().get(player.getUniqueId());
        if (sPlayer == null)
        {
            return;
        }
        String ability = sPlayer.getHeldAbility();
        if (ability != null)
        {
            Serenity.getComboManager().addRecentlyUsed(player, ability, ClickType.SHIFT_LEFT);
        }
        switch(ability)
        {
            case "Torrent":
                new Torrent(player);
                break;
            case "Gimbal":
                new Gimbal(player);
                break;
            case "Spikes":
                new Spikes(player);
                break;
        }
    }
}
