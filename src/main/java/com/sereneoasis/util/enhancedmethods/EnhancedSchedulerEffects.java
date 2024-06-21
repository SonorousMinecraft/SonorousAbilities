package com.sereneoasis.util.enhancedmethods;

import com.sereneoasis.util.methods.Constants;
import com.sereneoasis.util.methods.Scheduler;
import com.sereneoasis.util.temp.TempDisplayBlock;

import java.util.Set;

public class EnhancedSchedulerEffects {

    public static void raiseTDBs(Set<TempDisplayBlock> tempDisplayBlocks, int totalTicks, int increment) {
        for (int i = 0; i < totalTicks; i += increment) {
            Scheduler.performTaskLater(i, () -> {
                tempDisplayBlocks.stream().forEach(tempDisplayBlock -> tempDisplayBlock.moveToAndMaintainFacing(tempDisplayBlock.getLoc().add(0, (Math.random() + 1) * 5 * Constants.BLOCK_RAISE_SPEED, 0)));
            });
        }
    }

    public static void clearTDBs(Set<TempDisplayBlock> tempDisplayBlocks, int ticks) {
        Scheduler.performTaskLater(ticks, () -> {
            tempDisplayBlocks.forEach(TempDisplayBlock::revert);
        });
    }

}
