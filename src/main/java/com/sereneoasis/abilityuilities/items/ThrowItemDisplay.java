package com.sereneoasis.abilityuilities.items;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Display;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Vectors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.*;

public class ThrowItemDisplay extends CoreAbility {

    private final String name;

    private ArmorStand armorStand;

    private Location origin;

    private Vector dir;

    private boolean stick;

    private Set<ItemDisplay>displays = new HashSet<>();

    public ThrowItemDisplay(Player player, String name, Location loc, Vector dir, Material material, double width, double length, boolean stick, boolean diagonal, boolean sphere) {
        super(player, name);

        this.name = name;
        this.dir = dir.clone();
        this.stick = stick;

        abilityStatus = AbilityStatus.SHOT;

        this.origin = loc.clone();

        armorStand = Display.createArmorStand(loc);

        double height = width;



        double scale = 1;

        double pixelWidth = width * 1/16;
        double distance = pixelWidth/2;
        // 0, 1, 0, 1, 2, 2
        int[] numbers = {0, 1, 0, 1, 2, 2};
        for (int i : numbers) {


            scale -= (double) i*2 * pixelWidth;
            ItemDisplay leftDisplay = Display.createItemDisplayOffset(loc, material, width  , height * scale, length * scale, diagonal, distance, 0, 0);
            ItemDisplay rightDisplay = Display.createItemDisplayOffset(loc, material, width , height * scale,length * scale, diagonal, -distance, 0, 0);
            armorStand.addPassenger(leftDisplay);
            armorStand.addPassenger(rightDisplay);
            distance += pixelWidth;
        }

        armorStand.setVelocity(dir.clone().multiply(speed));
        abilityStatus = AbilityStatus.SHOT;

        start();
    }



    @Override
    public void progress() {

        if (abilityStatus != AbilityStatus.COMPLETE) {


            if (armorStand.getLocation().distance(origin) > range) {
                abilityStatus = AbilityStatus.COMPLETE;
            }

            Block b = armorStand.getLocation().getBlock();
            if (b.getType().isSolid()) {
                armorStand.setVelocity(new Vector(0, 0, 0));
                armorStand.setGravity(false);

                abilityStatus = AbilityStatus.COMPLETE;
                if (!stick) {
                    for (ItemDisplay currentDisplay : displays){
                        currentDisplay.remove();
                    }
                }
            }


        }
    }


    public Location getLoc(){
        return armorStand.getLocation();
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
