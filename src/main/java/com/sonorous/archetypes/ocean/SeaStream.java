package com.sonorous.archetypes.ocean;

import com.sonorous.ability.superclasses.MasterAbility;
import com.sonorous.abilityuilities.blocks.ShootBlockShapeFromLoc;
import com.sonorous.abilityuilities.blocks.forcetype.ShootBlocksFromLocGivenType;
import com.sonorous.archetypes.DisplayBlock;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.enhancedmethods.EnhancedBlocks;
import com.sonorous.util.methods.Blocks;
import com.sonorous.util.methods.Entities;
import com.sonorous.util.methods.Vectors;
import com.sonorous.util.methods.collections.CollectionUtils;
import com.sonorous.util.temp.TempBlock;
import com.sonorous.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class SeaStream extends MasterAbility {

    public static final String name = "SeaStream";

    private Set<TempBlock> sources = new HashSet<>();

    private Set<TempDisplayBlock> stream = new HashSet<>();

    private ShootBlockShapeFromLoc streamShot;


    public SeaStream(Player player) {
        super(player, name);

        if (shouldStart()) {
            Block facing = Blocks.getFacingBlockOrLiquid(player, sourceRange);
            if (facing != null && Blocks.getArchetypeBlocks(sPlayer).contains(facing.getType())) {
                abilityStatus = AbilityStatus.CHARGING;
//            Bukkit.broadcastMessage("started");
                start();
            }

        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        switch (abilityStatus) {
            case CHARGING -> {
                if (System.currentTimeMillis() - startTime > chargeTime) {
                    sources.forEach(tempBlock -> {

//                        if (tempBlock!= null && tempBlock.getBlock() != null) {
                        if (tempBlock.getBlock() != null) {
                            tempBlock.revert();
                        }

                    });
                    abilityStatus = AbilityStatus.CHARGED;
//                    Bukkit.broadcastMessage("charged");

                } else {
                    if (!player.isSneaking()) {
//                        Bukkit.broadcastMessage("removed for not sneaking");
                        this.remove();
                    } else {
                        Block facing = Blocks.getFacingBlockOrLiquid(player, sourceRange);
                        if (facing == null || !Blocks.getArchetypeBlocks(sPlayer).contains(facing.getType())) {
//                            Bukkit.broadcastMessage("removed for no source");
                            this.remove();
                        } else {
//                            Bukkit.broadcastMessage("creating ice");
                            EnhancedBlocks.getFacingSphereLiquidBlocks(this).stream()
                                    .filter(block -> !TempBlock.isTempBlock(block))
                                    .forEach(block -> {
                                        TempBlock ice = new TempBlock(block, DisplayBlock.ICE, 60000);
                                        stream.add(new TempDisplayBlock(block, DisplayBlock.WATER, 60000, size));

                                        sources.add(ice);
                                    });
                        }
                    }
                }
            }
            case CHARGED -> {
//                Bukkit.broadcastMessage("charged state");
                if (player.isSneaking()) {

                    stream.forEach(tempDisplayBlock -> {
                        Location sourceTo = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(radius * 3));
                        if (tempDisplayBlock.getLoc().distanceSquared(sourceTo) > radius * radius) {
                            tempDisplayBlock.moveTo(tempDisplayBlock.getLoc().add(Vectors.getDirectionBetweenLocations(tempDisplayBlock.getLoc(), sourceTo.clone().add(Vectors.getRandom().multiply(radius * 3))).normalize()));
                        }
                    });
                }
            }
            case SHOT -> {

//                Bukkit.broadcastMessage("stream has " + stream.size());
//                this.remove();
                stream.forEach(tempDisplayBlock -> {
                    tempDisplayBlock.moveTo(tempDisplayBlock.getLoc().add(Vectors.getDirectionBetweenLocations(tempDisplayBlock.getLoc(), streamShot.getLoc().clone().add(Vectors.getRandom().multiply(radius))).normalize()));
                });
                Entities.getEntitiesAroundPoint(streamShot.getLoc(), radius).forEach(entity -> entity.setVelocity(streamShot.getDir()));


                Location sourceTo = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(radius * 3));

                if (player.isSneaking()) {
                    streamShot.setDir(Vectors.getDirectionBetweenLocations(streamShot.getLoc(), sourceTo).normalize());
                } else {
                    streamShot.setDir(player.getEyeLocation().getDirection());
                }
                streamShot.setAbilityStatus(AbilityStatus.SHOT);
                if (streamShot.getLoc().distance(player.getEyeLocation()) > range) {
                    this.remove();
                }

            }
        }
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
        sources.stream().filter(tempBlock -> tempBlock != null).forEach(tempBlock -> tempBlock.revert());
        stream.stream().filter(tempBlock -> tempBlock != null).forEach(tempBlock -> tempBlock.revert());
    }


    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.CHARGED) {
            if (player.isSneaking()) {
                streamShot = new ShootBlockShapeFromLoc(player, name, player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(radius)), stream, radius, false, player.getEyeLocation().getDirection());

                abilityStatus = AbilityStatus.SHOT;
            } else {
                TempDisplayBlock shootingFrom = CollectionUtils.getRandomSetElement(stream);
                new ShootBlocksFromLocGivenType(player, name, shootingFrom.getLoc(), DisplayBlock.ICE, false, true);
                shootingFrom.revert();
                stream.remove(shootingFrom);
                if (stream.isEmpty()) {
                    this.remove();
                }
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
