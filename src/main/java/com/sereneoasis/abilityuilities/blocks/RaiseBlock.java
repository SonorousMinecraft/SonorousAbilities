package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RaiseBlock extends CoreAbility {

    private final String name;
    private Location origin, loc;

    private double height;
    private TempDisplayBlock block;

    private Vector offsetAdjustment = new Vector(-0.5,-0.5,-0.5);

    public RaiseBlock(Player player, String name, double height) {
        super(player, name);

        this.name = name;
        this.height = height;
        abilityStatus = AbilityStatus.NO_SOURCE;
        Block source = Blocks.getFacingBlock(player, sourceRange);
        if (source != null && Blocks.getArchetypeBlocks(sPlayer).contains(source.getType()))
        {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            Blocks.selectSourceAnimation(source, Color.GREEN);
            this.origin = Blocks.getFacingBlockLoc(player, sourceRange);
            this.loc = origin.clone();
            block = new TempDisplayBlock(loc.clone().add(offsetAdjustment), source.getType(), 60000, 1);
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (abilityStatus != AbilityStatus.SOURCED) {
            if (loc.getY()-origin.getY() < height) {
                loc.add(new Vector(0, 0.1, 0));
                block.teleport(loc.clone().add(offsetAdjustment));
            }else{
                abilityStatus = AbilityStatus.SOURCED;
            }
        }
    }

    public BlockDisplay getBlockEntity()
    {
        return block.getBlockDisplay();
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
