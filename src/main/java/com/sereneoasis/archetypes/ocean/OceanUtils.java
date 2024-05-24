package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.SerenityPlayer;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.temp.TempBlock;
import org.bukkit.Location;

import java.util.Set;
import java.util.stream.Collectors;

public class OceanUtils {

    public static Set<TempBlock> freeze(Location loc, double radius, SerenityPlayer sPlayer){
        return Blocks.getBlocksAroundPoint(loc, radius).stream()
                .filter(block -> Blocks.getArchetypeBlocks(sPlayer).contains(block.getType()))
                .map(block -> new TempBlock(block, DisplayBlock.ICE, 60000))
                .collect(Collectors.toSet());
    }
}
