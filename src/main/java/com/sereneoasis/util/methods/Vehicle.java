package com.sereneoasis.util.methods;

import org.bukkit.Location;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;

public class Vehicle {

    public static Boat createBoatVehicle(Location loc){
        Boat boat = (Boat) loc.getWorld().spawn(loc, EntityType.BOAT.getEntityClass(), (entity -> {
            Boat boatEntity = (Boat) entity;
            boatEntity.setInvulnerable(true);
            //boatEntity.setVisibleByDefault(false);
        }));
        return boat;
    }
}
