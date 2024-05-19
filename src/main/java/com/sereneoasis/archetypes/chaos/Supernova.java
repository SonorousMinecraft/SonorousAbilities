package com.sereneoasis.archetypes.chaos;

import com.sereneoasis.Serenity;
import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.abilityuilities.blocks.*;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.Laser;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Supernova extends MasterAbility {

    private static final String name = "Supernova";

    private ShootBlockFromLoc shootBlockFromLoc;
    
    private Location loc, origin;
    
    private double currentRadius = 0;

    private BlockDisintegrateSphereSuck blockDisintegrateSphereSuck;

    private Random random = new Random();

    private Set<BlockRingAroundPoint>rings = new HashSet<>();

    private HashMap<Laser.CrystalLaser, Block>crystalLasers = new HashMap<>();

    private Vector dir;
    private boolean hasShot = false, hasHit = false;

    public Supernova(Player player) {
        super(player, name);

        if (shouldStart()) {

            shootBlockFromLoc = new ShootBlockFromLoc(player, name, player.getEyeLocation(), Material.BLACK_CONCRETE, false, false);
            BlockAbilities.handleGravityShootBlockFromLoc(this, shootBlockFromLoc);

            abilityStatus = AbilityStatus.SHOT;
            start();

        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        iterateHelpers(abilityStatus);
        
        if (abilityStatus == AbilityStatus.SHOT) {
            if (shootBlockFromLoc.getAbilityStatus() == AbilityStatus.HIT_SOLID) {
                abilityStatus = AbilityStatus.HIT_SOLID;
                this.loc = shootBlockFromLoc.getLoc().clone();
                blockDisintegrateSphereSuck = new BlockDisintegrateSphereSuck(player, name, loc, loc, 0, 1);
                
            } else if (shootBlockFromLoc.getAbilityStatus() == AbilityStatus.DAMAGED) {
                abilityStatus = AbilityStatus.DAMAGED;
                this.loc = shootBlockFromLoc.getLoc().clone();
                blockDisintegrateSphereSuck = new BlockDisintegrateSphereSuck(player, name, loc, loc, 0, 1);

            }

        } else if (abilityStatus == AbilityStatus.HIT_SOLID || abilityStatus == AbilityStatus.DAMAGED){
            suckBlocks();

                blockDisintegrateSphereSuck.getSourceBlocksToLoc().forEach(sourceBlockToLoc -> {
                    if (sourceBlockToLoc.getSourceStatus() == AbilityStatus.SOURCED) {

                        if (rings.size() < 100) {

                            BlockRingAroundPoint blockRingAroundPoint = new BlockRingAroundPoint(player, name, loc, sourceBlockToLoc.getType(),
                                    radius/4, (int) Math.round(Math.random() * 360), 10, random.nextBoolean());
                            rings.add(blockRingAroundPoint);
                        }
                        sourceBlockToLoc.remove();
                    }
                });


        } else if (abilityStatus == AbilityStatus.SOURCED){
            crystalLasers.forEach((crystalLaser, block) -> {
                try {
                    crystalLaser.moveEnd(loc);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });

            rings.forEach(blockRingAroundPoint -> blockRingAroundPoint.setLoc(loc));

            Particles.spawnParticle(Particle.SONIC_BOOM, loc, 20, radius / 16, 0);

            if (!hasShot) {

                Location playerFront = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(radius / 2));

                if (player.isSneaking()) {
                    loc.add(Vectors.getDirectionBetweenLocations(loc, playerFront).normalize().multiply(speed));
                }

//                if (loc.distance(origin) > 50) {
//                this.remove();
//            }
            } else {
                if (!hasHit) {
                    loc.add(dir.clone().multiply(speed));
                    if (loc.distance(origin) > range) {
                        this.remove();
                    }
                    if (Blocks.getBlocksAroundPoint(loc, radius / 4).stream().anyMatch(b -> (b != null && !b.isPassable()))) {
                        hasHit = true;
                    }
                } else {
//                    rings.forEach(blockRingAroundPoint -> blockRingAroundPoint.setRingSize(blockRingAroundPoint.getRingSize() +1));
//                    if (rings.stream().anyMatch(blockRingAroundPoint -> blockRingAroundPoint.getRingSize() > radius)){
//                        this.remove();
//                    }
                    Entities.getEntitiesAroundPoint(loc, radius).forEach(entity -> DamageHandler.damageEntity(entity, player, this, damage));
                    Particles.spawnParticle(Particle.SONIC_BOOM, loc, 20, radius , 0);

                    new BlockDisintegrateSphere(player, name, loc, 0, 1);
                    this.remove();
                }
            }
        }
    }

    public void setHasClicked(){
        if (abilityStatus == AbilityStatus.SOURCED){
            if (!hasShot) {
                hasShot = true;
                this.dir = player.getEyeLocation().getDirection();
                this.origin = loc.clone();
            } else {
                this.dir = player.getEyeLocation().getDirection();
                if (player.isSneaking()) {
                    crystalLasers.forEach((crystalLaser, block) -> {
                        try {
                            Location newLoc = Blocks.getFacingBlockLoc(player, range);
                            if (newLoc != null) {
                                newLoc.add(Vectors.getRandom().multiply(radius));
                                new BlockDisintegrateSphereSuck(player, name, newLoc, loc, 0, 1);
                                crystalLaser.moveStart(newLoc);
                            }
                        } catch (ReflectiveOperationException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        }
    }

    private void suckBlocks(){
        
        if (currentRadius < radius){
            currentRadius++;
        } else {
            abilityStatus = AbilityStatus.SOURCED;
            //rings.forEach(BlockRingAroundPoint::remove);
            blockDisintegrateSphereSuck.getSourceBlocksToLoc().forEach(SourceBlockToLoc::remove);


            Location center = loc.clone().add(0,radius,0);
            Set<Block> inner = Blocks.getBlocksAroundPoint(center, radius - 1);
            Set<Block> outer = Blocks.getBlocksAroundPoint(center, radius );
            outer.removeAll(inner);

            Object[] outerArray =  outer.toArray();
            for (int i = 0 ; i < 10; i ++ ) {
                Block b = (Block) outerArray[new Random().nextInt(outer.size())];
                try {
                    Laser.CrystalLaser crystalLaser = new Laser.CrystalLaser(b.getLocation(), center, -1, 100);
                    crystalLaser.start(Serenity.getPlugin());
                    crystalLasers.put(crystalLaser, b);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }

            }
            this.origin = loc.clone();
        }



    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void remove() {
        super.remove();
        crystalLasers.keySet().forEach(Laser::stop);

        rings.forEach(BlockRingAroundPoint::remove);

    }
}
