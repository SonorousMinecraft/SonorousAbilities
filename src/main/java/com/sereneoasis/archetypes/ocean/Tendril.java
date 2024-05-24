package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempDisplayBlock;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Tendril extends CoreAbility {

    private HashMap<TempDisplayBlock, Double> tendrils = new HashMap<>();

    public Set<TempDisplayBlock> getTendrils() {
        return tendrils.keySet();
    }

    public void clearTendrilMap(){
        tendrils = new HashMap<>();
    }

    private TempDisplayBlock end;

    private final String name;

    private Vector dir;

    private double grabDistance, grappleDistance;

    private double length;

    private Entity grabTarget;

    private Block grappleBlock;


    public Tendril(Player player, String name, double length) {
        super(player, name);
        this.name = name;
        this.length = length;
        if (shouldStartCanHaveMultiple()){
            int amount = Math.round(Math.round(length/size));

            Location origin = player.getEyeLocation().subtract(player.getEyeLocation().getDirection());
            Vector dir = origin.getDirection();
            for (int i = 0; i < amount; i++){
                double distance = i * size/2;
//                Location displayBlockLoc = origin.clone().add(dir.clone().multiply(distance));
                TempDisplayBlock displayBlock = new TempDisplayBlock(origin, DisplayBlock.WATER, duration, size);

                tendrils.put(displayBlock, distance);
                end = displayBlock;
            }

            abilityStatus = AbilityStatus.MOVING;
            start();
        }
    }

    public TempDisplayBlock getEnd() {
        return end;
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (System.currentTimeMillis() - duration > startTime){
            abilityStatus = AbilityStatus.COMPLETE;
        }

        switch (abilityStatus){
            // For grab
            case GRAB -> {
                if (grabDistance > range){
                    endGrab();
                }
                if (grabTarget == null) {
                    grabDistance += speed;

                    Location origin = player.getEyeLocation().subtract(player.getEyeLocation().getDirection());

                    tendrils.forEach((tempDisplayBlock, distance) -> {

                        Location displayBlockLoc = origin.clone().add(dir.clone().multiply(distance/length * grabDistance));

//                    Vector toMove = Vectors.getDirectionBetweenLocations(origin.clone().add(player.getEyeLocation().getDirection().clone().multiply(distance)), displayBlockLoc);
                        Vector toMove = Vectors.getDirectionBetweenLocations(tempDisplayBlock.getLoc(), displayBlockLoc);

                        if (toMove.lengthSquared() > 1) {
                            toMove.normalize();
                            Location toMoveLocation = tempDisplayBlock.getLoc().clone().add(toMove);
                            tempDisplayBlock.moveTo(toMoveLocation);
                        } else  {
                            tempDisplayBlock.moveTo(displayBlockLoc);
                        }
                    });

                } else {

                    Location origin = player.getEyeLocation().subtract(player.getEyeLocation().getDirection());

                    tendrils.forEach((tempDisplayBlock, distance) -> {

                        Location displayBlockLoc = origin.clone().add(player.getEyeLocation().getDirection().clone().multiply(distance));

                        Vector toMove = Vectors.getDirectionBetweenLocations(tempDisplayBlock.getLoc(), displayBlockLoc);

                        if (toMove.lengthSquared() > 1) {
                            toMove.normalize();
                            Location toMoveLocation = tempDisplayBlock.getLoc().clone().add(toMove);
                            tempDisplayBlock.moveTo(toMoveLocation);
                        } else  {
                            tempDisplayBlock.moveTo(displayBlockLoc);
                        }
                    });
                }


                if (grabTarget ==null && Entities.getAffected(end.getLoc(), size *2, player)!= null){
                    grabTarget = Entities.getAffected(end.getLoc(), size *2, player);
                }
                if (grabTarget != null){
                    net.minecraft.world.entity.Entity entity = ((CraftEntity) grabTarget).getHandle();
                    entity.moveTo(new Vec3(end.getLoc().toVector().toVector3f()));
//                    grabTarget.setVelocity(Vectors.getDirectionBetweenLocations(end.getLoc(), grabTarget.getLocation()).normalize());
                }
            }

            case GRAPPLE -> {
                if (grappleDistance > range){
                    abilityStatus = AbilityStatus.MOVING;

                }
                if (grappleBlock == null) {
                    grappleDistance += speed;


                    Location origin = player.getEyeLocation();
                    tendrils.forEach((tempDisplayBlock, distance) -> {

                        Location displayBlockLoc = origin.clone().add(dir.clone().multiply(distance/length * grappleDistance));

//                    Vector toMove = Vectors.getDirectionBetweenLocations(origin.clone().add(player.getEyeLocation().getDirection().clone().multiply(distance)), displayBlockLoc);
                        Vector toMove = Vectors.getDirectionBetweenLocations(tempDisplayBlock.getLoc(), displayBlockLoc);

                        if (toMove.lengthSquared() > 1) {
                            toMove.normalize();
                            Location toMoveLocation = tempDisplayBlock.getLoc().clone().add(toMove);
                            tempDisplayBlock.moveTo(toMoveLocation);
                        } else  {
                            tempDisplayBlock.moveTo(displayBlockLoc);
                        }
                    });
                } else {
                    Location origin = player.getEyeLocation();
                    tendrils.forEach((tempDisplayBlock, distance) -> {

                        Vector dir = Vectors.getDirectionBetweenLocations(player.getEyeLocation(), grappleBlock.getLocation());
                        Location displayBlockLoc = origin.clone().add(dir.clone().multiply(distance/length * dir.length()));

//                    Vector toMove = Vectors.getDirectionBetweenLocations(origin.clone().add(player.getEyeLocation().getDirection().clone().multiply(distance)), displayBlockLoc);
                        Vector toMove = Vectors.getDirectionBetweenLocations(tempDisplayBlock.getLoc(), displayBlockLoc);

                        if (toMove.lengthSquared() > 1) {
                            toMove.normalize();
                            Location toMoveLocation = tempDisplayBlock.getLoc().clone().add(toMove);
                            tempDisplayBlock.moveTo(toMoveLocation);
                        } else  {
                            tempDisplayBlock.moveTo(displayBlockLoc);
                        }
                    });
                }


                if (grappleBlock == null && !end.getLoc().getBlock().isPassable()) {
                    grappleBlock = end.getLoc().getBlock();
                }

                if (grappleBlock != null){
                    player.setVelocity(Vectors.getDirectionBetweenLocations(player.getEyeLocation(), grappleBlock.getLocation()).normalize().multiply(speed));
                    if (player.getEyeLocation().distanceSquared(grappleBlock.getLocation()) < 9){
                        endGrapple();
                    }
                }
            }
        }
    }


    public void endGrab(){
        abilityStatus = AbilityStatus.MOVING;
        grabTarget = null;
    }

    public void endGrapple(){
        abilityStatus = AbilityStatus.MOVING;
        grappleBlock = null;
    }

    private Random random = new Random();

    public void setTendrilBlock(DisplayBlock block){
        tendrils.keySet().forEach(tempDisplayBlock -> {
            Material type = block.getBlocks().get(random.nextInt(block.getBlocks().size()));

            tempDisplayBlock.getBlockDisplay().setBlock(type.createBlockData());
        });
    }

    public void move(Vector dir){
        Location origin = player.getEyeLocation().subtract(player.getEyeLocation().getDirection());

        tendrils.forEach((tempDisplayBlock, distance) -> {

            Location displayBlockLoc = origin.clone().add(dir.clone().multiply(distance)).add( player.getEyeLocation().getDirection().clone().multiply(Math.log(Math.max(1,distance/2)) + distance/2));
            Vector toMove = Vectors.getDirectionBetweenLocations(tempDisplayBlock.getLoc(), displayBlockLoc);

            if (toMove.lengthSquared() > 1) {
                toMove.normalize();
                Location toMoveLocation = tempDisplayBlock.getLoc().clone().add(toMove);
                tempDisplayBlock.moveTo(toMoveLocation);
            } else  {
                tempDisplayBlock.moveTo(displayBlockLoc);
            }
        });
    }


    public void grab(Vector dir){
        abilityStatus = AbilityStatus.GRAB;
        this.dir = dir.clone();
//        distance = tendrils.size() * size;
        grabDistance = 0;
    }

    public void grapple(Vector dir){
        abilityStatus = AbilityStatus.GRAPPLE;
        this.dir = dir.clone();
//        distance = tendrils.size() * size;
        grappleDistance = 0;
    }


    @Override
    public void remove() {
        super.remove();
        tendrils.keySet().forEach(tempDisplayBlock -> tempDisplayBlock.revert());
    }

    @Override
    public String getName() {
        return name;
    }
}
