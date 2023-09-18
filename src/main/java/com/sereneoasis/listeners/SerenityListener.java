package com.sereneoasis.listeners;

import com.sereneoasis.airbending.AirBlast;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class SerenityListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {

    }

    @EventHandler
    public void onSwing(PlayerInteractEvent e)
    {
        new AirBlast(e.getPlayer());
    }
}
