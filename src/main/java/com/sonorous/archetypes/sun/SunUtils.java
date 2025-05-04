package com.sonorous.archetypes.sun;

import com.sonorous.abilityuilities.blocks.BlockExplodeSphere;
import com.sonorous.archetypes.DisplayBlock;
import com.sonorous.util.methods.Blocks;
import com.sonorous.util.methods.Scheduler;
import com.sonorous.util.temp.TempBlock;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class SunUtils {

    public static void blockExplode(Player player, String name, Location loc, double radius, double increment) {
        new BlockExplodeSphere(player, name, loc, radius, increment);

        Scheduler.performTaskLater(5, () -> {
            Blocks.getBlocksAroundPoint(loc, radius + 1).forEach(block -> {
                if (!block.isPassable()) {
                    new TempBlock(block, DisplayBlock.SUN, 60000);
//                    new TB(block, 60000, DisplayBlock.SUN);
                }
            });
        });
    }
}
