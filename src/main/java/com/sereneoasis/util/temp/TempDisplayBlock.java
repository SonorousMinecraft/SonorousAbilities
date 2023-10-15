package com.sereneoasis.util.temp;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4d;
import org.joml.AxisAngle4f;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sakrajin
 * Represents temporary display blocks (which are entiites) and handles reverting
 */
public class TempDisplayBlock {
    private static final PriorityQueue<TempDisplayBlock> REVERT_QUEUE = new PriorityQueue<>(100, (t1, t2) -> (int) (t1.revertTime - t2.revertTime));

    public static PriorityQueue<TempDisplayBlock> getRevertQueue()
    {
        return REVERT_QUEUE;
    }

    private BlockDisplay blockDisplay;

    private long revertTime;

    public TempDisplayBlock(Location loc, BlockData newData, final long revertTime, double size) {

        blockDisplay = (BlockDisplay) loc.getWorld().spawn(loc, EntityType.BLOCK_DISPLAY.getEntityClass(), (entity) ->
        {
            BlockDisplay bDisplay = (BlockDisplay)entity;

            bDisplay.setBlock(newData);
            Transformation transformation = bDisplay.getTransformation();
            //transformation.getTranslation().set(new Vector3d(-size/2, -size/2,- size/2));
            //transformation.getTranslation().set(new Vector3d(-Math.cos(Math.toRadians(yaw))*size -size/2, -size/2,-Math.sin(Math.toRadians(yaw)*size) - size/2));
            transformation.getScale().set(size);
            bDisplay.setViewRange(100);
            //transformation.getLeftRotation().set(new AxisAngle4d(Math.toRadians(yaw), 0, 1, 0));
            bDisplay.setGlowing(true);
            bDisplay.setTransformation(transformation);

        });
        this.revertTime = System.currentTimeMillis() + revertTime;
        REVERT_QUEUE.add(this);
    }


    public void revertTempDisplayBlock()
    {
        blockDisplay.remove();
        REVERT_QUEUE.remove();
    }

    public long getRevertTime() {
        return revertTime;
    }
}

