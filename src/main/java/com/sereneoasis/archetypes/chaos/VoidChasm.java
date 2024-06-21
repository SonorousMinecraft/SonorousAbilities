//package com.sereneoasis.archetypes.chaos;
//
//import com.sereneoasis.Serenity;
//import com.sereneoasis.ability.superclasses.MasterAbility;
//import com.sereneoasis.abilityuilities.blocks.BlockDisintegrateSphere;
//import com.sereneoasis.abilityuilities.blocks.BlockDisintegrateSphereSuck;
//import com.sereneoasis.abilityuilities.blocks.ShootBlockFromLoc;
//import com.sereneoasis.abilityuilities.blocks.ShootBlockShapeFromLoc;
//import com.sereneoasis.abilityuilities.velocity.Levitate;
//import com.sereneoasis.util.Laser;
//import com.sereneoasis.util.enhancedmethods.EnhancedBlocks;
//import com.sereneoasis.util.enhancedmethods.EnhancedDisplayBlocks;
//import com.sereneoasis.util.enhancedmethods.EnhancedSchedulerEffects;
//import com.sereneoasis.util.methods.*;
//import com.sereneoasis.util.temp.TempBlock;
//import com.sereneoasis.util.temp.TempDisplayBlock;
//import net.md_5.bungee.api.ChatColor;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.block.Block;
//import org.bukkit.block.BlockState;
//import org.bukkit.block.EndGateway;
//import org.bukkit.block.data.BlockData;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.Player;
//import org.bukkit.util.Vector;
//
//import java.awt.*;
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class VoidChasm extends MasterAbility {
//
//    private static final String name = "VoidChasm";
//
//    private Set<TempBlock>chasm = new HashSet<>();
//
//    private Location center;
//
//    private Set<Entity>targets;
//
//    private HashMap<Laser.CrystalLaser, Block>crystalLasers = new HashMap<>();
//
//    private Object[] outerArray;
//
//    private Random random = new Random();
//    private long lastAttacked = System.currentTimeMillis();
//
//    private Levitate levitate;
//    public VoidChasm(Player player) {
//        super(player, name);
//
//        if (shouldStart()){
//            targets = Entities.getEntitiesAroundPoint(player.getLocation(), radius).stream().filter(entity -> entity!=player).collect(Collectors.toSet());
//            targets.forEach(entity -> {
//                entity.setVelocity(new Vector(0, 10, 0));
//                entity.setGlowing(true);
//            } );
//
//
//            Set<TempDisplayBlock> raiseBlocks = EnhancedDisplayBlocks.createTopCircleTempBlocks(this, Material.BLACK_CONCRETE);
//            EnhancedSchedulerEffects.raiseTDBs(raiseBlocks, 40, 2);
//            EnhancedSchedulerEffects.clearTDBs(raiseBlocks, 40);
//            player.setVelocity(new Vector(0, 10, 0));
////            player.teleport(player.getLocation().add(0,100,0));
//
//            Scheduler.performTaskLater(40, () -> {
//                center = player.getLocation();
//                Set<Block> inner = Blocks.getBlocksAroundPoint(center, radius - 1);
//                Set<Block> outer = Blocks.getBlocksAroundPoint(center, radius );
//                outer.removeAll(inner);
//                inner.stream().forEach(block -> {
//                    TempBlock tb = new TempBlock(block, Material.LIGHT, duration);
//                    chasm.add(tb);
//                });
//                outer.stream().forEach(block -> {
//
//                    if (random.nextDouble() < 0.1) {
//                        BlockState state = Material.END_GATEWAY.createBlockData().createBlockState();
////                    ((EndGateway) state).setAge(-1000000);
//
//                        TempBlock tb = new TempBlock(block, state.getBlockData(), duration);
////                    TempBlock tb = new TempBlock(block, Material.BLACK_CONCRETE.createBlockData(), duration, true);
//                        if (block.getState() instanceof EndGateway endGateway )
//                        {
//                            endGateway.setAge(-1000000);
//                            endGateway.update(true);
//                            chasm.add(tb);
//                        }
//                    } else {
//                        TempBlock tb = new TempBlock(block, Material.BLACK_CONCRETE.createBlockData(), duration);
//                        chasm.add(tb);
//                    }
//                });
//
//                outerArray =  outer.toArray();
//                for (int i = 0 ; i < 10; i ++ ) {
//                    Block b = (Block) outerArray[new Random().nextInt(outer.size())];
//                    try {
//                        Laser.CrystalLaser crystalLaser = new Laser.CrystalLaser(b.getLocation(), player.getLocation().subtract(0,1,0), -1, (int) b.getLocation().distance(player.getLocation().subtract(0,1,0)));
//                        crystalLaser.start(Serenity.getPlugin());
//
//                        crystalLasers.put(crystalLaser, b);
//                    } catch (ReflectiveOperationException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                }
//
//
//                 levitate = new Levitate(player, name);
//
//                targets.forEach(entity -> {
//                    entity.teleport(player);
//                    entity.setGravity(false);
//                });
//
//
//                start();
//            });
//
//        }
//    }
//
//    @Override
//    public void progress() throws ReflectiveOperationException {
//        if (System.currentTimeMillis() > startTime + duration) {
//            this.remove();
//        }
//
//        if (player.isSneaking() && sPlayer.getHeldAbility().equals(name)) {
//            player.setVelocity(player.getEyeLocation().getDirection().multiply(speed));
//        }
//
//        Entities.getEntitiesAroundPoint(center, radius).stream().forEach(entity -> {
//            if (entity.getLocation().distance(center) > radius-3) {
//                entity.setVelocity(Vectors.getDirectionBetweenLocations(entity.getLocation(), center).normalize());
//            }
//        });
//
//        crystalLasers.forEach((crystalLaser, block) -> {
//            try {
//                crystalLaser.moveEnd(player.getLocation().subtract(0,1,0));
//
//            } catch (ReflectiveOperationException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        if (System.currentTimeMillis() - lastAttacked > 5000) {
//            AbilityUtils.sendActionBar(player, "READY", ChatColor.of(Color.MAGENTA) );
//        }
//    }
//
//    public void setHasClicked() throws ReflectiveOperationException {
//        if (System.currentTimeMillis() - lastAttacked > 5000) {
//            lastAttacked = System.currentTimeMillis();
//            outerArray = Arrays.stream(outerArray).filter((block) -> !crystalLasers.values().contains(block)).toArray();
//
//            for (Map.Entry<Laser.CrystalLaser, Block> entry : crystalLasers.entrySet()) {
//                Laser.CrystalLaser crystalLaser = entry.getKey();
//                Block block = entry.getValue();
//
//                double projectileSize = radius/4;
//
//                Vector dir = null;
//                Entity targetEntity = Entities.getFacingEntity(player, radius * 2, 1.0);
//                if (targetEntity != null) {
//                    dir =  Vectors.getDirectionBetweenLocations(crystalLaser.getStart(), targetEntity.getLocation()).normalize();
////                    ShootBlockFromLoc shootBlockFromLoc = new ShootBlockFromLoc(player, name, block1.getLocation(), Material.BLACK_CONCRETE, true, Vectors.getDirectionBetweenLocations(block1.getLocation(), targetEntity.getLocation()).normalize());
////                    shootBlockFromLoc.setGlowing(org.bukkit.Color.PURPLE);
//
//                } else {
//                    Block targetBlock = Blocks.getFacingBlock(player, radius * 2);
//                    if (targetBlock != null) {
////                        ShootBlockFromLoc shootBlockFromLoc = new ShootBlockFromLoc(player, name, block1.getLocation(), Material.BLACK_CONCRETE, true, Vectors.getDirectionBetweenLocations(block1.getLocation(), targetBlock.getLocation()).normalize());
////                        shootBlockFromLoc.setGlowing(org.bukkit.Color.PURPLE);
//                        dir =  Vectors.getDirectionBetweenLocations(crystalLaser.getStart(), targetBlock.getLocation()).normalize();
//                    }
//                }
//
//                if (dir != null) {
//                     Set<TempDisplayBlock> projectile =  Blocks.getBlocksAroundPoint(block.getLocation(), projectileSize).stream()
//                             .filter(b -> b.getType().equals(Material.BLACK_CONCRETE) || b.getType().equals(Material.END_GATEWAY))
//                             .map(endGateway -> new TempDisplayBlock(endGateway.getLocation(), Material.BLACK_CONCRETE, duration, size))
//                             .collect(Collectors.toSet());
//
//                     new ShootBlockShapeFromLoc(player, name, block.getLocation(), projectile, projectileSize, true, dir);
////                    new BlockDisintegrateSphereSuck(player, name, block.getLocation(), block.getLocation().add(dir.clone().multiply(-10)), 0, projectileSize, 1);
//                    new BlockDisintegrateSphere(player, name, block.getLocation(), 0, projectileSize, 1, true);
//                }
//
//
//
////                        TempBlock tb = TempBlock.getTempBlock(block1);
////                        chasm.remove(tb);
////                        tb.revert();
////                        new BlockDisintegrateSphere(player, name, block1.getLocation(), 0, projectileSize, 1, true);
////
////                    }
////                });
//                block = (Block) outerArray[new Random().nextInt(outerArray.length)];
//                crystalLasers.replace(crystalLaser, block);
//                crystalLaser.moveStart(block.getLocation());
//            }
//        }
//    }
//
//    @Override
//    public String getName() {
//        return name;
//    }
//
//    @Override
//    public void remove() {
//        super.remove();
////        new BlockDisintegrateSphereSuck(player, name,center, center, 0, radius, 1);
//        chasm.forEach(TempBlock::revert);
//        targets.forEach(entity -> {
//            entity.setGravity(true);
//            entity.setGlowing(false);
//        });
//        sPlayer.addCooldown(name, cooldown);
//
//        crystalLasers.keySet().forEach(Laser::stop);
//        levitate.remove();
//    }
//}
