package com.sonorous.archetypes.chaos;

import com.sonorous.ability.superclasses.MasterAbility;
import com.sonorous.abilityuilities.blocks.ShootBlockFromLoc;
import com.sonorous.abilityuilities.blocks.SourceBlockToPlayer;
import com.sonorous.abilityuilities.particles.Breath;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.RandomUtils;
import com.sonorous.util.methods.Vectors;
import com.sonorous.util.temp.TempBlock;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PhantomBreath extends MasterAbility {

    private static final String name = "PhantomBreath";

    private final Set<Block> sourceBlocks = new HashSet<>();
    private final Set<TempBlock> sourceTempBlocks = new HashSet<>();


    private int currentShots = 0;
    private final int shots = 100;

    public PhantomBreath(Player player) {
        super(player, name);

        if (shouldStart()) {
            Breath breath = new Breath(player, name, Particle.SCULK_SOUL);
            helpers.put(breath, (abilityStatus) -> {
                switch (abilityStatus) {
                    case SHOT -> {
                        if (breath.getAbilityStatus() == AbilityStatus.COMPLETE) {
                            breath.remove();
                        }
                        breath.getLocs().forEach(location -> {
                            Block b = location.getBlock();
                            if (!b.isPassable() && !sourceBlocks.contains(b)) {
                                TempBlock tb = new TempBlock(b, Material.BLACK_CONCRETE, duration);
                                sourceTempBlocks.add(tb);
                                sourceBlocks.add(b);
                            }
                        });
                    }
                    case SOURCING -> {
                        breath.remove();
                    }
                    case COMPLETE -> {
                        breath.remove();
                    }
                }
            });
            abilityStatus = AbilityStatus.SHOT;
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        iterateHelpers(abilityStatus);
        if (currentShots == shots || !player.isSneaking()) {
            iterateHelpers(AbilityStatus.COMPLETE);
            this.remove();
        }
    }

    public void setHasClicked() {
        if (abilityStatus != AbilityStatus.SOURCING) {
            abilityStatus = AbilityStatus.SOURCING;

            sourceBlocks.forEach(block -> {
                SourceBlockToPlayer sourceBlockToPlayer = new SourceBlockToPlayer(player, name, 4, 1, block);
                sourceBlockToPlayer.setAbilityStatus(AbilityStatus.SOURCING);

                helpers.put(sourceBlockToPlayer, (abilityStatus) -> {
                    switch (abilityStatus) {
                        case SOURCING -> {
                            if (sourceBlockToPlayer.getSourceStatus() == AbilityStatus.SOURCED) {
                                if (currentShots < shots && RandomUtils.getRandomDouble(0,1) < (double) (shots - currentShots) / (shots)) {
                                    Vector randomiser = Vectors.getRightSide(player, RandomUtils.getRandomDouble(0,1) - 0.5).add(new Vector(0, RandomUtils.getRandomDouble(0,1) - 0.5, 0).rotateAroundAxis(Vectors.getRightSideNormalisedVector(player), Math.toRadians(-player.getEyeLocation().getPitch())));
                                    ShootBlockFromLoc shootBlockFromLoc = new ShootBlockFromLoc(player, name, player.getEyeLocation().add(randomiser.multiply(range)), Material.BLACK_CONCRETE, true, true);
                                    shootBlockFromLoc.setRange(range * 4);
                                    shootBlockFromLoc.setGlowing(org.bukkit.Color.PURPLE);
                                    currentShots++;
                                }
                                sourceBlockToPlayer.remove();
                            }
                        }
                        case COMPLETE -> {
                            sourceBlockToPlayer.remove();
                        }
                    }
                });
                TempBlock tb = new TempBlock(block, Material.AIR, duration);
                sourceTempBlocks.add(tb);

            });

        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
    }
}
