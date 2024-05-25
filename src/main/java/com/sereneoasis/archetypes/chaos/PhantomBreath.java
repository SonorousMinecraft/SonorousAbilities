package com.sereneoasis.archetypes.chaos;

import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.abilityuilities.blocks.ShootBlockFromLoc;
import com.sereneoasis.abilityuilities.blocks.SourceBlockToPlayer;
import com.sereneoasis.abilityuilities.particles.Breath;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempBlock;
import org.bukkit.Bukkit;
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

    private Set<Block> sourceBlocks = new HashSet<>();
    private Set<TempBlock> sourceTempBlocks = new HashSet<>();

    private Random random = new Random();

    private int currentShots = 0, shots = 100;
    public PhantomBreath(Player player) {
        super(player, name);

        if (shouldStart())
        {
            Breath breath = new Breath(player, name, Particle.SCULK_SOUL);
            helpers.put(breath, (abilityStatus) -> {
                switch (abilityStatus){
                    case SHOT -> {
                        if (breath.getAbilityStatus() == AbilityStatus.COMPLETE) {
                            breath.remove();
                        }
                        breath.getLocs().forEach(location -> {
                            Block b = location.getBlock();
                            if (!b.isPassable() && !sourceBlocks.contains(b)) {
                                TempBlock tb = new TempBlock(b, Material.BLACK_CONCRETE, duration, true);
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
        if (currentShots == shots || !player.isSneaking()){
            iterateHelpers(AbilityStatus.COMPLETE);
            this.remove();
        }
    }

    public void setHasClicked() {
        if (abilityStatus != AbilityStatus.SOURCING) {
            abilityStatus = AbilityStatus.SOURCING;

            sourceBlocks.forEach(block -> {
                SourceBlockToPlayer sourceBlockToPlayer = new SourceBlockToPlayer(player, name, 4, 1, block );
                sourceBlockToPlayer.setAbilityStatus(AbilityStatus.SOURCING);

                helpers.put(sourceBlockToPlayer, (abilityStatus) -> {
                    switch (abilityStatus){
                        case SOURCING -> {
                            if (sourceBlockToPlayer.getSourceStatus() == AbilityStatus.SOURCED) {
                                if (currentShots < shots && random.nextDouble() < (double) (shots - currentShots) /(shots) ) {
                                    Vector randomiser = Vectors.getRightSide(player, random.nextDouble()-0.5).add(new Vector(0, random.nextDouble() - 0.5, 0).rotateAroundAxis(Vectors.getRightSideNormalisedVector(player), Math.toRadians(-player.getEyeLocation().getPitch())));
                                    ShootBlockFromLoc shootBlockFromLoc = new ShootBlockFromLoc(player, name, player.getEyeLocation().add(randomiser.multiply(range)), Material.BLACK_CONCRETE, true, true);
                                    shootBlockFromLoc.setRange(range*4);
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
                if (TempBlock.isTempBlock(block)){
                    TempBlock.getTempBlock(block).revert();
                    TempBlock tb = new TempBlock(block.getLocation().getBlock(), Material.AIR, duration, true);
                    sourceTempBlocks.add(tb);
                } else {
                    TempBlock tb = new TempBlock(block, Material.AIR, duration, true);
                    sourceTempBlocks.add(tb);
                }

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
