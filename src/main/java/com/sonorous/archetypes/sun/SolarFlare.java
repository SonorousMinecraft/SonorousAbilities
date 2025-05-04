package com.sonorous.archetypes.sun;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.archetypes.DisplayBlock;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.enhancedmethods.EnhancedBlocksArchetypeLess;
import com.sonorous.util.methods.*;
import com.sonorous.util.temp.TempBlock;
import com.sonorous.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Sakrajin
 */
public class SolarFlare extends CoreAbility {

    private static final String name = "SolarFlare";
    private long startTime = System.currentTimeMillis();
    private boolean started = false;
    private Block target;
    private Location flareLoc;
    private Set<TempDisplayBlock> flares;

    public SolarFlare(Player player) {
        super(player, name);

        if (shouldStart()) {
            abilityStatus = AbilityStatus.NO_SOURCE;
            target = Blocks.getFacingBlockOrLiquid(player, sourceRange);
            if (target != null && target.getType().isSolid()) {
                Location sourceLoc = Blocks.getFacingBlockOrLiquidLoc(player, sourceRange).subtract(0, size, 0);


//                Blocks.selectSourceAnimationShapeGivenType( Locations.getCircleLocsAroundPoint(sourceLoc.clone().add(0,speed,0), radius, size), sPlayer.getColor(), size, DisplayBlock.SUN);
                EnhancedBlocksArchetypeLess.getTopCircleBlocksFloor(this, sourceLoc.clone()).forEach(block -> {
                    new TempBlock(block, DisplayBlock.SUN, 20000);
                });

                double height = radius * 3;
                flareLoc = target.getLocation().clone().add(0, height, 0);

                Set<Location> locs = new HashSet<>();
                for (Location b : Locations.getOutsideSphereLocs(flareLoc, radius, size)) {
                    if ((int) b.getY() == flareLoc.getY()) {
                        locs.add(b);
                    }
                }
                flares = EnhancedBlocksArchetypeLess.getCircleAtYBlocks(this, flareLoc, flareLoc.getBlockY()).stream().map(block -> {
                    return new TempDisplayBlock(block, DisplayBlock.SUN, 60000, 1.0);
                }).collect(Collectors.toSet());


                start();
            }
            ;
        }


    }

    @Override
    public void progress() {

        if (!started && System.currentTimeMillis() > startTime + chargeTime) {
            started = true;
        }


        if (started) {

            flareLoc.subtract(0, speed, 0);
            flares.forEach(tempDisplayBlock -> tempDisplayBlock.moveTo(tempDisplayBlock.getLoc().subtract(0, RandomUtils.getRandomDouble(0, speed * 2), 0)));

            if (flareLoc.getY() <= target.getY()) {
                this.remove();
            }

            Set<Location> locs = new HashSet<>();
            for (Location b : Locations.getOutsideSphereLocs(flareLoc, radius, size)) {
                if ((int) b.getY() == flareLoc.getY()) {
                    locs.add(b);
                }
            }

            for (Location particleLoc : Locations.getCircleLocsAroundPoint(flareLoc, radius - size, size)) {
                Particles.spawnParticle(Particle.WAX_ON, particleLoc, 1, size, 0);
                AbilityDamage.damageSeveral(particleLoc, this, player, true, new Vector(0, 5, 0));
            }

        }
    }

    @Override
    public void remove() {
        super.remove();
        flares.forEach(tempDisplayBlock -> tempDisplayBlock.revert());
        sPlayer.addCooldown("SolarFlare", cooldown);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return "SolarFlare";
    }
}