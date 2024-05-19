package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.temp.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Sakrajin
 * Causes a spherical shaped blast to be shot from the player
 */
public class BlockDisintegrateSphereSuck extends CoreAbility {

    private Location centerLoc, targetLoc;

    private String name;

    private double currentRadius, increment;


    private Set<TempBlock> sourceTempBlocks = new HashSet<>();

    private Set<SourceBlockToLoc> sourceBlocksToLoc = new HashSet<>();

    public Set<SourceBlockToLoc> getSourceBlocksToLoc() {
        return sourceBlocksToLoc;
    }

    private Set<Block> previousSourceBlocks = new HashSet<>();

    private Random random = new Random();


    public BlockDisintegrateSphereSuck(Player player, String name, Location startLoc, Location targetLoc, double currentRadius, double increment) {
        super(player, name);

        this.name = name;

        this.centerLoc = startLoc.clone();
        this.targetLoc = targetLoc;
        this.currentRadius = currentRadius;
        this.increment = increment;
        start();
    }


    @Override
    public void progress() {
        currentRadius += increment;
        Particles.spawnParticle(Particle.SONIC_BOOM, centerLoc, 20, currentRadius/2 , 0);

        Set<Block> sourceBlocks = Blocks.getBlocksAroundPoint(centerLoc, currentRadius);

        sourceBlocks.removeIf(block -> previousSourceBlocks.contains(block));

        for (Block b : sourceBlocks) {
            if (b != null && !b.isPassable() ) {
                if (random.nextDouble() < 0.1) {
                    SourceBlockToLoc sourceBlockToLoc = new SourceBlockToLoc(player, name, 4, 1, b, targetLoc);
                    sourceBlockToLoc.setAbilityStatus(AbilityStatus.SOURCING);
                    sourceBlocksToLoc.add(sourceBlockToLoc);
                }

                if (TempBlock.isTempBlock(b) && !sourceTempBlocks.contains(TempBlock.getTempBlock(b))) {
                    TempBlock.getTempBlock(b).revert();
                }

                TempBlock tb = new TempBlock(b, Material.LIGHT, 60000, true);
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
