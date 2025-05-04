package com.sonorous.abilityuilities.blocks;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.Blocks;
import com.sonorous.util.methods.Particles;
import com.sonorous.util.methods.RandomUtils;
import com.sonorous.util.temp.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Causes a spherical shaped blast to be shot from the player
 */
public class BlockDisintegrateSphereSuck extends CoreAbility {

    private final Location centerLoc;
    private final Location targetLoc;

    private final String name;

    private double currentRadius;
    private final double increment;


    private final Set<TempBlock> sourceTempBlocks = new HashSet<>();

    private final Set<SourceBlockToLoc> sourceBlocksToLoc = new HashSet<>();
    private final Set<Block> previousSourceBlocks = new HashSet<>();

    public BlockDisintegrateSphereSuck(Player player, String name, Location startLoc, Location targetLoc, double currentRadius, double increment) {
        super(player, name);

        this.name = name;

        this.centerLoc = startLoc.clone();
        this.targetLoc = targetLoc;
        this.currentRadius = currentRadius;
        this.increment = increment;
        start();
    }


    public BlockDisintegrateSphereSuck(Player player, String name, Location startLoc, Location targetLoc, double currentRadius, double endRadius, double increment) {
        super(player, name);

        this.name = name;

        this.centerLoc = startLoc.clone();
        this.targetLoc = targetLoc;
        this.currentRadius = currentRadius;
        this.radius = endRadius;
        this.increment = increment;
        start();
    }

    public Set<SourceBlockToLoc> getSourceBlocksToLoc() {
        return sourceBlocksToLoc;
    }

    @Override
    public void progress() {
        currentRadius += increment;
        Particles.spawnParticle(Particle.SONIC_BOOM, centerLoc, 20, currentRadius / 2, 0);

        Set<Block> sourceBlocks = Blocks.getBlocksAroundPoint(centerLoc, currentRadius);

        sourceBlocks.removeIf(previousSourceBlocks::contains);

        for (Block b : sourceBlocks) {
            if (b != null && !b.isPassable()) {
                if (RandomUtils.getRandomDouble(0, 1) < 0.1) {
                    SourceBlockToLoc sourceBlockToLoc = new SourceBlockToLoc(player, name, 4, 1, b, targetLoc);
                    sourceBlockToLoc.setAbilityStatus(AbilityStatus.SOURCING);
                    sourceBlocksToLoc.add(sourceBlockToLoc);
                }

                TempBlock tb = new TempBlock(b, Material.AIR, 60000);
                sourceTempBlocks.add(tb);


            }
        }
        this.previousSourceBlocks.addAll(sourceBlocks);

        if (currentRadius > radius) {
            this.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();

        sourceBlocksToLoc.forEach(SourceBlockToLoc::remove);

    }

    @Override
    public String getName() {
        return name;
    }
}
