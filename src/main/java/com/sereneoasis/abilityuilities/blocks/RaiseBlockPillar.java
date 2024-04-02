package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Constants;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.temp.TempBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class RaiseBlockPillar extends CoreAbility {

    protected final String name;
    protected Location origin, loc;

    protected double currentHeight;
    protected double height;
    protected List<TempDisplayBlock> blocks = new ArrayList<>();

    protected Set<Block> solidifiedBlocks = new HashSet<>();
    protected List<TempBlock> solidBlocks = new ArrayList<>();

    protected boolean isFalling = false;

    public RaiseBlockPillar(Player player, String name, double height) {
        super(player, name);

        this.name = name;
        this.height = height;
        currentHeight = height + 1;
        abilityStatus = AbilityStatus.NO_SOURCE;
        Block source = Blocks.getFacingBlock(player, sourceRange);
        if (source != null && Blocks.getArchetypeBlocks(sPlayer).contains(source.getType())) {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            this.origin = source.getLocation();
//            Blocks.selectSourceAnimationBlock(source, Color.GREEN);
            this.loc = origin.clone();

            while (Blocks.getArchetypeBlocks(sPlayer).contains(loc.getBlock().getType()) && currentHeight > 0) {
                TempDisplayBlock displayBlock = new TempDisplayBlock(loc.getBlock(), loc.getBlock().getType(), 60000, 1 );
                blocks.add(displayBlock);
                currentHeight--;
                loc.subtract(0, 1, 0);
            }
            Collections.reverse(blocks);

            this.height = height - currentHeight;
            currentHeight = 0;
            start();
        }

    }

    public RaiseBlockPillar(Player player, String name, double height, Block targetBlock) {
        super(player, name);

        this.name = name;
        this.height = height;
        currentHeight = height + 1;
        abilityStatus = AbilityStatus.NO_SOURCE;
        if (targetBlock != null && Blocks.getArchetypeBlocks(sPlayer).contains(targetBlock.getType())) {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            this.origin = targetBlock.getLocation();
           // Blocks.selectSourceAnimationBlock(targetBlock, Color.GREEN);
            this.loc = origin.clone();
            while (Blocks.getArchetypeBlocks(sPlayer).contains(loc.getBlock().getType()) && currentHeight > 0) {
                TempDisplayBlock displayBlock = new TempDisplayBlock(loc.getBlock(), loc.getBlock().getType(), 60000, 1 );
                blocks.add(displayBlock);
                currentHeight--;
                loc.subtract(0, 1, 0);
            }
            Collections.reverse(blocks);
            this.height = height - currentHeight;
            currentHeight = 0;
            start();
        }

    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (abilityStatus != AbilityStatus.COMPLETE && !isFalling) {
            if (currentHeight - size > height) {

                abilityStatus = AbilityStatus.COMPLETE;
                revertAllTempDisplayBlocks();
            } else {
                for (TempDisplayBlock tdb : blocks) {
                    tdb.moveTo(tdb.getBlockDisplay().getLocation().add(0, Constants.BLOCK_RAISE_SPEED * speed, 0));
                }

                Block topBlock = origin.clone().add(0,currentHeight,0).getBlock();
                if (! solidifiedBlocks.contains(topBlock)) {
                    if (currentHeight >= 1) {
                        TempDisplayBlock tdb = blocks.get(((int) currentHeight) -1);
                        solidBlocks.add(new TempBlock(topBlock, tdb.getBlockDisplay().getBlock().getMaterial(), duration, false));
                        solidifiedBlocks.add(topBlock);
                    }

                }

                currentHeight += Constants.BLOCK_RAISE_SPEED * speed;

                Entities.getEntitiesAroundPoint(origin.clone().add(0,currentHeight,0), hitbox).forEach(entity -> entity.setVelocity(new Vector(0, Constants.BLOCK_RAISE_SPEED * speed, 0)));
            }
        }

        if (isFalling && currentHeight > 0 && abilityStatus !=AbilityStatus.DROPPED) {
            for (TempDisplayBlock tdb : blocks) {
                tdb.moveTo(tdb.getBlockDisplay().getLocation().subtract(0, Constants.BLOCK_RAISE_SPEED * speed, 0));
            }
            currentHeight -= Constants.BLOCK_RAISE_SPEED * speed;
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
    public void remove() {
        super.remove();
        revertAllTempDisplayBlocks();
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
