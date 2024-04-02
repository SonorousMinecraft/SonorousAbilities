package com.sereneoasis.util.enhancedmethods;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Locations;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EnhancedBlocks {

    public static Set<Block> getFacingSphereBlocks(CoreAbility coreability) {
        Location loc = Blocks.getFacingBlockLoc(coreability.getPlayer(), coreability.getSourceRange());
        if (loc == null) {
            return new HashSet<>();
        }
        return Blocks.getBlocksAroundPoint(loc.getBlock().getLocation(), coreability.getRadius()).stream()
                .filter(block -> Blocks.getArchetypeBlocks(coreability.getsPlayer()).contains(block.getType()))
                .collect(Collectors.toSet());
    }

    public static Set<Block> getFacingSphereBlocks(CoreAbility coreability, Location loc) {
        if (loc == null) {
            return new HashSet<>();
        }
        return Blocks.getBlocksAroundPoint(loc, coreability.getRadius()).stream()
                .filter(block -> Blocks.getArchetypeBlocks(coreability.getsPlayer()).contains(block.getType()))
                .collect(Collectors.toSet());
    }

    public static Set<Block> getTopCircleBlocks(CoreAbility coreAbility){
        Set<Block> blocks = Locations.getOutsideSphereLocs(coreAbility.getPlayer().getLocation(), coreAbility.getSourceRange(), 1).stream()
                .map(Location::getBlock)
                .filter(block -> Blocks.getArchetypeBlocks(coreAbility.getsPlayer()).contains(block.getType()) && Blocks.isTopBlock(block))
                .collect(Collectors.toSet());

        return blocks;
    }
}
