package com.sereneoasis.util.temp;

import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.methods.Vectors;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftBlockDisplay;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftBlockDisplay;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SplittableRandom;

/**
 * @author Sakrajin
 * Represents temporary display blocks (which are entiites) and handles reverting
 */
public class TempDisplayBlock {
    private static final PriorityQueue<TempDisplayBlock> REVERT_QUEUE = new PriorityQueue<>(100, (t1, t2) -> (int) (t1.revertTime - t2.revertTime));

    private static final Set<TempDisplayBlock> TEMP_DISPLAY_BLOCK_SET = new HashSet<>();

    public static Set<TempDisplayBlock> getTempDisplayBlockSet() { return TEMP_DISPLAY_BLOCK_SET;}

    public static PriorityQueue<TempDisplayBlock> getRevertQueue() {
        return REVERT_QUEUE;
    }

    private final BlockDisplay blockDisplay;

    private long revertTime;

    private double size;

    public double getSize() {
        return size;
    }

    public void setScale(double scale){
        Transformation transformation = blockDisplay.getTransformation();
        Vector3f currentSize = transformation.getScale();
        Vector3f newSize = currentSize.mul((float) scale);
        if (newSize.length() > 0.1){
            transformation.getScale().set(newSize);
            size = newSize.length();
        } else {
            transformation.getScale().set(currentSize.normalize(0.1F));
            size = 0.1;

        }
        blockDisplay.setTransformation(transformation);
    }

    public void setSize(float size){
        Transformation transformation = blockDisplay.getTransformation();
        transformation.getScale().set(size);
        this.size = size;
        blockDisplay.setTransformation(transformation);
    }

    public TempDisplayBlock(Location loc, DisplayBlock blocks, final long revertTime, double size) {

        this.blockDisplay = (BlockDisplay) loc.getWorld().spawn(loc, EntityType.BLOCK_DISPLAY.getEntityClass(), (entity) ->
        {
            BlockDisplay bDisplay = (BlockDisplay) entity;
            SplittableRandom splittableRandom = new SplittableRandom();
            int randomIndex = splittableRandom.nextInt(0, blocks.getBlocks().size());
            BlockData newData = blocks.getBlocks().get(randomIndex).createBlockData();
            bDisplay.setBlock(newData);
            Transformation transformation = bDisplay.getTransformation();
            transformation.getTranslation().set(new Vector3d(-size / 2, -size / 2, -size / 2));
            //transformation.getTranslation().set(new Vector3d(-Math.cos(Math.toRadians(yaw))*size -size/2, -size/2,-Math.sin(Math.toRadians(yaw)*size) - size/2));
            transformation.getScale().set(size-0.001);
            bDisplay.setViewRange(50);
            //transformation.getLeftRotation().set(new AxisAngle4d(Math.toRadians(yaw), 0, 1, 0));
            bDisplay.setTransformation(transformation);

        });
        this.size = size;
        this.revertTime = System.currentTimeMillis() + revertTime;
        REVERT_QUEUE.add(this);
        TEMP_DISPLAY_BLOCK_SET.add(this);
    }

    // Typically used in blocks
    public TempDisplayBlock(Location loc, Material block, final long revertTime, double size) {



        this.blockDisplay = (BlockDisplay) loc.getWorld().spawn(loc, EntityType.BLOCK_DISPLAY.getEntityClass(), (entity) ->
        {
            BlockDisplay bDisplay = (BlockDisplay) entity;
            bDisplay.setBlock(block.createBlockData());
            Transformation transformation = bDisplay.getTransformation();
            transformation.getTranslation().set(-size/2, -size/2, -size/2);
            //transformation.getTranslation().set(new Vector3d(-Math.cos(Math.toRadians(yaw))*size -size/2, -size/2,-Math.sin(Math.toRadians(yaw)*size) - size/2));
            transformation.getScale().set(size-0.001);
            bDisplay.setViewRange(50);
            //transformation.getLeftRotation().set(new AxisAngle4d(Math.toRadians(yaw), 0, 1, 0));
            bDisplay.setTransformation(transformation);

        });
        this.size = size;
        this.revertTime = System.currentTimeMillis() + revertTime;
        REVERT_QUEUE.add(this);
        TEMP_DISPLAY_BLOCK_SET.add(this);
    }

