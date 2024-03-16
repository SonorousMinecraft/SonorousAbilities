package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.temp.TempBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class RaiseBlockPillar extends CoreAbility {

    private final String name;
    private Location origin, loc;

    private double currentHeight;
    private double height;
    private List<TempDisplayBlock> blocks = new ArrayList<>();

    private List<TempBlock> solidBlocks = new ArrayList<>();

    private boolean isFalling = false;

    public RaiseBlockPillar(Player player, String name, double height) {
        super(player, name);

        this.name = name;
        this.height = height;
        currentHeight = height;
        abilityStatus = AbilityStatus.NO_SOURCE;
        Block source = Blocks.getFacingBlock(player, sourceRange);
        if (source != null && Blocks.getArchetypeBlocks(sPlayer).contains(source.getType())) {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            this.origin = source.getLocation();
            Blocks.selectSourceAnimationBlock(source, Color.GREEN);
            this.loc = origin.clone();
            while (Blocks.getArchetypeBlocks(sPlayer).contains(loc.getBlock().getType()) && currentHeight > 0) {
                TempDisplayBlock displayBlock = new TempDisplayBlock(loc, loc.getBlock().getType(), 60000, 1);
                blocks.add(displayBlock);
                currentHeight--;
                loc.subtract(0, 1, 0);
            }
            this.height = height - currentHeight;
            currentHeight = 0;
            start();
        }

    }

    public RaiseBlockPillar(Player player, String name, double height, Block targetBlock) {
        super(player, name);

        this.name = name;
        this.height = height;
        currentHeight = height;
        abilityStatus = AbilityStatus.NO_SOURCE;
        if (targetBlock != null && Blocks.getArchetypeBlocks(sPlayer).contains(targetBlock.getType())) {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            this.origin = targetBlock.getLocation();
            Blocks.selectSourceAnimationBlock(targetBlock, Color.GREEN);
            this.loc = origin.clone();
            while (Blocks.getArchetypeBlocks(sPlayer).contains(loc.getBlock().getType()) && currentHeight > 0) {
                TempDisplayBlock displayBlock = new TempDisplayBlock(loc, loc.getBlock().getType(), 60000, 1);
                blocks.add(displayBlock);
                currentHeight--;
                loc.subtract(0, 1, 0);
            }
            this.height = height - currentHeight;
            currentHeight = 0;
            start();
        }

    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (abilityStatus != AbilityStatus.COMPLETE && !isFalling) {
            if (currentHeight >= height+0.1*speed) {
                for (TempDisplayBlock tdb : blocks) {
                    solidBlocks.add(new TempBlock(tdb.getBlockDisplay().getLocation().getBlock(), tdb.getBlockDisplay().getBlock().getMaterial(), duration, true));
                    tdb.revert();
                }
                abilityStatus = AbilityStatus.COMPLETE;
            } else {
                for (TempDisplayBlock tdb : blocks) {
                    tdb.teleport(tdb.getBlockDisplay().getLocation().add(0, 0.1 * speed, 0));
                }
                currentHeight += 0.1 * speed;

                Entities.getEntitiesAroundPoint(origin.clone().add(0,currentHeight,0), hitbox).forEach(entity -> entity.setVelocity(new Vector(0, 0.1 * speed, 0)));
            }
        }

        if (isFalling && currentHeight > 0 && abilityStatus !=AbilityStatus.DROPPED) {
            for (TempDisplayBlock tdb : blocks) {
                tdb.teleport(tdb.getBlockDisplay().getLocation().subtract(0, 0.1 * speed, 0));
            }
            currentHeight -= 0.1 * speed;
        }
        if (isFalling && currentHeight <= 0){
            abilityStatus = AbilityStatus.DROPPED;
        }
    }

    public void drop(){
        if (!isFalling) {
            isFalling = true;
            solidBlocks.removeIf(tempBlock -> tempBlock.getBlock() == null);
            for (TempBlock b : solidBlocks) {
                TempDisplayBlock displayBlock = new TempDisplayBlock(b.getLoc(), b.getBlock().getType(), 60000, 1);
                blocks.add(displayBlock);
                b.revert();
            }
        }
    }


    public void revertAllTempDisplayBlocks(){
        for (TempDisplayBlock tdb : blocks) {
            tdb.revert();
        }
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return name;
    }
}
