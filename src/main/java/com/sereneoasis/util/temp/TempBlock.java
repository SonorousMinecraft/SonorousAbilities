package com.sereneoasis.util.temp;

import com.sereneoasis.archetypes.DisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Sakrajin
 * Represents temporary block instances and handles reverting
 */
public class TempBlock {



    private static final PriorityQueue<TempBlock> REVERT_QUEUE = new PriorityQueue<>(100, (t1, t2) -> (int) (t1.revertTime - t2.revertTime));

    public static PriorityQueue<TempBlock> getRevertQueue() {
        return REVERT_QUEUE;
    }

    private static Map<Block, TempBlock> INSTANCES = new ConcurrentHashMap<>();

    private Block block;

    private Location loc;

    private BlockData oldData, newData;

    private long revertTime;

    public TempBlock(Block block, DisplayBlock blocks, final long revertTime, boolean canReplaceBlocks) {
        if (block != null &&!INSTANCES.containsKey(block)) {
            this.block = block;
            this.loc = block.getLocation();
            if (!canReplaceBlocks && !block.getType().isAir()) {
                return;
            }
            int randomIndex = ThreadLocalRandom.current().nextInt(blocks.getBlocks().size());
            BlockData newData = blocks.getBlocks().get(randomIndex).createBlockData();
            this.newData = newData;
            this.revertTime = System.currentTimeMillis() + revertTime;

            this.oldData = block.getBlockData().clone();

            block.setBlockData(newData);
            INSTANCES.put(block, this);
            REVERT_QUEUE.add(this);
        }
    }

    public TempBlock(Block block, Material type, final long revertTime, boolean canReplaceBlocks) {
        if (block != null &&!INSTANCES.containsKey(block)) {
            this.block = block;
            this.loc = block.getLocation();

            if (!canReplaceBlocks && !block.getType().isAir()) {
                return;
            }
            BlockData newData = type.createBlockData();
            this.newData = newData;
            this.revertTime = System.currentTimeMillis() + revertTime;

            this.oldData = block.getBlockData().clone();

            block.setBlockData(newData);
            INSTANCES.put(block, this);
            REVERT_QUEUE.add(this);
        }
    }

    public TempBlock(Block block, BlockData newData, final long revertTime, boolean canReplaceBlocks) {
        if (block != null &&!INSTANCES.containsKey(block)) {
            this.block = block;
            this.loc = block.getLocation();

            if (!canReplaceBlocks && !block.getType().isAir()) {
                return;
            }
            this.newData = newData;
            this.revertTime = System.currentTimeMillis() + revertTime;

            this.oldData = block.getBlockData().clone();

            block.setBlockData(newData);
            INSTANCES.put(block, this);
            REVERT_QUEUE.add(this);
        }
    }


    public TempBlock(Block block, DisplayBlock blocks, final long revertTime) {

        boolean canReplaceBlocks = true;
        if (block != null &&!INSTANCES.containsKey(block)) {
            this.block = block;
            this.loc = block.getLocation();
            if (!canReplaceBlocks && !block.getType().isAir()) {
                return;
            }
            int randomIndex = ThreadLocalRandom.current().nextInt(blocks.getBlocks().size());
            BlockData newData = blocks.getBlocks().get(randomIndex).createBlockData();
            this.newData = newData;
            this.revertTime = System.currentTimeMillis() + revertTime;

            this.oldData = block.getBlockData().clone();

            block.setBlockData(newData);
            INSTANCES.put(block, this);
            REVERT_QUEUE.add(this);
        }    }

    public static boolean isTempBlock(Block block) {
        if (INSTANCES.containsKey(block)) {
            return true;
        }
        return false;
    }

    public static TempBlock getTempBlock(Block block) {
        return INSTANCES.get(block);
    }

    public void automaticRevert() {

        REVERT_QUEUE.remove();
        if (INSTANCES.containsKey(block)) {
            if (block.getBlockData() != oldData) {
                this.block.setBlockData(oldData);
            }
            INSTANCES.remove(block, this);
        }
    }

    public void revert() {
        if (oldData != null) {
            this.block.setBlockData(oldData);
        }
        if (block != null) {
            INSTANCES.remove(block, this);
        }
    }

    public long getRevertTime() {
        return revertTime;
    }

    public Block getBlock() {
        return block;
    }

    public Location getLoc(){
        return loc;
    }
}
