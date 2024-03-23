package com.sereneoasis.util.methods;

import com.sereneoasis.SerenityPlayer;
import com.sereneoasis.archetypes.data.ArchetypeDataManager;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * @author Sakrajin
 * Methods which are related to blocks
 */
public class Blocks {

    public static void selectSourceAnimationShape(Collection<Location> locs, Color color, double size) {
        for (Location loc : locs){
            selectSourceAnimation(loc, color, size);
        }
    }

    public static void selectSourceAnimation(Location loc, Color color, double size) {
        Material type = loc.getBlock().getRelative(BlockFace.DOWN).getType();
        Location tempLoc = loc.clone().add(-size/2, 0, -size/2);
        if (type == Material.WATER) {
            new TempDisplayBlock(tempLoc, Material.BLUE_STAINED_GLASS, 1000, size, true, color);
        } else {
            new TempDisplayBlock(tempLoc,type, 1000, size, true, color);
        }
    }

    public static void selectSourceAnimationBlock(Block block, Color color) {
        Material type = block.getType();
        if (type == Material.WATER) {
            new TempDisplayBlock(block.getLocation(), Material.BLUE_STAINED_GLASS, 1000, 1, true, color);
        } else {
            new TempDisplayBlock(block.getLocation(),type, 1000, 1, true, color);
        }
    }

    public static TempDisplayBlock selectSourceAnimationManual(Location loc, Color color, double size) {
        Material type = loc.getBlock().getRelative(BlockFace.DOWN).getType();
        Location tempLoc = loc.clone().add(-size/2, 0, -size/2);
        if (type == Material.WATER) {
            return new TempDisplayBlock(tempLoc, Material.BLUE_STAINED_GLASS, 30000, size, true, color);
        } else {
            return new TempDisplayBlock(tempLoc,type, 30000, size, true, color);
        }
    }

    public static Block getBelowBlock(Block b, double distance) {
        Location loc = b.getLocation();
        Block block = b;
        if (loc.getWorld().rayTraceBlocks(loc, new Vector(0, -1, 0), distance, FluidCollisionMode.NEVER) != null) {
            block = loc.getWorld().rayTraceBlocks(loc, new Vector(0, -1, 0), distance, FluidCollisionMode.NEVER).getHitBlock();
        }
        return block;
    }

    public static boolean isBelowArchetype(Block b, double distance, SerenityPlayer serenityPlayer) {
        Location loc = b.getLocation();
        if (loc.getWorld().rayTraceBlocks(loc, new Vector(0, -1, 0), distance, FluidCollisionMode.ALWAYS) != null) {
            Block block = loc.getWorld().rayTraceBlocks(loc, new Vector(0, -1, 0), distance, FluidCollisionMode.ALWAYS).getHitBlock();
            return Blocks.getArchetypeBlocks(serenityPlayer).contains(block.getType());
        }
        return false;
    }

    public static Block getFacingBlock(Player player, double distance) {
        Location loc = player.getEyeLocation().clone();
        Block block = null;
        if (loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.NEVER) != null) {
            RayTraceResult rayTraceResult = loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.NEVER);
            block = rayTraceResult.getHitBlock();
        }
        return block;
    }

    public static Location getFacingBlockLoc(Player player, double distance) {
        Location loc = player.getEyeLocation().clone();
        if (loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.NEVER) != null) {
            RayTraceResult rayTraceResult = loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.NEVER);
            Location hitLoc = new Location(loc.getWorld(), rayTraceResult.getHitPosition().getX(), rayTraceResult.getHitPosition().getY(), rayTraceResult.getHitPosition().getZ());
            return hitLoc;
        }
        return null;
    }

    public static boolean playerLookingAtBlockDisplay(Player player, BlockDisplay target, double maxDistance, double size) {
        Location lowest = target.getLocation().clone().subtract(size/2,size/2,size/2);
        Location highest = lowest.clone().add(size, size, size);
        BoundingBox boundingBox = new BoundingBox(lowest.getX(), lowest.getY(), lowest.getZ(), highest.getX(), highest.getY(), highest.getZ());
        Location loc = player.getEyeLocation().clone();
        Vector dir = player.getEyeLocation().getDirection().clone().normalize();
        RayTraceResult rayTraceResult = boundingBox.rayTrace(loc.toVector(), dir, maxDistance);
        if (rayTraceResult != null) {
            return true;
        }
        return false;
    }


    public static Block getFacingBlockOrLiquid(Player player, double distance) {
        Location loc = player.getEyeLocation().clone();
        Block block = null;
        if (loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.ALWAYS) != null) {
            block = loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.ALWAYS).getHitBlock();
        }
        return block;
    }

    public static BlockFace getFacingBlockFace(Location loc, Vector dir, double distance) {
        BlockFace blockFace = null;
        if (loc.getWorld().rayTraceBlocks(loc, dir, distance, FluidCollisionMode.NEVER) != null) {
            blockFace = loc.getWorld().rayTraceBlocks(loc, dir, distance, FluidCollisionMode.NEVER).getHitBlockFace();
        }
        return blockFace;
    }

    public static Location getFacingBlockOrLiquidLoc(Player player, double distance) {
        Location loc = player.getEyeLocation().clone();
        if (loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.ALWAYS) != null) {
            RayTraceResult rayTraceResult = loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.ALWAYS);
            return new Location(loc.getWorld(), rayTraceResult.getHitPosition().getX(), rayTraceResult.getHitPosition().getY(), rayTraceResult.getHitPosition().getZ());
        }
        return null;
    }

    public static Set<Block> getBlocksAroundPoint(Location loc, double radius) {
        Set<Block> blocks = new HashSet<>();
        for (double y = -radius; y < radius; y++) {
            for (double x = -radius; x < radius; x++) {
                for (double z = -radius; z < radius; z++) {
                    Block block = loc.clone().add(x, y, z).getBlock();
                    if (block.getLocation().distanceSquared(loc) < ((radius) * (radius))) {
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }

    public static List<Block> getBlocksAroundPoint(Location loc, double radius, Material type) {
        List<Block> blocks = new ArrayList<Block>();
        for (double y = -radius; y < radius; y++) {
            for (double x = -radius; x < radius; x++) {
                for (double z = -radius; z < radius; z++) {
                    Block block = loc.clone().add(x, y, z).getBlock();
                    if (block.getType() == type && block.getLocation().distanceSquared(loc) < ((radius) * (radius))) {
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }

    public static Set<Material> getArchetypeBlocks(SerenityPlayer serenityPlayer) {
        return ArchetypeDataManager.getArchetypeData(serenityPlayer.getArchetype()).getBlocks();
    }

    public static Block getSourceBlock(Player player, SerenityPlayer sPlayer, double sourceRange) {
        Block source = Blocks.getFacingBlockOrLiquid(player, sourceRange);
        if (source != null && Blocks.getArchetypeBlocks(sPlayer).contains(source.getType())) {
            return source;
        }
        return null;
    }

    public static boolean isTopBlock(Block b) {
        if (b.getLocation().add(0, 1, 0).getBlock().getType().isSolid()) {
            return false;
        }
        return true;
    }

    public static boolean isSolid(Location loc){
        return !loc.getBlock().isPassable();
    }

}
