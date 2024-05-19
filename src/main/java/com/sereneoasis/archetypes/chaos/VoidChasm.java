package com.sereneoasis.archetypes.chaos;

import com.sereneoasis.Serenity;
import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.abilityuilities.blocks.ShootBlockFromLoc;
import com.sereneoasis.abilityuilities.velocity.Levitate;
import com.sereneoasis.util.Laser;
import com.sereneoasis.util.enhancedmethods.EnhancedBlocks;
import com.sereneoasis.util.methods.*;
import com.sereneoasis.util.temp.TempBlock;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.EndGateway;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

public class VoidChasm extends MasterAbility {

    private static final String name = "VoidChasm";

    private Set<TempBlock>chasm = new HashSet<>();

    private Location center;

    private Set<Entity>targets;

    private HashMap<Laser.CrystalLaser, Block>crystalLasers = new HashMap<>();

    private Object[] outerArray;

    private long lastAttacked = System.currentTimeMillis();
    public VoidChasm(Player player) {
        super(player, name);

        if (shouldStart()){
            targets = Entities.getEntitiesAroundPoint(player.getLocation(), radius).stream().filter(entity -> entity!=player).collect(Collectors.toSet());

            player.teleport(player.getLocation().add(0,100,0));

//            Scheduler.performTaskLater(20, () -> {
                center = player.getLocation();
                Set<Block> inner = Blocks.getBlocksAroundPoint(center, radius - 1);
                Set<Block> outer = Blocks.getBlocksAroundPoint(center, radius );
                outer.removeAll(inner);
                inner.stream().forEach(block -> {
                    TempBlock tb = new TempBlock(block, Material.LIGHT, duration, true);
                    chasm.add(tb);
                });
                outer.stream().forEach(block -> {

                    BlockState state = Material.END_GATEWAY.createBlockData().createBlockState();
//                    ((EndGateway) state).setAge(-1000000);

                    TempBlock tb = new TempBlock(block, state.getBlockData(), duration, true);
                    EndGateway endGateway =  ((EndGateway) block.getState());
                    endGateway.setAge(-1000000);
                    endGateway.update(true);
                    chasm.add(tb);
                });

                outerArray =  outer.toArray();
                for (int i = 0 ; i < 10; i ++ ) {
                    Block b = (Block) outerArray[new Random().nextInt(outer.size())];
                    try {
                        Laser.CrystalLaser crystalLaser = new Laser.CrystalLaser(b.getLocation(), player.getLocation().subtract(0,1,0), -1, (int) b.getLocation().distance(player.getLocation().subtract(0,1,0)));
                        crystalLaser.start(Serenity.getPlugin());

                        crystalLasers.put(crystalLaser, b);
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }

                }


                Levitate levitate = new Levitate(player, name);

                targets.forEach(entity -> {
                    entity.teleport(player);
                    entity.setGravity(false);
                });


                start();
//            });

        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (System.currentTimeMillis() > startTime + duration) {
            this.remove();
        }

        if (player.isSneaking() && sPlayer.getHeldAbility().equals(name)) {
            player.setVelocity(player.getEyeLocation().getDirection().multiply(speed));
        }

        Entities.getEntitiesAroundPoint(center, radius).stream().forEach(entity -> {
            if (entity.getLocation().distance(center) > radius-3) {
                entity.setVelocity(entity.getVelocity().multiply(-1));
            }
        });

        crystalLasers.forEach((crystalLaser, block) -> {
            try {
                crystalLaser.moveEnd(player.getLocation().subtract(0,1,0));

            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        });

        if (System.currentTimeMillis() - lastAttacked > 5000) {
            AbilityUtils.sendActionBar(player, "READY", ChatColor.of(Color.MAGENTA) );
        }
    }

    public void setHasClicked() throws ReflectiveOperationException {
        if (System.currentTimeMillis() - lastAttacked > 5000) {
            lastAttacked = System.currentTimeMillis();
            outerArray = Arrays.stream(outerArray).filter((block) -> !crystalLasers.values().contains(block)).toArray();

            for (Map.Entry<Laser.CrystalLaser, Block> entry : crystalLasers.entrySet()) {
                Laser.CrystalLaser crystalLaser = entry.getKey();
                Block block = entry.getValue();

                Blocks.getBlocksAroundPoint(block.getLocation(), 3).forEach(block1 -> {
                    if (TempBlock.isTempBlock(block1)) {

                        Entity targetEntity = Entities.getFacingEntity(player, radius * 2, 1.0);
                        if (targetEntity != null) {
                            ShootBlockFromLoc shootBlockFromLoc = new ShootBlockFromLoc(player, name, block1.getLocation(), Material.WHITE_CONCRETE, true, Vectors.getDirectionBetweenLocations(block1.getLocation(), targetEntity.getLocation()).normalize());
                            shootBlockFromLoc.setGlowing(org.bukkit.Color.PURPLE);

                        } else {
                            Block targetBlock = Blocks.getFacingBlock(player, radius * 2);
                            if (targetBlock != null) {
                                ShootBlockFromLoc shootBlockFromLoc = new ShootBlockFromLoc(player, name, block1.getLocation(), Material.WHITE_CONCRETE, true, Vectors.getDirectionBetweenLocations(block1.getLocation(), targetBlock.getLocation()).normalize());
                                shootBlockFromLoc.setGlowing(org.bukkit.Color.PURPLE);
                            }
                        }
                        TempBlock tb = TempBlock.getTempBlock(block1);
                        chasm.remove(tb);
                        tb.revert();
                        new TempBlock(block1, Material.WHITE_CONCRETE, duration, true);

                    }
                });
                block = (Block) outerArray[new Random().nextInt(outerArray.length)];
                crystalLasers.replace(crystalLaser, block);
                crystalLaser.moveStart(block.getLocation());
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void remove() {
        super.remove();
        chasm.forEach(TempBlock::revert);
        targets.forEach(entity -> entity.setGravity(true));
        sPlayer.addCooldown(name, cooldown);

        crystalLasers.keySet().forEach(Laser::stop);
    }
}
