package com.sereneoasis.ability.superclasses;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;

public interface CollisionAbility {

    interface CollisionHandler{
        void handleCollision(Location loc);
    }


    HashMap<Location, Double> getLocs();

    void handleCollisions(HashMap<Location, Double> colliders);

}
