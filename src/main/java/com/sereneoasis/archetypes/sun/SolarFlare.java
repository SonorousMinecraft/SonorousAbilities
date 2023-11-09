package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Sakrajin
 *
 */
public class SolarFlare extends CoreAbility {

    private long startTime = System.currentTimeMillis();

    private boolean isDayTime;

    private boolean started = false;

    private Block target;

    private Location flareLoc;

    private AbilityStatus abilityStatus;

    private HashMap<Integer, TempDisplayBlock>flares = new HashMap<>();

    public SolarFlare(Player player) {
        super(player);

        abilityStatus = AbilityStatus.NO_SOURCE;
        target = Blocks.getFacingBlockOrLiquid(player, sourceRange);
        if (target != null && target.getType().isSolid()) {
            long time = player.getWorld().getTime();
            if (time < 12300 || time > 23850)
            {
                flareLoc = target.getLocation().clone().subtract(0,10,0);
                isDayTime = false;
            }
            else{
                flareLoc = target.getLocation().clone().add(0,10,0);
                isDayTime = true;
            }
            Set<Location> locs = new HashSet<>();
            for (Block block : Blocks.getBlocksAroundPoint(flareLoc, radius))
            {
                Location b = block.getLocation();
                if (b.getY() == flareLoc.getY())
                {
                    locs.add(b);
                }
            }
            flares = Entities.handleDisplayBlockEntities(flares,locs, DisplayBlock.SUN, 0.5);
            startTime = System.currentTimeMillis();
            start();
        }
    }

    @Override
    public void progress() {

        if (!started && System.currentTimeMillis() > startTime + chargetime)
        {
            started = true;
        }

        if (started)
        {


            if (isDayTime)
            {
                flareLoc.subtract(0,1,0);
            }
            else{
                flareLoc.add(0,1,0);
            }



            Set<Location> locs = new HashSet<>();
            for (Block block : Blocks.getBlocksAroundPoint(flareLoc, radius))
            {
                Location b = block.getLocation();
                if (b.getBlock() == flareLoc.getBlock())
                {
                    locs.add(b);
                }
            }

            flares = Entities.handleDisplayBlockEntities(flares,locs, DisplayBlock.SUN, 0.5);
        }
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown("SolarFlare", cooldown);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return "SolarFlare";
    }
}
