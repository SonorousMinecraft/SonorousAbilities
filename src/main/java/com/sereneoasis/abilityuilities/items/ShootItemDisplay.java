package com.sereneoasis.abilityuilities.items;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Display;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Vectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ShootItemDisplay extends CoreAbility {

    private final String name;

    private ItemDisplay itemDisplay;

    private ArmorStand armorStand;

    private Location tempLoc;

    private Vector dir;

    private double oldPitch, size;

    private boolean stick;

    public ShootItemDisplay(Player player, String name, Location loc, Vector dir, Material material, double size, boolean stick, boolean diagonal) {
        super(player, name);

        this.name = name;
        this.dir = dir.clone();
        this.stick = stick;
        this.size = size;

        abilityStatus = AbilityStatus.SHOT;

        tempLoc = loc.clone();
        tempLoc.setDirection(dir.clone());
        oldPitch = tempLoc.getPitch();

        itemDisplay = Display.createItemDisplay(loc, material, size, diagonal);
        armorStand = Display.createArmorStand(loc, false);

        armorStand.setVelocity(dir.clone().multiply(speed));
        start();
    }

    @Override
    public void progress() {

        if (abilityStatus != AbilityStatus.COMPLETE) {
            Transformation transformation = itemDisplay.getTransformation();
            Quaternionf quaternionf = transformation.getLeftRotation();
            if (Vectors.getAngleBetweenVectors(dir, armorStand.getVelocity()) > 0.1) {
                tempLoc.setDirection(armorStand.getVelocity());
                quaternionf.rotateX((float) Math.toRadians(tempLoc.getPitch() - oldPitch));
                oldPitch = tempLoc.getPitch();
                transformation.getLeftRotation().set(quaternionf);
            }
            itemDisplay.setTransformation(transformation);


            armorStand.setVelocity(dir.clone().multiply(speed));
            Vector offsetFix = new Vector(size/2, 0, size/2).rotateAroundY(-Math.toRadians(armorStand.getLocation().getYaw()));
            itemDisplay.teleport(armorStand.getLocation().clone().add(offsetFix));

            if (stick) {
                Block b = Entities.getCollidedBlock(armorStand);
                if (b != null) {
                    armorStand.setVelocity(new Vector(0, 0, 0));
                    armorStand.setGravity(false);
                    itemDisplay.teleport(armorStand);

                    abilityStatus = AbilityStatus.COMPLETE;
                }
            }
        }
    }


    public void setDir(Vector dir) {
        this.dir = dir;
    }


    public ArmorStand getArmorStand() {
        return armorStand;
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
        if (armorStand != null) {
            armorStand.remove();
        }
        if (itemDisplay != null) {
            itemDisplay.remove();
        }
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return name;
    }
}
