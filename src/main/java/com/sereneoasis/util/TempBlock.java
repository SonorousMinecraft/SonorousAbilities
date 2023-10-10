package com.sereneoasis.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

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

    public TempBlock( Block block, BlockData newData, final long revertTime) {
        if (! INSTANCES.containsKey(block)) {
            this.block = block;
            this.newData = newData;
            this.revertTime = System.currentTimeMillis() + revertTime;

            this.oldData = block.getBlockData();

            block.setBlockData(newData);
            INSTANCES.put(block, this);
            REVERT_QUEUE.add(this);
        }
    }

    public static boolean isTempBlock(Block block)
    {
        if (INSTANCES.containsKey(block))
        {
            return true;
        }
        return false;
    }

    public void revertBlock()
    {
        block.setBlockData(oldData);
        REVERT_QUEUE.remove();
        INSTANCES.remove(block,this);
    }

    public long getRevertTime() {
        return revertTime;
    }
}
