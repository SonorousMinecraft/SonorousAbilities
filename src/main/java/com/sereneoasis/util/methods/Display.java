package com.sereneoasis.util.methods;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.*;

import java.lang.Math;

public class Display {

    public static ItemDisplay createItemDisplay(Location loc, Material material, Vector dir, double size)
    {
        ItemDisplay itemDisplay = (ItemDisplay) loc.getWorld().spawn(loc, EntityType.ITEM_DISPLAY.getEntityClass(), (entity) ->
        {
            ItemDisplay iDisplay = (ItemDisplay) entity;
            ItemStack itemStack = new ItemStack(material);
            iDisplay.setItemStack(itemStack);
            iDisplay.setBillboard(org.bukkit.entity.Display.Billboard.FIXED);
            Transformation transformation = iDisplay.getTransformation();

            transformation.getScale().set(size);
            Vector orth = Vectors.getDirectionBetweenLocations(Locations.getLeftSide(loc, 0.5), Locations.getRightSide(loc, 0.5)  );

            Quaternionf quaternionf = transformation.getLeftRotation();
            quaternionf = quaternionf.rotateY((float) Math.toRadians(90));
            quaternionf = quaternionf.rotateAxis((float) -Math.toRadians(22.5), new Vector3f((float) orth.getX(), 0, (float) orth.getZ()));
            transformation.getLeftRotation().set(quaternionf);
            iDisplay.setTransformation(transformation);
        });
        return itemDisplay;
    }

    public static ArmorStand createArmorStand(Location loc)
    {
        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawn(loc, EntityType.ARMOR_STAND.getEntityClass(), ((entity) ->
        {
            ArmorStand aStand = (ArmorStand) entity;
            aStand.setInvulnerable(true);
            aStand.setVisible(false);
        }));
        return armorStand;
    }

}
