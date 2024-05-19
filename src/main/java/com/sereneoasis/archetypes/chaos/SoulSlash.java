package com.sereneoasis.archetypes.chaos;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.abilityuilities.blocks.BlockRingAroundPlayer;
import com.sereneoasis.abilityuilities.blocks.ShootBlockFromLoc;
import com.sereneoasis.abilityuilities.blocks.SourceBlockToPlayer;
import com.sereneoasis.abilityuilities.particles.Sweep;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.temp.TempBlock;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class SoulSlash extends MasterAbility {

    private static final String name = "SoulSlash";

    private Set<TempBlock> sourceTempBlocks = new HashSet<>();

    private Set<SourceBlockToPlayer> sourceBlocksToPlayer = new HashSet<>();

    private Set<BlockRingAroundPlayer>rings = new HashSet<>();

    private Random random = new Random();

    public SoulSlash(Player player) {
        super(player, name);

        if (shouldStart()){

            Location forwardLoc = player.getEyeLocation().clone().add(player.getEyeLocation().getDirection().clone().normalize().multiply(0.5));
            Sweep sweep = new Sweep(player, name, new ArchetypeVisuals.ChaosVisual(), forwardLoc.clone().subtract(0, 0.3, 0), forwardLoc.clone().add(0, 0.3, 0));
            getHelpers().put(sweep, (abilityStatus) -> {
                switch (abilityStatus){
                    case SHOT -> {
                        if (sweep.getAbilityStatus() == AbilityStatus.COMPLETE) {
                            sweep.remove();
                            if (sourceBlocksToPlayer.isEmpty()){
                                this.remove();
                            }
                        } else {
                            Set<Block> sourceBlocks = sweep.getLocations().stream().map(Location::getBlock)
                                    .filter(block -> !block.isPassable()).collect(Collectors.toSet());

                            for (Block b : sourceBlocks) {
                                if (b != null && !b.isPassable()) {

                                    SourceBlockToPlayer sourceBlockToPlayer = new SourceBlockToPlayer(player, name, 4, 1, b);
                                    sourceBlockToPlayer.setAbilityStatus(AbilityStatus.SOURCING);
                                    sourceBlocksToPlayer.add(sourceBlockToPlayer);

                                    TempBlock tb = new TempBlock(b, Material.AIR, 60000, true);
                                    for (Block surrounding : Blocks.getBlocksAroundPoint(b.getLocation(), 2)) {
                                        if (surrounding != null && !surrounding.isPassable()) {
                                            TempBlock surroundingTB = new TempBlock(surrounding, Material.AIR, 60000, true);
                                            sourceTempBlocks.add(surroundingTB);

                                        }
                                    }
                                    sourceTempBlocks.add(tb);
                                }
                            }
                        }

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
        sourceBlocksToPlayer.forEach(sourceBlockToPlayer -> {
            if (sourceBlockToPlayer.getSourceStatus() == AbilityStatus.SOURCED) {
                sourceBlockToPlayer.remove();
                if (rings.size() < 20) {
                    BlockRingAroundPlayer blockRingAroundPlayer = new BlockRingAroundPlayer(player, name, sourceBlockToPlayer.getLocation(), sourceBlockToPlayer.getType(),
                            radius, (int) Math.round(Math.random() * 360), 10, random.nextBoolean());
                    rings.add(blockRingAroundPlayer);
                }
            }
        });

        if (player.isSneaking() && !rings.isEmpty()){
            for (BlockRingAroundPlayer ring : rings){
                ShootBlockFromLoc shootBlockFromLoc = new ShootBlockFromLoc(player, name, ring.getLocation(), ring.getType(), false, true);
                shootBlockFromLoc.setGlowing(Color.PURPLE);
                ring.remove();
                Particles.spawnParticle(Particle.SONIC_BOOM, ring.getLocation(), 1, 0, 1);
            }
            this.remove();
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

        sourceTempBlocks.forEach(TempBlock::automaticRevert);
        rings.forEach(BlockRingAroundPlayer::remove);
        sourceBlocksToPlayer.forEach(SourceBlockToPlayer::remove);
    }
}
