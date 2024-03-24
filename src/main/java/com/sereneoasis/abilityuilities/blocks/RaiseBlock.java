package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Constants;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class RaiseBlock extends CoreAbility {

    private final String name;
    private Location origin, loc;

    private double height;
    private TempDisplayBlock block;

    private boolean isFalling = false;

    private double speedChange ;

    public RaiseBlock(Player player, String name, double height, boolean mustLook, boolean playGlowing) {
        super(player, name);

        this.name = name;
        this.height = height;
        speedChange = Math.sqrt(2 * 2 * Constants.GRAVITY);
        abilityStatus = AbilityStatus.NO_SOURCE;
        Block source;
        if (mustLook) {
            source = Blocks.getFacingBlock(player, sourceRange);
            if (source != null && Blocks.getArchetypeBlocks(sPlayer).contains(source.getType())) {
                abilityStatus = AbilityStatus.SOURCE_SELECTED;

                this.origin = Blocks.getFacingBlockLoc(player, sourceRange);
                if (playGlowing) {
                    Blocks.selectSourceAnimation(origin.clone().subtract(0, size, 0), Color.GREEN, size);
                }
                this.loc = origin.clone();
                block = new TempDisplayBlock(loc.clone(), source.getType(), 60000, size);
                start();
            }
        } else {
            Set<Block> possibleSources = Blocks.getBlocksAroundPoint(player.getLocation(), sourceRange).stream()
                    .filter(Blocks::isTopBlock)
                    .filter(b -> Blocks.getArchetypeBlocks(sPlayer).contains(b.getType()))
                    .collect(Collectors.toSet());
            List<Block> sourcesByDistance = new java.util.ArrayList<>(possibleSources.stream().toList());
            sourcesByDistance.sort(Comparator.comparing(b -> b.getLocation().distanceSquared(player.getLocation())));
            if (!sourcesByDistance.isEmpty()) {
                Random rand = new Random();
                source = sourcesByDistance.get(rand.nextInt(sourcesByDistance.size()));
                abilityStatus = AbilityStatus.SOURCE_SELECTED;
                this.origin = source.getLocation();
                if (playGlowing) {
                    Blocks.selectSourceAnimation(origin.clone().subtract(0, size, 0), Color.GREEN, size);
                }
                this.loc = origin.clone();
                block = new TempDisplayBlock(loc.clone(), source.getType(), 60000, size);
                start();
            }
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (abilityStatus != AbilityStatus.SOURCED || isFalling) {
            if (!isFalling) {
                if (loc.getY() - origin.getY() < height) {
                    loc.add(new Vector(0, speedChange, 0));
                    block.moveToAndMaintainFacing(loc.clone());
                    speedChange -= Constants.GRAVITY;
                    if (speedChange < Constants.GRAVITY){
                        speedChange = Constants.GRAVITY;
                    }
                } else {
                    abilityStatus = AbilityStatus.SOURCED;
                }
            } else {
                if (loc.getY() - origin.getY() > size/2) {
                    loc.subtract(new Vector(0, speedChange , 0));
                    block.moveToAndMaintainFacing(loc.clone());
                    speedChange += Constants.GRAVITY;
                } else {
                    abilityStatus = AbilityStatus.COMPLETE;
                }
            }
        }
    }

    public BlockDisplay getBlockEntity() {
        return block.getBlockDisplay();
    }

    public TempDisplayBlock getBlock() {
        return block;
    }

    public void fall(){
        isFalling = true;
        speedChange = 0;
    }

    @Override
    public void remove() {
        super.remove();
        block.revert();
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
