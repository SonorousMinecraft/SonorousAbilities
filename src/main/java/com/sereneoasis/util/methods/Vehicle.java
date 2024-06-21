package com.sereneoasis.util.methods;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftBoat;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPig;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;

public class Vehicle {

    public static Boat createBoatVehicle(Location loc) {
        Boat boat = (Boat) loc.getWorld().spawn(loc, EntityType.BOAT.getEntityClass(), (entity -> {
            Boat boatEntity = (Boat) entity;
            boatEntity.setInvulnerable(true);
            CraftBoat craftBoat = (CraftBoat) boatEntity;
            craftBoat.setMaxSpeed(2);
            craftBoat.setWorkOnLand(true);
            craftBoat.setBoatType(Boat.Type.OAK);
            net.minecraft.world.entity.vehicle.Boat nmsBoat = craftBoat.getHandle();
            nmsBoat.setMaxUpStep(10);
            nmsBoat.setInvisible(true);
        }));
        return boat;
    }


    public static Pig createPigVehicle(Location loc) {
        Pig pig = (Pig) loc.getWorld().spawn(loc, EntityType.PIG.getEntityClass(), (entity -> {
            Pig pigEntity = (Pig) entity;
            pigEntity.setInvulnerable(true);
            //  pigEntity.setInvisible(true);
            // pigEntity.setBaby();
            CraftPig craftPig = (CraftPig) pigEntity;
            craftPig.setSaddle(true);
            craftPig.setAware(false);
        }));
        return pig;
    }
}
