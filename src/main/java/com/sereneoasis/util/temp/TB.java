package com.sereneoasis.util.temp;

import com.sereneoasis.archetypes.DisplayBlock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TB {

    public static final WeakHashMap<Block, PriorityQueue<TB>> INSTANCES= new WeakHashMap<>();

    private static final HashMap<Block, BlockData> BLOCK_ORIGINAL_DATA_MAP = new HashMap<>();


    private long timeToRevert;

    private BlockData previousData;


    public TB(Block block, long revertTimeFromNow, Material newType) {
        timeToRevert = System.currentTimeMillis() + revertTimeFromNow;

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
            PriorityQueue<TB> newQueue = new PriorityQueue<>(100, (t1, t2) -> (int) (t1.timeToRevert - t2.timeToRevert));
            newQueue.add(this);
            BLOCK_ORIGINAL_DATA_MAP.put(b, b.getBlockData());
            return newQueue;
        }));

    }

    public TB(Block block, long revertTimeFromNow, DisplayBlock displayBlock) {
        timeToRevert = System.currentTimeMillis() + revertTimeFromNow;

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
            PriorityQueue<TB> newQueue = new PriorityQueue<>(100, (t1, t2) -> (int) (t1.timeToRevert - t2.timeToRevert));
            newQueue.add(this);
            BLOCK_ORIGINAL_DATA_MAP.put(b, b.getBlockData());

            return newQueue;
        }));

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
