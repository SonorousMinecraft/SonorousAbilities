package com.sereneoasis.listeners;

import com.sereneoasis.CoreAbility;
import com.sereneoasis.airbending.AirBlast;
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

    }

    @EventHandler
    public void onSwing(PlayerInteractEvent e)
    {
        if (e.getPlayer().getName().equals("Sakrajin")) {
            if (CoreAbility.hasAbility(e.getPlayer(), AirBlast.class)) {
                CoreAbility.getAbility(e.getPlayer(), AirBlast.class).setHasClicked();
            }
        }
    }

    @EventHandler
    public void onShift(PlayerToggleSneakEvent e)
    {
        if (e.getPlayer().getName().equals("Sakrajin")) {
            new AirBlast(e.getPlayer());
        }
    }
}
