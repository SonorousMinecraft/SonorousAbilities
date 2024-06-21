package com.sereneoasis.ability.superclasses;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.util.Vector;

import java.util.HashMap;

public interface RedirectAbility {

    boolean hasCustomRedirect();

    HashMap<Location, Double> getLocs();

    void handleRedirects(Player redirectingPlayer, ClickType clickType);

    Vector getDir();

    void setDir(Vector dir);

    interface RedirectHandler {
        void handleRedirect(Location loc);
    }

}
