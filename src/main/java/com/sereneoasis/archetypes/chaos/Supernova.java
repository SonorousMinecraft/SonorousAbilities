package com.sereneoasis.archetypes.chaos;

import com.sereneoasis.Serenity;
import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.abilityuilities.blocks.BlockAbilities;
import com.sereneoasis.abilityuilities.blocks.BlockRingAroundPoint;
import com.sereneoasis.abilityuilities.blocks.ShootBlockFromLoc;
import com.sereneoasis.abilityuilities.blocks.SourceBlockToLoc;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.Laser;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Bukkit;
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

    private HashMap<TempDisplayBlock, Vector> displayBlocks = new HashMap<>();
    private Set<TempBlock> sourceTempBlocks = new HashSet<>();

    private Set<SourceBlockToLoc> sourceBlocksToLoc = new HashSet<>();


    private Set<Block> previousSourceBlocks = new HashSet<>();

    private Random random = new Random();

    private Set<BlockRingAroundPoint>rings = new HashSet<>();

    private HashMap<Laser.CrystalLaser, Block>crystalLasers = new HashMap<>();

    private Vector dir;
    private boolean hasShot = false;

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
                
            } else if (shootBlockFromLoc.getAbilityStatus() == AbilityStatus.DAMAGED) {
                abilityStatus = AbilityStatus.DAMAGED;
                this.loc = shootBlockFromLoc.getLoc().clone();
            }
        } else if (abilityStatus == AbilityStatus.HIT_SOLID || abilityStatus == AbilityStatus.DAMAGED){
            suckBlocks();

                sourceBlocksToLoc.forEach(sourceBlockToLoc -> {
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
                loc.add(dir.clone().multiply(speed));
                if (loc.distance(origin) > range){
                    this.remove();
                }
            }
        }
    }

    public void setHasClicked(){
        if (abilityStatus == AbilityStatus.SOURCED && !hasShot){
            hasShot = true;
            this.dir = player.getEyeLocation().getDirection();
            this.origin = loc.clone();
        }
    }

    private void suckBlocks(){
        
        if (currentRadius < radius){
            currentRadius++;
        } else {
            abilityStatus = AbilityStatus.SOURCED;
            //rings.forEach(BlockRingAroundPoint::remove);
            sourceBlocksToLoc.forEach(SourceBlockToLoc::remove);
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
        Set<Block> sourceBlocks = Blocks.getBlocksAroundPoint(loc, currentRadius);
        sourceBlocks.removeIf(block -> previousSourceBlocks.contains(block));
        for (Block b : sourceBlocks) {
            if (b != null && !b.isPassable() && b.getType() != Material.CRYING_OBSIDIAN) {
                if (random.nextDouble() < 0.1) {
                    SourceBlockToLoc sourceBlockToLoc = new SourceBlockToLoc(player, name, 4, 1, b, loc);
                    sourceBlockToLoc.setAbilityStatus(AbilityStatus.SOURCING);
                    sourceBlocksToLoc.add(sourceBlockToLoc);
                }
                
//                TempDisplayBlock tdb = new TempDisplayBlock(b, b.getType(), 60000, 2);
//                Vector offset = Vectors.getDirectionBetweenLocations(loc, b.getLocation());
//                displayBlocks.put(tdb, offset);
                
                if (TempBlock.isTempBlock(b) && !sourceTempBlocks.contains(TempBlock.getTempBlock(b))) {
                    TempBlock.getTempBlock(b).revert();
                }
                TempBlock tb = new TempBlock(b, Material.AIR, 60000, true);
                sourceTempBlocks.add(tb);
            }
        }
        this.previousSourceBlocks.addAll(sourceBlocks);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void remove() {
        super.remove();
        crystalLasers.keySet().forEach(Laser::stop);

        sourceBlocksToLoc.forEach(SourceBlockToLoc::remove);

    }
}
