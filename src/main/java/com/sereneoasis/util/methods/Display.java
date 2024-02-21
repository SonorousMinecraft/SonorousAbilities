package com.sereneoasis.util.methods;

import net.minecraft.util.datafix.fixes.EntityArmorStandSilentFix;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftVex;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3d;

import java.lang.reflect.Field;

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

    public static ItemDisplay createItemDisplayNoTransform(Location loc, Material material, double size, ItemDisplay.ItemDisplayTransform transform, org.bukkit.entity.Display.Billboard billboard) {
        ItemDisplay itemDisplay = (ItemDisplay) loc.getWorld().spawn(loc, EntityType.ITEM_DISPLAY.getEntityClass(), (entity) ->
        {
            ItemDisplay iDisplay = (ItemDisplay) entity;
            ItemStack itemStack = new ItemStack(material);

            iDisplay.setItemStack(itemStack);
            iDisplay.setBillboard(billboard);
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

    public static ArmorStand createArmorStandBig(Location loc) {
        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawn(loc, EntityType.ARMOR_STAND.getEntityClass(), ((entity) ->
        {
            ArmorStand aStand = (ArmorStand) entity;
            aStand.setInvulnerable(true);
            aStand.setVisible(false);
        }));
        return armorStand;
    }
}
