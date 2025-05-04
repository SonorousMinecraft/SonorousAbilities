package com.sonorous.ability.superclasses;

import org.bukkit.Location;

import java.util.HashMap;

public interface CollisionAbility {

    HashMap<Location, Double> getLocs();

    void handleCollisions(HashMap<Location, Double> colliders);

    interface CollisionHandler {
        void handleCollision(Location loc);
    }

}
