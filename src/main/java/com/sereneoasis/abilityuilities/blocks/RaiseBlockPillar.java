package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.temp.TempBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Bukkit;
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


    public RaiseBlockPillar(Player player, String name, double height) {
        super(player, name);

        this.name = name;
        this.height = height;
        currentHeight = height;
        abilityStatus = AbilityStatus.NO_SOURCE;
        Block source = Blocks.getFacingBlock(player, sourceRange);
        if (source != null && Blocks.getArchetypeBlocks(sPlayer).contains(source.getType())) {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            Blocks.selectSourceAnimation(source, Color.GREEN);
            this.origin = source.getLocation();
            this.loc = origin.clone();
            while (Blocks.getArchetypeBlocks(sPlayer).contains(loc.getBlock().getType()) && currentHeight > 0) {
                TempDisplayBlock displayBlock = new TempDisplayBlock(loc, loc.getBlock().getType(), 60000, 1);
                blocks.add(displayBlock);
                currentHeight--;
                loc.subtract(0, 1, 0);
            }
            this.height = height - currentHeight;
            currentHeight = 0;
            Bukkit.broadcastMessage(String.valueOf(blocks));
            start();
        }

    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (abilityStatus != AbilityStatus.COMPLETE)
        {
            if (currentHeight >= height)
            {
                for (TempDisplayBlock tdb : blocks)
                {
                    new TempBlock(tdb.getBlockDisplay().getLocation().getBlock(), tdb.getBlockDisplay().getBlock().getMaterial(), duration, true);
                    tdb.revert();
                }
                abilityStatus = AbilityStatus.COMPLETE;
            }
            else{
                for (TempDisplayBlock tdb : blocks)
                {
                    tdb.teleport(tdb.getBlockDisplay().getLocation().add(0,0.1*speed,0));
                }
                currentHeight += 0.1*speed;
            }
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
