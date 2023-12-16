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

public class ThrowItemDisplay extends CoreAbility {

    private final String name;

    private ItemDisplay itemDisplay;

    private ArmorStand armorStand;

    private Vector dir, orth;

    private double oldPitch, size;

    private boolean mightHaveStopped = false, stick;

    public ThrowItemDisplay(Player player, String name, Location loc, Vector dir, Material material, double size, boolean stick, boolean diagonal) {
        super(player, name);

        this.name = name;
        this.dir = dir.clone();
        this.stick = stick;
        this.size = size;

        abilityStatus = AbilityStatus.SHOT;

        orth = Vectors.getDirectionBetweenLocations(Locations.getLeftSide(loc, 0.5), Locations.getRightSide(loc, 0.5));

        itemDisplay = Display.createItemDisplay(loc, material, size, diagonal);
        armorStand = Display.createArmorStand(loc, true);
        armorStand.setVelocity(dir.clone().multiply(speed));
        oldPitch = armorStand.getLocation().getPitch();
        start();
    }

    @Override
    public void progress() {

        if (abilityStatus != AbilityStatus.COMPLETE) {
            if (armorStand.getVelocity().length() < 0.01) {
                if (mightHaveStopped) {
                    this.abilityStatus = AbilityStatus.COMPLETE;
                }
                mightHaveStopped = true;
            } else {

                Transformation transformation = itemDisplay.getTransformation();
                if (Vectors.getAngleBetweenVectors(dir, armorStand.getVelocity()) > 0.1) {
                    Quaternionf quaternionf = transformation.getLeftRotation();
                    quaternionf = quaternionf.rotateAxis((float) Math.toRadians(armorStand.getLocation().getPitch() - oldPitch),
                            new Vector3f((float) orth.getX(), (float) orth.getY(), (float) orth.getZ()));
                    oldPitch = armorStand.getLocation().getPitch();
                    transformation.getLeftRotation().set(quaternionf);
                    itemDisplay.setTransformation(transformation);
                }

                dir = armorStand.getVelocity().clone().normalize();
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
