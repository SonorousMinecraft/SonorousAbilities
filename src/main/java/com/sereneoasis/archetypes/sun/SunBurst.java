package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.ShootBlockShapeFromLoc;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.enhancedmethods.EnhancedBlocksArchetypeLess;
import com.sereneoasis.util.enhancedmethods.EnhancedDisplayBlocks;
import com.sereneoasis.util.enhancedmethods.EnhancedSchedulerEffects;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.stream.Collectors;

public class SunBurst extends CoreAbility {

    private static final String name = "SunBurst";

    private HashMap<Integer, TempDisplayBlock> explosion = new HashMap<>();

    private double currentRadius = 0;

    private ShootBlockShapeFromLoc sun;

    public SunBurst(Player player) {
        super(player, name);

        if (shouldStart()) {
            abilityStatus = AbilityStatus.CHARGING;
            start();
        }
    }

    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.CHARGING) {
            if (!player.isSneaking()) {
                this.remove();
            }
            if (System.currentTimeMillis() > chargeTime + startTime) {
                abilityStatus = AbilityStatus.CHARGED;
                Particles.spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 10, 0.5, 0);

                EnhancedSchedulerEffects.raiseTDBs(EnhancedDisplayBlocks.createTopCircleTempBlocks(this, DisplayBlock.SUN), 50, 1);

                player.setVelocity(new Vector(0, 3, 0));
            }
        }

        if (abilityStatus == AbilityStatus.CHARGED) {
            currentRadius += speed;
            if (currentRadius < radius) {
                explosion = Entities.handleDisplayBlockEntities(explosion,
//                        Locations.getOutsideSphereLocs(player.getLocation(), currentRadius, size).stream().map(location -> location.add(Vectors.getRandom())).collect(Collectors.toSet()),
                        Locations.getOutsideSphereLocs(player.getLocation(), currentRadius, size),
                        DisplayBlock.SUN, size);

//                for (Block b : Blocks.getBlocksAroundPoint(player.getLocation(), radius)) {
//                    Block topBlock = b.getLocation().add(0, 1, 0).getBlock();
//                    if (topBlock.getType().isAir()) {
//                        new TempBlock(topBlock, DisplayBlock.FIRE, 2000, true);
//                    }
//                }
            } else {
                abilityStatus = AbilityStatus.SHOT;
                for (TempDisplayBlock tb : explosion.values()) {
                    tb.getBlockDisplay().setGlowColorOverride(Color.YELLOW);
                    tb.getBlockDisplay().setGlowing(true);
                }
                sun = new ShootBlockShapeFromLoc(player, name, player.getEyeLocation(), explosion.values().stream().collect(Collectors.toSet()), radius, false, player.getEyeLocation().getDirection());

            }
        }
        if (abilityStatus == AbilityStatus.SHOT) {
            if (sun.getAbilityStatus() == AbilityStatus.DAMAGED || sun.getAbilityStatus() == AbilityStatus.COMPLETE || sun.getAbilityStatus() == AbilityStatus.HIT_SOLID) {

                Location facing = Locations.getFacingLocation(sun.getLoc().clone(), sun.getDir().clone(), radius);

                if (!EnhancedBlocksArchetypeLess.getFacingSphereBlocks(this, facing).isEmpty()) {
//                    new BlockExplodeSphere(player, name, facing.add(sun.getDir().multiply(radius * speed)), radius, 1);

                    SunUtils.blockExplode(player, name, facing.add(sun.getDir().multiply(radius * speed)), radius, 1);
                }
                sun.remove();
                this.remove();
            }
        }
    }


    @Override
    public void remove() {
        super.remove();
//        EnhancedBlocksArchetypeLess.getTopCircleBlocks(this).forEach(block -> {
//            new TempBlock(block, Material.MAGMA_BLOCK, 60000, true);
//        });

//        Scheduler.performTaskLater(20, () -> new BlockExplodeSphere(player, name, player.getLocation(), 1));


//        EnhancedBlocksArchetypeLess.getTopCircleBlocks(this).forEach(block -> {
//            new TempBlock(block, Material.MAGMA_BLOCK, 60000, true);
//        });


//            ShootBlockFromLoc shootBlockFromLoc = new ShootBlockFromLoc(player, name, tb.getBlockDisplay().getLocation(), tb.getBlockDisplay().getBlock().getMaterial(), false, true);
//            shootBlockFromLoc.setGlowing(Color.YELLOW);
//            Particles.spawnParticle(Particle.EXPLOSION_LARGE,  tb.getBlockDisplay().getLocation(), 1, 0, 1);
//            tb.revert();


        sPlayer.addCooldown(name, cooldown);
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