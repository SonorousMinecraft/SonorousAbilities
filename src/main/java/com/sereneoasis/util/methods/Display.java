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

    public static ItemDisplay createItemDisplay(Location loc, Material material, double size, boolean diagonal)
    {
        ItemDisplay itemDisplay = (ItemDisplay) loc.getWorld().spawn(loc, EntityType.ITEM_DISPLAY.getEntityClass(), (entity) ->
        {
            ItemDisplay iDisplay = (ItemDisplay) entity;
            ItemStack itemStack = new ItemStack(material);
            iDisplay.setItemStack(itemStack);
            iDisplay.setBillboard(org.bukkit.entity.Display.Billboard.FIXED);
            Transformation transformation = iDisplay.getTransformation();
            transformation.getScale().set(size);
            transformation.getTranslation().set(new Vector3d(-size/2, 0,-size/2));
            Quaternionf quaternionf = transformation.getLeftRotation();

            double faceForward;
            if (diagonal)
            {
                faceForward = 45;
            }
            else{
                faceForward = 90;
            }
            quaternionf.rotateXYZ((float) Math.toRadians(90), (float) -Math.toRadians(faceForward), 0);
            transformation.getLeftRotation().set(quaternionf);
            iDisplay.setTransformation(transformation);
        });
        return itemDisplay;
    }

    public static ArmorStand createArmorStand(Location loc, boolean hasGravity)
    {
        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawn(loc, EntityType.ARMOR_STAND.getEntityClass(), ((entity) ->
        {
            ArmorStand aStand = (ArmorStand) entity;
            aStand.setInvulnerable(true);
            aStand.setSmall(true);
            aStand.setVisible(false);
            //aStand.setGravity(hasGravity);
        }));
        return armorStand;
    }

}
