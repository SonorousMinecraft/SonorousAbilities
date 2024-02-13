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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sakrajin
 */
public class SolarFlare extends CoreAbility {

    private long startTime = System.currentTimeMillis();

    private boolean isDayTime;

    private boolean started = false;

    private Block target;

    private Location flareLoc;


    private HashMap<Integer, TempDisplayBlock> flares = new HashMap<>();

    public SolarFlare(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        abilityStatus = AbilityStatus.NO_SOURCE;
        target = Blocks.getFacingBlockOrLiquid(player, sourceRange);
        if (target != null && target.getType().isSolid()) {
            Blocks.selectSourceAnimation(Blocks.getFacingBlockOrLiquidLoc(player, sourceRange).clone().subtract(0,size,0), sPlayer.getColor(), size);
            long time = player.getWorld().getTime();
            if (time < 167 || (time > 1200 && time < 22300)) {
                flareLoc = target.getLocation().clone().subtract(0, 10, 0);
                isDayTime = false;
            } else {
                flareLoc = target.getLocation().clone().add(0, 10, 0);
                isDayTime = true;
            }
            Set<Location> locs = new HashSet<>();
            for (Location b : Locations.getOutsideSphereLocs(flareLoc, radius, size)) {
                if ((int)b.getY() == flareLoc.getY()) {
                    locs.add(b);
                }
            }
            flares = Entities.handleDisplayBlockEntities(flares, locs, DisplayBlock.SUN, size);
            startTime = System.currentTimeMillis();
            start();
        }
    }

    @Override
    public void progress() {

        if (!started && System.currentTimeMillis() > startTime + chargeTime) {
            started = true;
        }


        if (started) {


            if (isDayTime) {
                flareLoc.subtract(0, 1, 0);
                if (flareLoc.getY() <= target.getY()) {
                    this.remove();
                }
            } else {
                flareLoc.add(0, 1, 0);
                if (flareLoc.getY() >= target.getY()) {
                    this.remove();
                }
            }

            Set<Location> locs = new HashSet<>();
            for (Location b : Locations.getOutsideSphereLocs(flareLoc, radius, size)) {
                if ((int)b.getY() == flareLoc.getY()) {
                    locs.add(b);
                }
            }

            Entities.handleDisplayBlockEntities(flares, locs, DisplayBlock.SUN, size);
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
