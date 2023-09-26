package com.sereneoasis.listeners;

import com.sereneoasis.*;
import com.sereneoasis.airbending.AirBlast;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;


public class SerenityListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        SerenityPlayer.loadAsync(player.getUniqueId(), player);

        Bukkit.getScheduler().runTaskLater(Serenity.getPlugin(), new Runnable() {
            @Override
            public void run() {
                SerenityBoard board = SerenityBoard.createScore(player);
                board.setTitle("&aSerenity");

                SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayerMap().get(player.getUniqueId());
                HashMap<Integer, String> abilities = sPlayer.getAbilities();
                for (int i : abilities.keySet()) {
                    board.setSlot(i, abilities.get(i));
                }
            }
        }, 50L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        SerenityBoard.removeScore(player);
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
