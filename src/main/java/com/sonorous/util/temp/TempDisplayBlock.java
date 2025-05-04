package com.sonorous.util.temp;

import com.sonorous.SonorousAbilities;
import com.sonorous.archetypes.DisplayBlock;
import com.sonorous.util.methods.Vectors;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftBlockDisplay;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.*;

/**
 * @author Sakrajin
 * Represents temporary display blocks (which are entiites) and handles reverting
 */
public class TempDisplayBlock {
    private static final PriorityQueue<TempDisplayBlock> REVERT_QUEUE = new PriorityQueue<>(100, (t1, t2) -> (int) (t1.revertTime - t2.revertTime));

    private static final Set<TempDisplayBlock> TEMP_DISPLAY_BLOCK_SET = new HashSet<>();
    public static Random random = new Random();
    private final BlockDisplay blockDisplay;
    private long revertTime;
    private double size;

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
            transformation.getScale().set(size - 0.001);
            bDisplay.setViewRange(100);
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
            transformation.getTranslation().set(-size / 2, -size / 2, -size / 2);
            //transformation.getTranslation().set(new Vector3d(-Math.cos(Math.toRadians(yaw))*size -size/2, -size/2,-Math.sin(Math.toRadians(yaw)*size) - size/2));
            transformation.getScale().set(size - 0.001);
            bDisplay.setViewRange(100);
            //transformation.getLeftRotation().set(new AxisAngle4d(Math.toRadians(yaw), 0, 1, 0));
            bDisplay.setTransformation(transformation);

        });
        this.size = size;
        this.revertTime = System.currentTimeMillis() + revertTime;
        REVERT_QUEUE.add(this);
        TEMP_DISPLAY_BLOCK_SET.add(this);
    }

    public TempDisplayBlock(Block block, Material type, final long revertTime, double size) {
        Location loc = block.getLocation().add(size / 2, size / 2, size / 2);
        this.blockDisplay = (BlockDisplay) loc.getWorld().spawn(loc, EntityType.BLOCK_DISPLAY.getEntityClass(), (entity) ->
        {
            BlockDisplay bDisplay = (BlockDisplay) entity;
            bDisplay.setBlock(type.createBlockData());
            Transformation transformation = bDisplay.getTransformation();
            transformation.getTranslation().set(-size / 2, -size / 2, -size / 2);
            //transformation.getTranslation().set(new Vector3d(-Math.cos(Math.toRadians(yaw))*size -size/2, -size/2,-Math.sin(Math.toRadians(yaw)*size) - size/2));
            transformation.getScale().set(size - 0.001);
            bDisplay.setViewRange(100);
            //transformation.getLeftRotation().set(new AxisAngle4d(Math.toRadians(yaw), 0, 1, 0));
            bDisplay.setTransformation(transformation);

        });
        this.size = size;
        this.revertTime = System.currentTimeMillis() + revertTime;
        REVERT_QUEUE.add(this);
        TEMP_DISPLAY_BLOCK_SET.add(this);
    }

    public TempDisplayBlock(Block block, DisplayBlock blocks, final long revertTime, double size) {
        Location loc = block.getLocation().add(size / 2, size / 2, size / 2);
        this.blockDisplay = (BlockDisplay) loc.getWorld().spawn(loc, EntityType.BLOCK_DISPLAY.getEntityClass(), (entity) ->
        {
            BlockDisplay bDisplay = (BlockDisplay) entity;
            SplittableRandom splittableRandom = new SplittableRandom();
            int randomIndex = splittableRandom.nextInt(0, blocks.getBlocks().size());
            BlockData newData = blocks.getBlocks().get(randomIndex).createBlockData();
            bDisplay.setBlock(newData);
            Transformation transformation = bDisplay.getTransformation();
            transformation.getTranslation().set(-size / 2, -size / 2, -size / 2);
            //transformation.getTranslation().set(new Vector3d(-Math.cos(Math.toRadians(yaw))*size -size/2, -size/2,-Math.sin(Math.toRadians(yaw)*size) - size/2));
            transformation.getScale().set(size - 0.001);
            bDisplay.setViewRange(100);
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
            transformation.getScale().set(size - 0.001);
            bDisplay.setViewRange(100);
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
            transformation.getScale().set(size - 0.001);
            bDisplay.setViewRange(100);
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

    public static Set<TempDisplayBlock> getTempDisplayBlockSet() {
        return TEMP_DISPLAY_BLOCK_SET;
    }

    public static PriorityQueue<TempDisplayBlock> getRevertQueue() {
        return REVERT_QUEUE;
    }

    public double getSize() {
        return size;
    }

    public void setSize(float size) {
        Transformation transformation = blockDisplay.getTransformation();
        transformation.getScale().set(size);
        this.size = size;
        blockDisplay.setTransformation(transformation);
    }

    public void setScale(double scale) {
        Transformation transformation = blockDisplay.getTransformation();
        Vector3f currentSize = transformation.getScale();
        Vector3f newSize = currentSize.mul((float) scale);
        if (newSize.length() > 0.1) {
            transformation.getScale().set(newSize);
            size = newSize.length();
        } else {
            transformation.getScale().set(currentSize.normalize(0.1F));
            size = 0.1;

        }
        blockDisplay.setTransformation(transformation);
    }

    public void automaticRevert() {
        if (blockDisplay != null) {
            blockDisplay.remove();
        }
        REVERT_QUEUE.remove();
    }

    public void revert() {
        blockDisplay.remove();
    }

    public long getRevertTime() {
        return revertTime;
    }

    public void setRevertTime(long newRevertTime) {
        revertTime = newRevertTime;
    }

    public void moveTo(Location newLoc) {
        //this.blockDisplay.teleport(newLoc);

        try {
            newLoc.checkFinite();
            Vector diff = Vectors.getDirectionBetweenLocations(blockDisplay.getLocation(), newLoc);

            ((CraftBlockDisplay) blockDisplay).getHandle().move(MoverType.SELF, new Vec3(diff.getX(), diff.getY(), diff.getZ()));
            ((CraftBlockDisplay) blockDisplay).getHandle().setRot(newLoc.getYaw(), newLoc.getPitch());

            Location loc = newLoc.clone();
            loc.setPitch(((CraftBlockDisplay) blockDisplay).getPitch());
            loc.setYaw(((CraftBlockDisplay) blockDisplay).getYaw());

            Vector left = Vectors.getLeftSideNormalisedVector(loc);
            Vector right = Vectors.getRightSideNormalisedVector(loc);

            Vector forward = new Vector(0, 0, 1).rotateAroundX(-Math.toRadians(loc.getPitch())).rotateAroundY(-Math.toRadians(((CraftBlockDisplay) blockDisplay).getYaw()));
//            Vector left = forward.clone().rotateAroundY(Math.toRadians(90));
//            Vector right = forward.clone().rotateAroundY(Math.toRadians(-90));


            //bottom left
            Location closeBottomLeft = loc.clone().add(Vectors.getDown(loc, size / 2)).add(left.clone().multiply(size / 2)).subtract(forward.clone().multiply(size / 2));
            Location farBottomLeft = loc.clone().add(Vectors.getDown(loc, size / 2)).add(left.clone().multiply(size / 2)).add(forward.clone().multiply(size / 2));
            Location closeBottomRight = loc.clone().add(Vectors.getDown(loc, size / 2)).add(right.clone().multiply(size / 2)).subtract(forward.clone().multiply(size / 2));
            Location farBottomRight = loc.clone().add(Vectors.getDown(loc, size / 2)).add(right.clone().multiply(size / 2)).add(forward.clone().multiply(size / 2));

            // top right
            Location closeTopRight = loc.clone().add(Vectors.getUp(loc, size / 2)).add(right.clone().multiply(size / 2)).subtract(forward.clone().multiply(size / 2));
            Location farTopRight = loc.clone().add(Vectors.getUp(loc, size / 2)).add(right.clone().multiply(size / 2)).add(forward.clone().multiply(size / 2));
            Location closeTopLeft = loc.clone().add(Vectors.getUp(loc, size / 2)).add(left.clone().multiply(size / 2)).subtract(forward.clone().multiply(size / 2));
            Location farTopLeft = loc.clone().add(Vectors.getUp(loc, size / 2)).add(left.clone().multiply(size / 2)).add(forward.clone().multiply(size / 2));

            List<Location> locs = List.of(closeBottomLeft, farBottomLeft, closeBottomRight, farBottomRight, closeTopRight, farTopRight, closeTopLeft, farTopLeft);

//            Location maxLoc =  boundingBox.getMax().toLocation(blockDisplay.getWorld());
//            Location minLoc = boundingBox.getMin().toLocation(blockDisplay.getWorld());

            if (!isInvisible() && !this.getBlockDisplay().isGlowing() && isLocSolid(locs)) {
                this.setInvisible();
            } else if (this.isInvisible() && !isLocSolid(locs)) {
                setVisible();
            }

        } catch (IllegalArgumentException exception) {
            SonorousAbilities.getPlugin().getLogger().warning("Block display new location invalid");
        }


    }

    private boolean isLocSolid(List<Location> locs) {
        return locs.stream().anyMatch(location -> location.getBlock().getType().isSolid());
    }

    public void moveToAndMaintainFacing(Location newLoc) {
//        ((CraftBlockDisplay) blockDisplay).getHandle().moveTo(newLoc.getX(), newLoc.getY(), newLoc.getZ(), ((CraftBlockDisplay) blockDisplay).getYaw(), ((CraftBlockDisplay) blockDisplay).getPitch());

//        blockDisplay.setTeleportDuration(0);
//        blockDisplay.teleport(newLoc);

//        ((CraftBlockDisplay) blockDisplay).getHandle().noPhysics = false;
        try {
            newLoc.checkFinite();
            Vector diff = Vectors.getDirectionBetweenLocations(blockDisplay.getLocation(), newLoc);

            ((CraftBlockDisplay) blockDisplay).getHandle().move(MoverType.SELF, new Vec3(diff.getX(), diff.getY(), diff.getZ()));
            ((CraftBlockDisplay) blockDisplay).getHandle().setRot(((CraftBlockDisplay) blockDisplay).getYaw(), ((CraftBlockDisplay) blockDisplay).getPitch());

            Location loc = newLoc.clone();
            loc.setPitch(((CraftBlockDisplay) blockDisplay).getPitch());
            loc.setYaw(((CraftBlockDisplay) blockDisplay).getYaw());

            Vector left = Vectors.getLeftSideNormalisedVector(loc);
            Vector right = Vectors.getRightSideNormalisedVector(loc);

            Vector forward = new Vector(0, 0, 1).rotateAroundX(-Math.toRadians(((CraftBlockDisplay) blockDisplay).getPitch())).rotateAroundY(-Math.toRadians(((CraftBlockDisplay) blockDisplay).getYaw()));
//            Vector left = forward.clone().rotateAroundY(Math.toRadians(90));
//            Vector right = forward.clone().rotateAroundY(Math.toRadians(-90));


            //bottom left
            Location closeBottomLeft = loc.clone().add(Vectors.getDown(loc, size / 2)).add(left.clone().multiply(size / 2)).subtract(forward.clone().multiply(size / 2));
            Location farBottomLeft = loc.clone().add(Vectors.getDown(loc, size / 2)).add(left.clone().multiply(size / 2)).add(forward.clone().multiply(size / 2));
            Location closeBottomRight = loc.clone().add(Vectors.getDown(loc, size / 2)).add(right.clone().multiply(size / 2)).subtract(forward.clone().multiply(size / 2));
            Location farBottomRight = loc.clone().add(Vectors.getDown(loc, size / 2)).add(right.clone().multiply(size / 2)).add(forward.clone().multiply(size / 2));

            // top right
            Location closeTopRight = loc.clone().add(Vectors.getUp(loc, size / 2)).add(right.clone().multiply(size / 2)).subtract(forward.clone().multiply(size / 2));
            Location farTopRight = loc.clone().add(Vectors.getUp(loc, size / 2)).add(right.clone().multiply(size / 2)).add(forward.clone().multiply(size / 2));
            Location closeTopLeft = loc.clone().add(Vectors.getUp(loc, size / 2)).add(left.clone().multiply(size / 2)).subtract(forward.clone().multiply(size / 2));
            Location farTopLeft = loc.clone().add(Vectors.getUp(loc, size / 2)).add(left.clone().multiply(size / 2)).add(forward.clone().multiply(size / 2));

            List<Location> locs = List.of(closeBottomLeft, farBottomLeft, closeBottomRight, farBottomRight, closeTopRight, farTopRight, closeTopLeft, farTopLeft);

//            Location maxLoc =  boundingBox.getMax().toLocation(blockDisplay.getWorld());
//            Location minLoc = boundingBox.getMin().toLocation(blockDisplay.getWorld());

            if (!isInvisible() && !this.getBlockDisplay().isGlowing() && isLocSolid(locs)) {
                this.setInvisible();
            } else if (this.isInvisible() && !isLocSolid(locs)) {
                setVisible();
            }


        } catch (IllegalArgumentException exception) {
            SonorousAbilities.getPlugin().getLogger().warning("Block display new location invalid");
        }

//        ((CraftBlockDisplay) blockDisplay).getHandle().teleportTo(newLoc.getX(), newLoc.getY(), newLoc.getZ());
//        ((CraftBlockDisplay) blockDisplay).getHandle().setRot(((CraftBlockDisplay) blockDisplay).getYaw(), ((CraftBlockDisplay) blockDisplay).getPitch());
    }

    public void rotate(float yaw, float pitch) {
        ((CraftBlockDisplay) blockDisplay).getHandle().setRot(yaw, pitch);
    }

    public BlockDisplay getBlockDisplay() {
        return blockDisplay;
    }

    public void setVisible() {
        ((CraftBlockDisplay) blockDisplay).getHandle().setViewRange(100);
    }

    public void setInvisible() {
        ((CraftBlockDisplay) blockDisplay).getHandle().setViewRange(0);
    }

    private boolean isInvisible() {
        return ((CraftBlockDisplay) blockDisplay).getHandle().getViewRange() == 0;

    }

    public Location getLoc() {
        return blockDisplay.getLocation();
    }


}

