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

    private Vector dir, orth;

    private double oldPitch;

    private boolean mightHaveStopped = false, stick;

    public ShootItemDisplay(Player player, String name, Location loc, Vector dir, Material material, double size, boolean stick) {
        super(player, name);

        this.name = name;
        this.dir = dir.clone();
        this.stick = stick;

        abilityStatus = AbilityStatus.SHOT;

        orth = Vectors.getDirectionBetweenLocations(Locations.getLeftSide(loc, 0.5), Locations.getRightSide(loc, 0.5) );

        tempLoc = loc.clone();
        tempLoc.setDirection(dir.clone());
        oldPitch = tempLoc.getPitch();

        itemDisplay = Display.createItemDisplay(loc, material, dir, size);
        armorStand = Display.createArmorStand(loc);
        armorStand.setVelocity(dir.clone().multiply(speed));
        start();
    }

    @Override
    public void progress() {

        if (abilityStatus != AbilityStatus.COMPLETE) {
            if (armorStand.getVelocity().length() < 0.2) {
                if (mightHaveStopped) {
                    this.abilityStatus = AbilityStatus.COMPLETE;
                }
                mightHaveStopped = true;
            } else {

                Transformation transformation = itemDisplay.getTransformation();
                if (Vectors.getAngleBetweenVectors(dir, armorStand.getVelocity()) > 0.1) {
                    tempLoc.setDirection(armorStand.getVelocity());
                    Quaternionf quaternionf = transformation.getLeftRotation();
                    quaternionf = quaternionf.rotateAxis((float) Math.toRadians(tempLoc.getPitch() - oldPitch),
                            new Vector3f((float) orth.getX(), (float) orth.getY(), (float) orth.getZ()));
                    oldPitch = tempLoc.getPitch();
                    transformation.getLeftRotation().set(quaternionf);
                    itemDisplay.setTransformation(transformation);
                }

                dir = armorStand.getVelocity().clone().normalize();
                itemDisplay.teleport(armorStand);

                if (stick) {
                    Block b = Entities.getCollidedBlock(armorStand);
                    if (b!= null)
                    {
                        armorStand.setVelocity(new Vector(0,0,0));
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
        if (armorStand != null)
        {
            armorStand.remove();
        }
        if (itemDisplay != null)
        {
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
