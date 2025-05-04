package com.sonorous.archetypes.ocean;

import com.sonorous.SonorousAbilitiesPlayer;
import com.sonorous.archetypes.DisplayBlock;
import com.sonorous.util.methods.Blocks;
import com.sonorous.util.temp.TempBlock;
import org.bukkit.Location;

import java.util.Set;
import java.util.stream.Collectors;

public class OceanUtils {

    public static Set<TempBlock> freeze(Location loc, double radius, SonorousAbilitiesPlayer sPlayer) {
        return Blocks.getBlocksAroundPoint(loc, radius).stream()
                .filter(block -> Blocks.getArchetypeBlocks(sPlayer).contains(block.getType()))
                .map(block -> new TempBlock(block, DisplayBlock.ICE, 60000))
                .collect(Collectors.toSet());
    }
}
