package com.sereneoasis.util.temp;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sakrajin
 * Represents temporary block instances and handles reverting
 */
public class TempBlock {

    private static final PriorityQueue<TempBlock> REVERT_QUEUE = new PriorityQueue<>(100, (t1, t2) -> (int) (t1.revertTime - t2.revertTime));

    public static PriorityQueue<TempBlock> getRevertQueue()
    {
        return REVERT_QUEUE;
    }

    private static Map<Block, TempBlock> INSTANCES = new ConcurrentHashMap<>();

    private Block block;

    private BlockData oldData, newData;

    private long revertTime;

    public TempBlock( Block block, BlockData newData, final long revertTime, boolean canReplaceBlocks)
    {
        if (! INSTANCES.containsKey(block)) {
            this.block = block;
            if (!canReplaceBlocks && !block.getType().isAir())
            {
                return;
            }
            this.newData = newData;
            this.revertTime = System.currentTimeMillis() + revertTime;

            this.oldData = block.getBlockData();

            block.setBlockData(newData);
            INSTANCES.put(block, this);
            REVERT_QUEUE.add(this);
        }
    }
    public TempBlock( Block block, BlockData newData, final long revertTime) {
        new TempBlock(block, newData, revertTime, true);
    }

    public static boolean isTempBlock(Block block)
    {
        if (INSTANCES.containsKey(block))
        {
            return true;
        }
        return false;
    }

    public static TempBlock getTempBlock(Block block)
    {
        return INSTANCES.get(block);
    }

    public void revertBlock()
    {
        this.block.setBlockData(oldData);
        REVERT_QUEUE.remove();
        INSTANCES.remove(block,this);
    }

    public long getRevertTime() {
        return revertTime;
    }
}
