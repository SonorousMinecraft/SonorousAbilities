package com.sonorous.util.temp;

import com.sonorous.archetypes.DisplayBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class TempBlock {

    public static final HashMap<Block, PriorityQueue<TempBlock>> INSTANCES = new HashMap<>();

    private static final HashMap<Block, BlockData> BLOCK_ORIGINAL_DATA_MAP = new HashMap<>();


    private long timeToRevert;

    private BlockData previousData;

    private Block block;


    public TempBlock(Block block, Material newType, long revertTimeFromNow) {
        timeToRevert = System.currentTimeMillis() + revertTimeFromNow;
        this.block = block;

        BlockData newData = newType.createBlockData();

        INSTANCES.computeIfPresent(block, (currentBlock, priorityQueue) -> {
            previousData = currentBlock.getBlockData().clone();
            currentBlock.setBlockData(newData);

            priorityQueue.add(this);
            return priorityQueue;
        });
        INSTANCES.computeIfAbsent(block, (b -> {
            previousData = block.getBlockData().clone();
            block.setBlockData(newData);
            PriorityQueue<TempBlock> newQueue = new PriorityQueue<>(100, (t1, t2) -> (int) (t1.timeToRevert - t2.timeToRevert));
            newQueue.add(this);
            BLOCK_ORIGINAL_DATA_MAP.put(block, previousData);
            return newQueue;
        }));
    }

    public TempBlock(Block block, BlockData newData, long revertTimeFromNow) {
        timeToRevert = System.currentTimeMillis() + revertTimeFromNow;
        this.block = block;


        INSTANCES.computeIfPresent(block, (currentBlock, priorityQueue) -> {
            previousData = currentBlock.getBlockData().clone();
            currentBlock.setBlockData(newData);

            priorityQueue.add(this);
            return priorityQueue;
        });
        INSTANCES.computeIfAbsent(block, (b -> {
            previousData = block.getBlockData().clone();
            block.setBlockData(newData);
            PriorityQueue<TempBlock> newQueue = new PriorityQueue<>(100, (t1, t2) -> (int) (t1.timeToRevert - t2.timeToRevert));
            newQueue.add(this);
            BLOCK_ORIGINAL_DATA_MAP.put(block, previousData);
            return newQueue;
        }));

    }

    public TempBlock(Block block, DisplayBlock displayBlock, long revertTimeFromNow) {
        timeToRevert = System.currentTimeMillis() + revertTimeFromNow;
        this.block = block;


        int randomIndex = ThreadLocalRandom.current().nextInt(displayBlock.getBlocks().size());
        BlockData newData = displayBlock.getBlocks().get(randomIndex).createBlockData();


        INSTANCES.computeIfPresent(block, (currentBlock, priorityQueue) -> {
            previousData = currentBlock.getBlockData().clone();
            currentBlock.setBlockData(newData);

            priorityQueue.add(this);
            return priorityQueue;
        });
        INSTANCES.computeIfAbsent(block, (b -> {
            previousData = block.getBlockData().clone();
            block.setBlockData(newData);
            PriorityQueue<TempBlock> newQueue = new PriorityQueue<>(100, (t1, t2) -> (int) (t1.timeToRevert - t2.timeToRevert));
            newQueue.add(this);
            BLOCK_ORIGINAL_DATA_MAP.put(block, previousData);

            return newQueue;
        }));

    }

    public static boolean isTempBlock(Block block) {
        return BLOCK_ORIGINAL_DATA_MAP.containsKey(block);
    }

    public static void checkBlocks() {
        Set<Block> toRemove = new HashSet<>();
        INSTANCES.forEach((block, priorityQueue) -> {
            if (priorityQueue.peek() != null) {
                if (priorityQueue.peek().timeToRevert < System.currentTimeMillis()) {
                    if (priorityQueue.peek().previousData != null) {
                        block.setBlockData(priorityQueue.peek().previousData);
                        priorityQueue.poll();
                    } else {
                        priorityQueue.poll();
//                        Bukkit.broadcastMessage("shit is null");
                    }
                }
            } else {
                if (BLOCK_ORIGINAL_DATA_MAP.get(block) != null) {
                    toRemove.add(block);
                    block.setBlockData(BLOCK_ORIGINAL_DATA_MAP.get(block));
//                Bukkit.broadcastMessage("test");
                }
                BLOCK_ORIGINAL_DATA_MAP.remove(block);
            }
        });
        toRemove.forEach(removeBlock -> INSTANCES.remove(removeBlock));
    }

    public Block getBlock() {
        return block;
    }

    public void revert() {
        if (BLOCK_ORIGINAL_DATA_MAP.get(block) != null) {
            block.setBlockData(BLOCK_ORIGINAL_DATA_MAP.get(block));
        }
        BLOCK_ORIGINAL_DATA_MAP.remove(block);
        INSTANCES.remove(block);
    }
}
