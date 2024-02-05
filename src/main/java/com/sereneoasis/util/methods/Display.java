package com.sereneoasis.util.methods;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3d;

public class Display {

    public static ItemDisplay createItemDisplay(Location loc, Material material, double size, boolean diagonal) {
        ItemDisplay itemDisplay = (ItemDisplay) loc.getWorld().spawn(loc, EntityType.ITEM_DISPLAY.getEntityClass(), (entity) ->
        {
            ItemDisplay iDisplay = (ItemDisplay) entity;
            ItemStack itemStack = new ItemStack(material);
            iDisplay.setItemStack(itemStack);
            iDisplay.setBillboard(org.bukkit.entity.Display.Billboard.FIXED);
            Transformation transformation = iDisplay.getTransformation();
            transformation.getScale().set(size);
            transformation.getTranslation().set(new Vector3d(-size / 2, 0, -size / 2));
            Quaternionf quaternionf = transformation.getLeftRotation();

            double faceForward;
            if (diagonal) {
                faceForward = 75;
            } else {
                faceForward = 90;
            }
            quaternionf.rotateXYZ(0, (float) -Math.toRadians(90), (float) -Math.toRadians(faceForward));
            transformation.getLeftRotation().set(quaternionf);
            iDisplay.setTransformation(transformation);
        });
        return itemDisplay;
    }

    public static ArmorStand createArmorStand(Location loc) {
        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawn(loc, EntityType.ARMOR_STAND.getEntityClass(), ((entity) ->
        {
            ArmorStand aStand = (ArmorStand) entity;
            aStand.setInvulnerable(true);
            aStand.setSmall(true);
            aStand.setVisible(false);
        }));
        return armorStand;
    }

}
