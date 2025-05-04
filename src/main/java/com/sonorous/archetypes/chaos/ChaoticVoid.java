//package com.sereneoasis.archetypes.chaos;
//
//import com.sereneoasis.ability.superclasses.MasterAbility;
//import com.sereneoasis.util.methods.*;
//import com.sereneoasis.util.temp.TempBlock;
//import net.md_5.bungee.api.ChatColor;
//import org.bukkit.Bukkit;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.block.Block;
//import org.bukkit.boss.BarColor;
//import org.bukkit.boss.BarStyle;
//import org.bukkit.boss.BossBar;
//import org.bukkit.entity.Player;
//import org.bukkit.util.BoundingBox;
//import org.bukkit.util.Vector;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Random;
//import java.util.Set;
//
//public class ChaoticVoid extends MasterAbility {
//
//    private static final String name = "ChaoticVoid";
//
//    private BoundingBox chaos;
//
//    private HashMap<BoundingBox, Location> portalsFrom = new HashMap<>();
//
//    private HashSet<BoundingBox> portalsTo = new HashSet<>();
//
//    private Set<TempBlock> tempBlocks = new HashSet<>();
//
//    private Location origin;
//
//    private Random random = new Random();
//
//    private Location lastTeleported;
//
//    private long lastPortalMade = System.currentTimeMillis();
//
//    private BossBar bar;
//
//
//
//    public ChaoticVoid(Player player) {
//        super(player, name);
//
////        Bukkit.broadcastMessage(SonorousAbilities.getPlugin().getServer().getStructureManager().getStructures().toString());
////    Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("shipwreck")).place(player.getLocation(), false, StructureRotation.NONE, Mirror.NONE, 1, 0, new Random());
//
//
////        Structures.spawnStructureFill(player.getLocation(), "ancient_city/city_center/city_center_1");
//
//        if (shouldStart()){
//            bar = BossBarUtils.initBar(player, name, BarColor.PURPLE);
//
//            origin = player.getLocation();
//            origin.setY(player.getWorld().getMaxHeight() - 40);
//            origin.setDirection(new Vector(1,0,0));
//            Location loc = origin.clone().subtract(0,2,19);
//
//            Structures.spawnStructure(loc, "ancient_city/city/entrance/entrance_connector");
//            Structures.spawnStructure(loc.add(30,5,0), "ancient_city/city/entrance/entrance_path_1");
//            Structures.spawnStructure(loc.add(24,0,0), "ancient_city/city/entrance/entrance_path_2");
//            Structures.spawnStructure(loc.add(24,0,0), "ancient_city/city/entrance/entrance_path_3");
//            Structures.spawnStructure(loc.add(24,0,0), "ancient_city/city/entrance/entrance_path_4");
//            Structures.spawnStructure(loc.add(24,0,0), "ancient_city/city/entrance/entrance_path_5");
//
//
//            chaos = BoundingBox.of(loc,origin);
//            chaos.expandDirectional(33,10,20);
////            chaos.expandDirectional(0,0,-20);
//
////            TempBlock tb = new TempBlock(chaos.getMin().toLocation(player.getWorld()).getBlock(), Material.DIAMOND_BLOCK, duration, true);
////            TempBlock tb2 = new TempBlock(chaos.getMax().toLocation(player.getWorld()).getBlock(), Material.EMERALD_BLOCK, duration, true);
//
//            start();
//        }
//    }
//
//    @Override
//    public void progress() throws ReflectiveOperationException {
////        if (sPlayer.getHeldAbility().equals(name) && player.isSneaking() && !chaos.contains(player.getBoundingBox())){
////            player.teleport(origin);
////        }
//
//        BossBarUtils.manageBarDuration(bar, player, startTime, duration);
//
//        if (player.getLocation().getBlock().getType().equals(Material.END_GATEWAY)){
//            handleTeleport();
//        }
//
//        Block looking = Blocks.getFacingBlock(player, sourceRange);
//        if (looking != null){
//            if (looking.getType().equals(Material.END_GATEWAY)){
//                portalsFrom.forEach((boundingBox, location) -> {
//                    if (boundingBox.contains(looking.getBoundingBox())){
//                        AbilityUtils.sendActionBar(player, "X: " + location.getBlockX() + ", Y:" + location.getBlockY() + ", Z:" + location.getBlockX(), ChatColor.DARK_PURPLE);
//                    }
//                });
//            }
//        }
//
//        if (System.currentTimeMillis() - startTime > duration){
//            this.remove();
//        }
//
//    }
//
//    public void setHasClicked(){
//        if ((System.currentTimeMillis() - lastPortalMade) > 3000) {
//            if (!chaos.contains(player.getBoundingBox())) {
//                Block targetBlock = Blocks.getFacingBlock(player, sourceRange);
//
//                if (targetBlock != null) {
//                    portalsTo.add(BoundingBox.of(targetBlock).expand(radius));
//                    for (Block b : Blocks.getBlocksAroundPoint(targetBlock.getLocation(), radius)) {
//                        TempBlock tb = new TempBlock(b, Material.END_GATEWAY, duration);
//                        tempBlocks.add(tb);
//                    }
//
//                    Vector randomiser = chaos.getMax().subtract(chaos.getMin()).multiply(new Vector(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5));
//                    Location midpoint = (chaos.getMax().add(chaos.getMin())).multiply(0.5).toLocation(player.getWorld());
//                    Location portalFrom = midpoint.add(randomiser).add(0,radius,0);
//                    for (Block b : Blocks.getBlocksAroundPoint(portalFrom, radius)) {
//                        TempBlock tb = new TempBlock(b, Material.END_GATEWAY, duration);
//                        tempBlocks.add(tb);
//                    }
//
//                    portalsFrom.put(BoundingBox.of(portalFrom.getBlock()).expand(radius), targetBlock.getLocation());
//                    lastPortalMade = System.currentTimeMillis();
//                }
//            }
//        }
//
//    }
//
//    public void handleTeleport(){
//            if (chaos.overlaps((player.getBoundingBox().expand(radius * 10)))) {
//                portalsFrom.forEach((boundingBox, location) -> {
//                    if (boundingBox.contains(player.getBoundingBox())) {
//                        player.teleport(location);
//                        lastTeleported = location.clone();
//                        Scheduler.performTaskLater(200, () -> {
//                            lastTeleported = null;
//                        });
//                    }
//                });
//            } else if (lastTeleported == null || portalsTo.stream().noneMatch(boundingBox -> boundingBox.contains(lastTeleported.toVector()))) {
//                player.teleport(origin);
//            }
//    }
//
//
//    @Override
//    public void remove() {
//        super.remove();
//        sPlayer.addCooldown(name, cooldown);
//        bar.removeAll();
//        for (Block b : Blocks.getBlocksAroundPoint(chaos.getCenter().toLocation(player.getWorld()), 100)){
//            b.setBlockData(Material.AIR.createBlockData());
//        }
//        tempBlocks.forEach(tempBlock -> {
//            if (tempBlock != null) {
//                tempBlock.revert();
//            }
//        });
//    }
//
//    @Override
//    public String getName() {
//        return name;
//    }
//}
