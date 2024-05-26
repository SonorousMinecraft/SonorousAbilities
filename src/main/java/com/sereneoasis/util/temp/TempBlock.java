package com.sereneoasis.util.temp;

import com.sereneoasis.archetypes.DisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TempBlock {

    public static final WeakHashMap<Block, PriorityQueue<TempBlock>> INSTANCES= new WeakHashMap<>();

    private static final HashMap<Block, BlockData> BLOCK_ORIGINAL_DATA_MAP = new HashMap<>();


    private long timeToRevert;

    private BlockData previousData;

    private Block block;

    public Block getBlock() {
        return block;
    }

    public TempBlock(Block block, Material newType, long revertTimeFromNow ) {
        timeToRevert = System.currentTimeMillis() + revertTimeFromNow;
        this.block = block;

        BlockData newData = newType.createBlockData();

        INSTANCES.computeIfPresent(block, (currentBlock, priorityQueue) -> {
            previousData = currentBlock.getBlockData();
            currentBlock.setBlockData(newData);

            priorityQueue.add(this);
            return priorityQueue;
        });
        INSTANCES.computeIfAbsent(block, (b -> {
            previousData = b.getBlockData();
            b.setBlockData(newData);
            PriorityQueue<TempBlock> newQueue = new PriorityQueue<>(100, (t1, t2) -> (int) (t1.timeToRevert - t2.timeToRevert));
            newQueue.add(this);
            BLOCK_ORIGINAL_DATA_MAP.put(b, newData);
            return newQueue;
        }));
    }

    public TempBlock(Block block, BlockData newData, long revertTimeFromNow ) {
        timeToRevert = System.currentTimeMillis() + revertTimeFromNow;
        this.block = block;


        INSTANCES.computeIfPresent(block, (currentBlock, priorityQueue) -> {
            previousData = currentBlock.getBlockData();
            currentBlock.setBlockData(newData);

            priorityQueue.add(this);
            return priorityQueue;
        });
        INSTANCES.computeIfAbsent(block, (b -> {
            previousData = b.getBlockData();
            b.setBlockData(newData);
            PriorityQueue<TempBlock> newQueue = new PriorityQueue<>(100, (t1, t2) -> (int) (t1.timeToRevert - t2.timeToRevert));
            newQueue.add(this);
            BLOCK_ORIGINAL_DATA_MAP.put(b, newData);
            return newQueue;
        }));

    }

    public TempBlock(Block block,  DisplayBlock displayBlock, long revertTimeFromNow) {
        timeToRevert = System.currentTimeMillis() + revertTimeFromNow;
        this.block = block;


        int randomIndex = ThreadLocalRandom.current().nextInt(displayBlock.getBlocks().size());
        BlockData newData = displayBlock.getBlocks().get(randomIndex).createBlockData();


        INSTANCES.computeIfPresent(block, (currentBlock, priorityQueue) -> {
            previousData = currentBlock.getBlockData();
            currentBlock.setBlockData(newData);

            priorityQueue.add(this);
            return priorityQueue;
        });
        INSTANCES.computeIfAbsent(block, (b -> {
            previousData = b.getBlockData();
            b.setBlockData(newData);
            PriorityQueue<TempBlock> newQueue = new PriorityQueue<>(100, (t1, t2) -> (int) (t1.timeToRevert - t2.timeToRevert));
            newQueue.add(this);
            BLOCK_ORIGINAL_DATA_MAP.put(b, b.getBlockData());

            return newQueue;
        }));

    }

    public void revert(){
        block.setBlockData(BLOCK_ORIGINAL_DATA_MAP.get(block));
        BLOCK_ORIGINAL_DATA_MAP.remove(block);
    }

    public static boolean isTempBlock(Block block){
        if (BLOCK_ORIGINAL_DATA_MAP.containsKey(block)){
            return true;
        }
        return false;
    }

    public static void checkBlocks(){
        INSTANCES.forEach((block, priorityQueue) -> {
            if (priorityQueue.peek() != null) {
                if (priorityQueue.peek().timeToRevert < System.currentTimeMillis()){
                    if (priorityQueue.size() == 1){
                        block.setBlockData(BLOCK_ORIGINAL_DATA_MAP.get(block));
                        BLOCK_ORIGINAL_DATA_MAP.remove(block);
                        priorityQueue.remove();
                    } else {
                        block.setBlockData(priorityQueue.peek().previousData);
                        priorityQueue.remove();
                    }
                }
            } else {
//                Bukkit.broadcastMessage("test");
            }
        });
    }
}
