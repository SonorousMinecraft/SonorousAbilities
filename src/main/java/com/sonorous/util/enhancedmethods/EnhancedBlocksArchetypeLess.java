package com.sonorous.util.enhancedmethods;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.methods.Blocks;
import com.sonorous.util.methods.Locations;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EnhancedBlocksArchetypeLess {

    public static Set<Block> getFacingSphereBlocks(CoreAbility coreability) {
        Location loc = Blocks.getFacingBlockLoc(coreability.getPlayer(), coreability.getSourceRange());
        if (loc == null) {
            return new HashSet<>();
        }
        return Blocks.getBlocksAroundPoint(loc.getBlock().getLocation(), coreability.getRadius()).stream()
                .collect(Collectors.toSet());
    }


    public static boolean isStandingOnSource(CoreAbility coreability) {
        return !Blocks.getBlocksAroundPoint(coreability.getPlayer().getLocation().subtract(0, 1, 0), 2).stream()
                .collect(Collectors.toSet()).isEmpty();

    }

    public static Set<Block> getFacingSphereBlocks(CoreAbility coreability, Location loc) {
        if (loc == null) {
            return new HashSet<>();
        }
        return Blocks.getSolidBlocksAroundPoint(loc, coreability.getRadius()).stream()
                .collect(Collectors.toSet());
    }

    public static Set<Block> getOutsideSphereBlocks(CoreAbility coreability, Location loc) {
        if (loc == null) {
            return new HashSet<>();
        }
//        Set<Block> facingSphereBlocks = getFacingSphereBlocks(coreability, loc);
        return Locations.getOutsideSphereLocs(loc, coreability.getRadius(), 1).stream().map(location -> location.getBlock()).collect(Collectors.toSet());
//        if (coreability.getRadius() > 1) {
//            facingSphereBlocks.removeIf(block -> Blocks.getBlocksAroundPoint(loc, coreability.getRadius() - 1).contains(block));
//        }
//        return facingSphereBlocks;

    }

    public static Set<Block> getTopCylinderBlocks(CoreAbility coreAbility, double height) {
        Set<Block> blocks = Blocks.getBlocksAroundPoint(coreAbility.getPlayer().getLocation().subtract(0, 1, 0), coreAbility.getRadius()).stream()
                .filter(block -> Blocks.isTopBlock(block))
                .collect(Collectors.toSet());


        Set<Block> beneath = new HashSet<>();
        for (int h = 1; h < height; h++) {
            int finalH = h;
            blocks.forEach(block -> {
                Block newBlock = block.getRelative(0, -finalH, 0);
                beneath.add(newBlock);
            });
        }
        blocks.addAll(beneath);
        return blocks;
    }

    public static Set<Block> getTopHCircleBlocks(CoreAbility coreAbility) {
        Set<Block> blocks = Locations.getOutsideSphereLocs(coreAbility.getPlayer().getLocation(), coreAbility.getSourceRange(), 1).stream()
                .map(Location::getBlock)
                .filter(block -> Blocks.isTopBlock(block))
                .collect(Collectors.toSet());
        return blocks;
    }

    public static Set<Block> getTopCircleBlocks(CoreAbility coreAbility) {
        return Blocks.getBlocksAroundPoint(coreAbility.getPlayer().getLocation(), coreAbility.getRadius()).stream().filter(b -> Blocks.isTopBlock(b) && !b.isPassable()).collect(Collectors.toSet());
    }

    public static Set<Block> getTopCircleBlocksFloor(CoreAbility coreAbility) {

        Location floorLoc = Blocks.getBelowBlock(coreAbility.getPlayer().getLocation().getBlock(), 100).getLocation();

        return Blocks.getBlocksAroundPoint(floorLoc, coreAbility.getRadius()).stream().filter(b -> Blocks.isTopBlock(b) && !b.isPassable()).collect(Collectors.toSet());
    }


    public static Set<Block> getTopCircleBlocksFloor(CoreAbility coreAbility, Location target) {

        if (Blocks.isSolid(target)) {
            return Blocks.getBlocksAroundPoint(target, coreAbility.getRadius()).stream().filter(b -> Blocks.isTopBlock(b) && !b.isPassable()).collect(Collectors.toSet());
        }
        Location floorLoc = Blocks.getBelowBlock(target.getBlock(), 100).getLocation();


        return Blocks.getBlocksAroundPoint(floorLoc, coreAbility.getRadius()).stream().filter(b -> Blocks.isTopBlock(b) && !b.isPassable()).collect(Collectors.toSet());
    }

    public static Set<Block> getCircleAtYBlocks(CoreAbility coreAbility, Location loc, int y) {
        return Blocks.getBlocksAroundPoint(loc, coreAbility.getRadius()).stream().filter(b -> b.getY() == y).collect(Collectors.toSet());
    }


    public static Set<Block> getTopHCircleBlocks(CoreAbility coreAbility, Location loc) {
        Set<Block> blocks = Locations.getOutsideSphereLocs(loc, coreAbility.getSourceRange(), 1).stream()
                .map(Location::getBlock)
                .filter(block -> Blocks.isTopBlock(block))
                .collect(Collectors.toSet());

        return blocks;
    }


}
