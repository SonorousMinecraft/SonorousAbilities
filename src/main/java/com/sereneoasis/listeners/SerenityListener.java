package com.sereneoasis.listeners;

import com.sereneoasis.CoreAbility;
import com.sereneoasis.PlayerData;
import com.sereneoasis.SerenityPlayer;
import com.sereneoasis.airbending.AirBlast;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;


public class SerenityListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();
        SerenityPlayer.loadAsync(player.getUniqueId(), player);
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
                Bukkit.broadcastMessage("on click case check");
                if (CoreAbility.hasAbility(e.getPlayer(), AirBlast.class)) {
                    Bukkit.broadcastMessage("player has selected source");
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
                Bukkit.broadcastMessage("on shift case check");
                new AirBlast(player);
                break;
        }
    }
}
