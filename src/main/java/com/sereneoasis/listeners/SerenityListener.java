package com.sereneoasis.listeners;

import com.sereneoasis.*;
import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.airbending.AirBlast;
import com.sereneoasis.board.SerenityBoard;
import com.sereneoasis.storage.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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


        switch(ability)
        {
            case "AirBlast":
                if (CoreAbility.hasAbility(e.getPlayer(), AirBlast.class)) {
                    CoreAbility.getAbility(e.getPlayer(), AirBlast.class).setHasClicked();
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
        switch(ability)
        {
            case "AirBlast":
                new AirBlast(player);
                break;
        }
    }
}
