package com.sereneoasis.util.methods;

import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
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
            transformation.getTranslation().set(new Vector3d(-size / 2, 0, 0));
            Quaternionf quaternionf = transformation.getLeftRotation();

            double faceForward;
            if (diagonal) {
                faceForward = 75;
            } else {
                faceForward = 90;
            }
            quaternionf.rotateXYZ(0, (float) -Math.toRadians(90), (float) -Math.toRadians(faceForward));
            transformation.getLeftRotation().set(quaternionf);
            transformation.getTranslation().set(new Vector3d(0, 0, 0));
            iDisplay.setTransformation(transformation);
        });
        return itemDisplay;
    }

    public static ItemDisplay createItemDisplay(Location loc, Material material, double width, double length, boolean diagonal) {
        ItemDisplay itemDisplay = (ItemDisplay) loc.getWorld().spawn(loc, EntityType.ITEM_DISPLAY.getEntityClass(), (entity) ->
        {
            ItemDisplay iDisplay = (ItemDisplay) entity;
            ItemStack itemStack = new ItemStack(material);
            iDisplay.setItemStack(itemStack);
            iDisplay.setBillboard(org.bukkit.entity.Display.Billboard.FIXED);
            Transformation transformation = iDisplay.getTransformation();
            transformation.getScale().set(width, length, width);
            transformation.getTranslation().set(new Vector3d(-width / 2, 0, 0));
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
// Positive x is left, positive y is up, positive z is forward
    public static ItemDisplay createItemDisplayOffset(Location loc, Material material, double width, double height, double length, boolean diagonal, double x, double y, double z) {
        ItemDisplay itemDisplay = (ItemDisplay) loc.getWorld().spawn(loc, EntityType.ITEM_DISPLAY.getEntityClass(), (entity) ->
        {
            ItemDisplay iDisplay = (ItemDisplay) entity;
            ItemStack itemStack = new ItemStack(material);
            iDisplay.setItemStack(itemStack);
            iDisplay.setBillboard(org.bukkit.entity.Display.Billboard.FIXED);
            Transformation transformation = iDisplay.getTransformation();
            // first one is height, second length, third width
            transformation.getScale().set(height , length , width);
            transformation.getTranslation().set(new Vector3d( x, y , z));

            Quaternionf quaternionf = transformation.getLeftRotation();

            double faceForward;
            if (diagonal) {
                faceForward = 45;
            } else {
                faceForward = 90;
            }
            quaternionf.rotateXYZ(0, (float) -Math.toRadians(90), (float) -Math.toRadians(faceForward));
            transformation.getLeftRotation().set(quaternionf);
            iDisplay.setTransformation(transformation);
        });
        return itemDisplay;
    }


    public static ItemDisplay createItemDisplayNoTransform(Location loc, Material material, double size, ItemDisplay.ItemDisplayTransform transform, org.bukkit.entity.Display.Billboard billboard) {
        ItemDisplay itemDisplay = (ItemDisplay) loc.getWorld().spawn(loc, EntityType.ITEM_DISPLAY.getEntityClass(), (entity) ->
        {
            ItemDisplay iDisplay = (ItemDisplay) entity;
            ItemStack itemStack = new ItemStack(material);
            iDisplay.setItemStack(itemStack);
//            iDisplay.setBillboard(billboard);
            iDisplay.setItemDisplayTransform(transform);
            Transformation transformation = iDisplay.getTransformation();
            transformation.getScale().set(size);
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
        Entity nmsStand = ((CraftArmorStand)armorStand).getHandle();
        nmsStand.noPhysics = true;
        return armorStand;
    }

    public static ArmorStand createArmorStandClip(Location loc) {

        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawn(loc, EntityType.ARMOR_STAND.getEntityClass(), ((entity) ->
        {
            ArmorStand aStand = (ArmorStand) entity;
            aStand.setInvulnerable(true);
            aStand.setSmall(true);
            aStand.setVisible(false);
        }));
        return armorStand;
    }

    public static ArmorStand createArmorStandBig(Location loc) {
        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawn(loc, EntityType.ARMOR_STAND.getEntityClass(), ((entity) ->
        {
            ArmorStand aStand = (ArmorStand) entity;
            aStand.setInvulnerable(true);
            aStand.setVisible(false);
        }));
        return armorStand;
    }

    public static Location getItemDisplaySpawnLoc(Location loc, double size){
        Vector offsetFix = new Vector(size / 2, 0, size / 2).rotateAroundY(-Math.toRadians(loc.getYaw()));
        return loc.clone().add(offsetFix);
    }
}