    public TempDisplayBlock(Block block, Material type, final long revertTime, double size) {
        Location loc = block.getLocation().add(size/2, size/2, size/2);
        this.blockDisplay = (BlockDisplay) loc.getWorld().spawn(loc, EntityType.BLOCK_DISPLAY.getEntityClass(), (entity) ->
        {
            BlockDisplay bDisplay = (BlockDisplay) entity;
            bDisplay.setBlock(type.createBlockData());
            Transformation transformation = bDisplay.getTransformation();
            transformation.getTranslation().set(-size/2, -size/2, -size/2);
            //transformation.getTranslation().set(new Vector3d(-Math.cos(Math.toRadians(yaw))*size -size/2, -size/2,-Math.sin(Math.toRadians(yaw)*size) - size/2));
            transformation.getScale().set(size-0.001);
            bDisplay.setViewRange(50);
            //transformation.getLeftRotation().set(new AxisAngle4d(Math.toRadians(yaw), 0, 1, 0));
            bDisplay.setTransformation(transformation);

        });
        this.size = size;
        this.revertTime = System.currentTimeMillis() + revertTime;
        REVERT_QUEUE.add(this);
        TEMP_DISPLAY_BLOCK_SET.add(this);
    }

    public TempDisplayBlock(Block block, DisplayBlock blocks, final long revertTime, double size) {
        Location loc = block.getLocation().add(size/2, size/2, size/2);
        this.blockDisplay = (BlockDisplay) loc.getWorld().spawn(loc, EntityType.BLOCK_DISPLAY.getEntityClass(), (entity) ->
        {
            BlockDisplay bDisplay = (BlockDisplay) entity;
            SplittableRandom splittableRandom = new SplittableRandom();
            int randomIndex = splittableRandom.nextInt(0, blocks.getBlocks().size());
            BlockData newData = blocks.getBlocks().get(randomIndex).createBlockData();
            bDisplay.setBlock(newData);
            Transformation transformation = bDisplay.getTransformation();
            transformation.getTranslation().set(-size/2, -size/2, -size/2);
            //transformation.getTranslation().set(new Vector3d(-Math.cos(Math.toRadians(yaw))*size -size/2, -size/2,-Math.sin(Math.toRadians(yaw)*size) - size/2));
            transformation.getScale().set(size-0.001);
            bDisplay.setViewRange(50);
            //transformation.getLeftRotation().set(new AxisAngle4d(Math.toRadians(yaw), 0, 1, 0));
            bDisplay.setTransformation(transformation);

        });
        this.size = size;
        this.revertTime = System.currentTimeMillis() + revertTime;
        REVERT_QUEUE.add(this);
        TEMP_DISPLAY_BLOCK_SET.add(this);
    }

    // Typically used in locations
    public TempDisplayBlock(Location loc, Material block, final long revertTime, double size, boolean glowing, Color color) {

        this.blockDisplay = (BlockDisplay) loc.getWorld().spawn(loc, EntityType.BLOCK_DISPLAY.getEntityClass(), (entity) ->
        {
            BlockDisplay bDisplay = (BlockDisplay) entity;
            SplittableRandom splittableRandom = new SplittableRandom();
            bDisplay.setBlock(block.createBlockData());
            Transformation transformation = bDisplay.getTransformation();
            //transformation.getTranslation().set(new Vector3d(-Math.cos(Math.toRadians(yaw))*size -size/2, -size/2,-Math.sin(Math.toRadians(yaw)*size) - size/2));
            transformation.getScale().set(size-0.001);
            bDisplay.setViewRange(50);
            //transformation.getLeftRotation().set(new AxisAngle4d(Math.toRadians(yaw), 0, 1, 0));
            bDisplay.setTransformation(transformation);
            if (glowing) {
                bDisplay.setGlowing(true);
                bDisplay.setGlowColorOverride(color);
            }

        });
        this.size = size;
        this.revertTime = System.currentTimeMillis() + revertTime;
        REVERT_QUEUE.add(this);
        TEMP_DISPLAY_BLOCK_SET.add(this);
    }


