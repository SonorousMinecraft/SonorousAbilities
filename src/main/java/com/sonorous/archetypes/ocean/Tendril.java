package com.sonorous.archetypes.ocean;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.archetypes.DisplayBlock;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.Blocks;
import com.sonorous.util.methods.Entities;
import com.sonorous.util.methods.Vectors;
import com.sonorous.util.temp.TempDisplayBlock;
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

    private final String name;
    private HashMap<TempDisplayBlock, Double> tendrils = new HashMap<>();
    private TempDisplayBlock end;
    private Vector dir;
    private double grabDistance, grappleDistance;
    private double length;
    private Entity grabTarget;
    private Block grappleBlock;
    private long sinceLastGrappled = System.currentTimeMillis();
    private Location oldPlayerLoc = player.getEyeLocation();
    private Random random = new Random();

    public Tendril(Player player, String name, double length) {
        super(player, name);
        this.name = name;
        this.length = length;
        if (shouldStartCanHaveMultiple()) {
            int amount = Math.round(Math.round(length / size));

            Location origin = player.getEyeLocation().subtract(player.getEyeLocation().getDirection().multiply(size));
            Vector dir = origin.getDirection();
            for (int i = 0; i < amount; i++) {
                double distance = i * size / 2;
//                Location displayBlockLoc = origin.clone().add(dir.clone().multiply(distance));
                TempDisplayBlock displayBlock = new TempDisplayBlock(origin, DisplayBlock.WATER, duration, size);

                tendrils.put(displayBlock, distance);
                end = displayBlock;
            }

            abilityStatus = AbilityStatus.MOVING;
            start();
        }
    }

    public Set<TempDisplayBlock> getTendrils() {
        return tendrils.keySet();
    }

    public void clearTendrilMap() {
        tendrils = new HashMap<>();
    }

    public TempDisplayBlock getEnd() {
        return end;
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (System.currentTimeMillis() - duration > startTime) {
            abilityStatus = AbilityStatus.COMPLETE;
        }

        switch (abilityStatus) {

            case MOVING -> {
                Location newPlayerLoc = player.getEyeLocation();
                Vector diff = Vectors.getDirectionBetweenLocations(oldPlayerLoc, newPlayerLoc);
                tendrils.keySet().forEach(tempDisplayBlock -> tempDisplayBlock.moveTo(tempDisplayBlock.getLoc().add(diff)));
            }
            // For grab
            case GRAB -> {
                if (grabDistance > range) {
                    endGrab();
                }
                if (grabTarget == null) {
                    grabDistance += speed;

                    Location origin = player.getEyeLocation().subtract(player.getEyeLocation().getDirection().multiply(size));

                    tendrils.forEach((tempDisplayBlock, distance) -> {

                        Location displayBlockLoc = origin.clone().add(dir.clone().multiply(distance / length * grabDistance));

//                    Vector toMove = Vectors.getDirectionBetweenLocations(origin.clone().add(player.getEyeLocation().getDirection().clone().multiply(distance)), displayBlockLoc);
                        Vector toMove = Vectors.getDirectionBetweenLocations(tempDisplayBlock.getLoc(), displayBlockLoc);

                        if (toMove.lengthSquared() > 1) {
                            toMove.normalize();
                            Location toMoveLocation = tempDisplayBlock.getLoc().clone().add(toMove);
                            tempDisplayBlock.moveTo(toMoveLocation);
                        } else {
                            tempDisplayBlock.moveTo(displayBlockLoc);
                        }
                    });

                } else {

                    Location origin = player.getEyeLocation().subtract(player.getEyeLocation().getDirection().multiply(size));

                    tendrils.forEach((tempDisplayBlock, distance) -> {

                        Location displayBlockLoc = origin.clone().add(player.getEyeLocation().getDirection().clone().multiply(distance));

                        Vector toMove = Vectors.getDirectionBetweenLocations(tempDisplayBlock.getLoc(), displayBlockLoc);

                        if (toMove.lengthSquared() > 1) {
                            toMove.normalize();
                            Location toMoveLocation = tempDisplayBlock.getLoc().clone().add(toMove);
                            tempDisplayBlock.moveTo(toMoveLocation);
                        } else {
                            tempDisplayBlock.moveTo(displayBlockLoc);
                        }
                    });
                }


                if (grabTarget == null && Entities.getAffected(end.getLoc(), size * 2, player) != null) {
                    grabTarget = Entities.getAffected(end.getLoc(), size * 2, player);
                }
                if (grabTarget != null) {
                    net.minecraft.world.entity.Entity entity = ((CraftEntity) grabTarget).getHandle();
                    entity.moveTo(new Vec3(end.getLoc().toVector().toVector3f()));
//                    grabTarget.setVelocity(Vectors.getDirectionBetweenLocations(end.getLoc(), grabTarget.getLocation()).normalize());
                }
            }

            case GRAPPLE -> {
                if (grappleDistance > range) {
                    abilityStatus = AbilityStatus.MOVING;

                }
                if (grappleBlock == null) {
                    grappleDistance += speed;


                    Location origin = player.getEyeLocation().subtract(player.getEyeLocation().getDirection().multiply(size));

                    tendrils.forEach((tempDisplayBlock, distance) -> {

                        Location displayBlockLoc = origin.clone().add(dir.clone().multiply(distance / length * grappleDistance));

//                    Vector toMove = Vectors.getDirectionBetweenLocations(origin.clone().add(player.getEyeLocation().getDirection().clone().multiply(distance)), displayBlockLoc);
                        Vector toMove = Vectors.getDirectionBetweenLocations(tempDisplayBlock.getLoc(), displayBlockLoc);

                        if (toMove.lengthSquared() > 1) {
                            toMove.normalize();
                            Location toMoveLocation = tempDisplayBlock.getLoc().clone().add(toMove);
                            tempDisplayBlock.moveTo(toMoveLocation);
                        } else {
                            tempDisplayBlock.moveTo(displayBlockLoc);
                        }
                    });
                } else {
                    Location origin = player.getEyeLocation().subtract(player.getEyeLocation().getDirection().multiply(size));

                    tendrils.forEach((tempDisplayBlock, distance) -> {

                        Vector dir = Vectors.getDirectionBetweenLocations(player.getEyeLocation(), grappleBlock.getLocation());
                        Location displayBlockLoc = origin.clone().add(dir.clone().multiply(distance / length * dir.length()));

//                    Vector toMove = Vectors.getDirectionBetweenLocations(origin.clone().add(player.getEyeLocation().getDirection().clone().multiply(distance)), displayBlockLoc);
                        Vector toMove = Vectors.getDirectionBetweenLocations(tempDisplayBlock.getLoc(), displayBlockLoc);

                        if (toMove.lengthSquared() > 1) {
                            toMove.normalize();
                            Location toMoveLocation = tempDisplayBlock.getLoc().clone().add(toMove);
                            tempDisplayBlock.moveTo(toMoveLocation);
                        } else {
                            tempDisplayBlock.moveTo(displayBlockLoc);
                        }
                    });
                }

                Block targetBlock = Blocks.getFacingBlock(player.getEyeLocation(), dir, grappleDistance);

                if (grappleBlock == null && targetBlock != null && (!targetBlock.isLiquid())) {
                    grappleBlock = targetBlock;
                }

                if (grappleBlock != null) {
                    player.setVelocity(Vectors.getDirectionBetweenLocations(player.getEyeLocation(), grappleBlock.getLocation()).add(new Vector(0, 2, 0)).normalize().multiply(speed));
                    if ((System.currentTimeMillis() - sinceLastGrappled > 1000 || player.getVelocity().lengthSquared() < 0.5) || player.getEyeLocation().distanceSquared(grappleBlock.getLocation()) < 9) {
                        endGrapple();
                    }
                }
            }

        }
        oldPlayerLoc = player.getEyeLocation();

    }

    public Entity getGrabTarget() {
        return grabTarget;
    }

    public void endGrab() {
        abilityStatus = AbilityStatus.MOVING;
        grabTarget = null;
    }

    public void endGrapple() {
        abilityStatus = AbilityStatus.MOVING;
        grappleBlock = null;
    }

    public void setTendrilBlock(DisplayBlock block) {
        tendrils.keySet().forEach(tempDisplayBlock -> {
            Material type = block.getBlocks().get(random.nextInt(block.getBlocks().size()));

            tempDisplayBlock.getBlockDisplay().setBlock(type.createBlockData());
        });
    }

    public void animateMovement(Vector dir, Location origin) {

        tendrils.forEach((tempDisplayBlock, distance) -> {

            Location displayBlockLoc = origin.clone().add(dir.clone().multiply(distance));

            Vector toMove = Vectors.getDirectionBetweenLocations(tempDisplayBlock.getLoc(), displayBlockLoc);


            if (toMove.lengthSquared() > 1) {
                toMove.normalize();
                Location toMoveLocation = tempDisplayBlock.getLoc().clone().add(toMove);
                tempDisplayBlock.moveTo(toMoveLocation);
            } else {
                tempDisplayBlock.moveTo(displayBlockLoc);
            }
        });
    }


    public void animateMovement(Vector dir) {

        Location origin = player.getEyeLocation().subtract(player.getEyeLocation().getDirection().multiply(size));

        if (dir.distanceSquared(Vectors.getRightSideNormalisedVector(player)) < dir.distanceSquared(Vectors.getLeftSideNormalisedVector(player))) {
            origin.add(Vectors.getRightSideNormalisedVector(player).multiply(0.5));
        } else {
            origin.add(Vectors.getLeftSideNormalisedVector(player).multiply(0.5));
        }
        origin.subtract(0, 0.5, 0);


        tendrils.forEach((tempDisplayBlock, distance) -> {

            Location displayBlockLoc = origin.clone().add(dir.clone().multiply(distance));

            Vector toMove = Vectors.getDirectionBetweenLocations(tempDisplayBlock.getLoc(), displayBlockLoc);


            if (toMove.lengthSquared() > 1) {
                toMove.normalize();
                Location toMoveLocation = tempDisplayBlock.getLoc().clone().add(toMove);
                tempDisplayBlock.moveTo(toMoveLocation);
            } else {
                tempDisplayBlock.moveTo(displayBlockLoc);
            }
        });
    }


    public void animateCurveTowardsPlayerDir(Vector dir) {

        Location origin = player.getEyeLocation().subtract(player.getEyeLocation().getDirection().multiply(size));

        Vector playerDir = player.getEyeLocation().getDirection();

        if (dir.distanceSquared(Vectors.getRightSideNormalisedVector(player)) < dir.distanceSquared(Vectors.getLeftSideNormalisedVector(player))) {
            origin.add(Vectors.getRightSideNormalisedVector(player).multiply(0.5));
        } else {
            origin.add(Vectors.getLeftSideNormalisedVector(player).multiply(0.5));
        }
        origin.subtract(0, 0.5, 0);


        tendrils.forEach((tempDisplayBlock, distance) -> {
            Location displayBlockLoc = origin.clone().add(dir.clone().multiply(distance).add(playerDir.clone().multiply(Math.log(Math.max(1, distance)) * Math.log(Math.max(1, distance)) * Math.sqrt(distance))));

            Vector toMove = Vectors.getDirectionBetweenLocations(tempDisplayBlock.getLoc(), displayBlockLoc);

            if (toMove.lengthSquared() > 1) {

                toMove.normalize();
                Location toMoveLocation = tempDisplayBlock.getLoc().clone().add(toMove);
                tempDisplayBlock.moveTo(toMoveLocation);
            } else {
                tempDisplayBlock.moveTo(displayBlockLoc);
            }
        });
    }


    public void grab(Vector dir) {
        abilityStatus = AbilityStatus.GRAB;
        this.dir = dir.clone();
//        distance = tendrils.size() * size;
        grabDistance = 0;
    }

    public void grapple(Vector dir) {
        sinceLastGrappled = System.currentTimeMillis();
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
