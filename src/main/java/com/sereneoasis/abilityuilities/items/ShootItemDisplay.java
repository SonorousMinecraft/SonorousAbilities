package com.sereneoasis.abilityuilities.items;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Display;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class ShootItemDisplay extends CoreAbility {

    private final String name;

    private ItemDisplay itemDisplay;

    private ArmorStand armorStand;

    private Location origin, tempLoc;

    private Vector dir;

    private double oldPitch, size;

    private boolean stick;

    private Set<ItemDisplay>displays = new HashSet<>();

    private Vector offsetFix;

    public ShootItemDisplay(Player player, String name, Location loc, Vector dir, Material material, double size, boolean stick, boolean diagonal) {
        super(player, name);

        this.name = name;
        this.dir = dir.clone();
        this.stick = stick;
        this.size = size;

        abilityStatus = AbilityStatus.SHOT;

        this.origin = loc.clone();

        tempLoc = loc.clone();
        tempLoc.setDirection(dir.clone());
        oldPitch = tempLoc.getPitch();


        offsetFix = new Vector(size / 2, 0, size / 2).rotateAroundY(-Math.toRadians(loc.getYaw()));
        Location offsetLocation = loc.clone().add(offsetFix);
        itemDisplay = Display.createItemDisplay(loc, material, size, diagonal);
        armorStand = Display.createArmorStand(offsetLocation);
        armorStand.addPassenger(itemDisplay);

        armorStand.setVelocity(dir.clone().multiply(speed));
        abilityStatus = AbilityStatus.SHOT;


        Location straightLoc = loc.clone();
        straightLoc.setPitch(0);
        Vector straightDir = straightLoc.getDirection().normalize();
        Vector relativeUpright = straightDir.getCrossProduct(new Vector(0,1,0));
        Bukkit.broadcastMessage(String.valueOf(relativeUpright));
        Vector pitchFixMiddle = relativeUpright.clone().rotateAroundAxis(dir, Math.toRadians(90)).multiply(size/2 * loc.getPitch() /90);



        ItemDisplay middle = Display.createItemDisplay(Display.getItemDisplaySpawnLoc(getLoc().add(pitchFixMiddle), size ) , material, size , false);
        displays.add(middle);

        double currentDistance = 0;
        double currentSize = size;
        double pixelDimension = 32;

        for (int i = 0; i < pixelDimension/4; i++) {
            double distance = currentSize * 2/pixelDimension + currentDistance;
            currentDistance = distance;
            double tempSize = currentSize - size*4/(pixelDimension) ;
            currentSize = tempSize;


            Vector pitchFix = relativeUpright.clone().rotateAroundAxis(dir, Math.toRadians(90)).multiply(tempSize/2 * loc.getPitch() / 90);

            Location leftLoc = Locations.getLeftSideFromPlayerView(getLoc(), player, distance).add(pitchFix);


            ItemDisplay left = Display.createItemDisplay(Display.getItemDisplaySpawnLoc(leftLoc, tempSize ) ,material, tempSize, false);
            displays.add(left);
            Location rightLoc = Locations.getRightSideFromPlayerView(getLoc(), player, distance).add(pitchFix);

            ItemDisplay right = Display.createItemDisplay(Display.getItemDisplaySpawnLoc(rightLoc, tempSize ) , material, tempSize, false);
            displays.add(right);
        }


        start();
    }

    @Override
    public void progress() {

        if (abilityStatus != AbilityStatus.COMPLETE) {



            for (ItemDisplay currentDisplay : displays){


                Location tempLoc = currentDisplay.getLocation().add(dir.clone().multiply(speed));
                //tempLoc.setY(getLoc().getY() + (size - currentDisplay.getHeight())/2);
                currentDisplay.teleport(tempLoc);

            }

            armorStand.setVelocity(dir.clone().multiply(speed));


//            for (Location tempItemDisplays : Locations.getPointsAlongLine(Locations.getLeftSide(getLoc(), dir,(size/2)), Locations.getRightSide(getLoc(), dir,(size/2)), size/16)) {
//                double sizeRatio =  ( (size/2) - Math.abs(getLoc().distance(tempItemDisplays)) ) / (size/2);
//                double distanceRatio = 1-sizeRatio; // the distance from the midpoint as a ratio
//                double tempSize = size * sizeRatio;
//
//                Vector toMidpoint = Vectors.getDirectionBetweenLocations(tempItemDisplays, getLoc()).normalize();
//                double internalFixing = 15.5/16;
//                double noSizeDiffLength = Vectors.getDirectionBetweenLocations(tempItemDisplays, getLoc()).length();
//                double actualDistance = noSizeDiffLength
//                double correction = noSizeDiffLength - actualDistance;
//                Vector offsetCorrection = toMidpoint.clone().multiply(correction ); //Subtract this from the loc
//
//
//                //Particles.spawnParticle(Particle.FLAME, tempItemDisplays, 1, 0, 0);
//
//                Location finalLoc = tempItemDisplays.clone().subtract(offsetCorrection);
//                finalLoc.setY(getLoc().getY());
//                Display.createItemDisplay(Display.getItemDisplaySpawnLoc(finalLoc, size), Material.FIREWORK_ROCKET, tempSize  , false);
//            }



            if (armorStand.getLocation().distance(origin) > range) {
                abilityStatus = AbilityStatus.COMPLETE;
            }

            Block b = getLoc().getBlock();
            if (b.getType().isSolid()) {
                armorStand.setVelocity(new Vector(0, 0, 0));
                armorStand.setGravity(false);

                abilityStatus = AbilityStatus.COMPLETE;
                if (!stick) {
                    itemDisplay.remove();
                    for (ItemDisplay currentDisplay : displays){
                        currentDisplay.remove();
                    }
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

    public Location getLoc() {
        return armorStand.getLocation().clone().subtract(offsetFix);
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
        if (itemDisplay != null) {
            itemDisplay.remove();
        }
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