    // Typically used in locations
    public TempDisplayBlock(Location loc, DisplayBlock blocks, final long revertTime, double size, boolean glowing, Color color) {

        this.blockDisplay = (BlockDisplay) loc.getWorld().spawn(loc, EntityType.BLOCK_DISPLAY.getEntityClass(), (entity) ->
        {
            BlockDisplay bDisplay = (BlockDisplay) entity;
            SplittableRandom splittableRandom = new SplittableRandom();
            int randomIndex = splittableRandom.nextInt(0, blocks.getBlocks().size());
            BlockData newData = blocks.getBlocks().get(randomIndex).createBlockData();
            bDisplay.setBlock(newData);
            Transformation transformation = bDisplay.getTransformation();
            //transformation.getTranslation().set(new Vector3d(-Math.cos(Math.toRadians(yaw))*size -size/2, -size/2,-Math.sin(Math.toRadians(yaw)*size) - size/2));
            transformation.getScale().set(size-0.001);
            bDisplay.setViewRange(50);
            //transformation.getLeftRotation().set(new AxisAngle4d(Math.toRadians(yaw), 0, 1, 0));
            bDisplay.setTransformation(transformation);
            if (glowing) {
                bDisplay.setGlowing(true);
                bDisplay.setGlowColorOverride(color);
            }

        });
        this.size = size;
        this.revertTime = System.currentTimeMillis() + revertTime;
        REVERT_QUEUE.add(this);
        TEMP_DISPLAY_BLOCK_SET.add(this);
    }


    public void automaticRevert() {
        if (blockDisplay != null) {
            blockDisplay.remove();
        }
        REVERT_QUEUE.remove();
    }

    public void setRevertTime(long newRevertTime) {
        revertTime = newRevertTime;
    }

    public void revert() {
        blockDisplay.remove();
    }


    public long getRevertTime() {
        return revertTime;
    }

    public void moveTo(Location newLoc) {
        //this.blockDisplay.teleport(newLoc);

        Vector diff = Vectors.getDirectionBetweenLocations(blockDisplay.getLocation(), newLoc);

        ((CraftBlockDisplay) blockDisplay).getHandle().move(MoverType.SELF, new Vec3(diff.getX(), diff.getY(), diff.getZ()));
        ((CraftBlockDisplay) blockDisplay).getHandle().setRot(newLoc.getYaw(), newLoc.getPitch());
    }

    public void moveToAndMaintainFacing(Location newLoc){
//        ((CraftBlockDisplay) blockDisplay).getHandle().moveTo(newLoc.getX(), newLoc.getY(), newLoc.getZ(), ((CraftBlockDisplay) blockDisplay).getYaw(), ((CraftBlockDisplay) blockDisplay).getPitch());

//        blockDisplay.setTeleportDuration(0);
//        blockDisplay.teleport(newLoc);

//        ((CraftBlockDisplay) blockDisplay).getHandle().noPhysics = false;
        Vector diff = Vectors.getDirectionBetweenLocations(blockDisplay.getLocation(), newLoc);

        ((CraftBlockDisplay) blockDisplay).getHandle().move(MoverType.SELF, new Vec3(diff.getX(), diff.getY(), diff.getZ()));
        ((CraftBlockDisplay) blockDisplay).getHandle().setRot(((CraftBlockDisplay) blockDisplay).getYaw(), ((CraftBlockDisplay) blockDisplay).getPitch());

//        ((CraftBlockDisplay) blockDisplay).getHandle().teleportTo(newLoc.getX(), newLoc.getY(), newLoc.getZ());
//        ((CraftBlockDisplay) blockDisplay).getHandle().setRot(((CraftBlockDisplay) blockDisplay).getYaw(), ((CraftBlockDisplay) blockDisplay).getPitch());
    }

    public void rotate(float yaw, float pitch){
        ((CraftBlockDisplay) blockDisplay).getHandle().setRot(yaw, pitch);
    }
    public BlockDisplay getBlockDisplay() {
        return blockDisplay;
    }

    public void setVisible(){
        ((CraftBlockDisplay) blockDisplay).getHandle().setViewRange(30);
    }

    public void setInvisible(){
        ((CraftBlockDisplay) blockDisplay).getHandle().setViewRange(0);
    }

    public Location getLoc(){
        return  blockDisplay.getLocation();
    }



}

