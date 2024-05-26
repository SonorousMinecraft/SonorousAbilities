package com.sereneoasis.util.temp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.*;

public class TB {

    public static final WeakHashMap<Block, PriorityQueue<TB>> INSTANCES= new WeakHashMap<>();

    private static final Set<Block> currentBlocks = new HashSet<>();


    private long timeToRevert;

    private BlockData originalData, previousData;


    public TB(Block block, long revertTimeFromNow, Material newType) {
        timeToRevert = System.currentTimeMillis() + revertTimeFromNow;

        BlockData newData = newType.createBlockData();

        INSTANCES.computeIfPresent(block, (currentBlock, priorityQueue) -> {
            originalData = priorityQueue.peek().originalData;
            previousData = currentBlock.getBlockData();
            currentBlock.setBlockData(newData);

            priorityQueue.add(this);
            return priorityQueue;
        });
        INSTANCES.computeIfAbsent(block, (b -> {
            originalData = b.getBlockData();
            previousData = b.getBlockData();
            b.setBlockData(newData);
            PriorityQueue<TB> newQueue = new PriorityQueue<>(100, (t1, t2) -> (int) (t1.timeToRevert - t2.timeToRevert));
            newQueue.add(this);
            return newQueue;
        }));

        currentBlocks.add(block);
    }

    public static void checkBlocks(){
        INSTANCES.forEach((block, priorityQueue) -> {
            if (priorityQueue.peek() != null) {
                if (priorityQueue.peek().timeToRevert < System.currentTimeMillis()){
                    if (priorityQueue.size() == 1){
                        block.setBlockData(priorityQueue.peek().originalData);
                        currentBlocks.remove(block);
                    } else {
                        block.setBlockData(priorityQueue.peek().previousData);
                    }
                }
            } else {
//                Bukkit.broadcastMessage("test");
            }
        });
    }
}
