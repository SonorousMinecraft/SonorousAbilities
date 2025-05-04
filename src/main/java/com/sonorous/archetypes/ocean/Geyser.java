package com.sonorous.archetypes.ocean;

import com.sonorous.ability.superclasses.MasterAbility;
import com.sonorous.archetypes.DisplayBlock;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.Blocks;
import com.sonorous.util.methods.Entities;
import com.sonorous.util.methods.Vectors;
import com.sonorous.util.temp.TempBlock;
import com.sonorous.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Geyser extends MasterAbility {

    public static final String name = "Geyser";

    private Vector dir;

    private Location loc, origin;

    private Set<TempBlock> sources = new HashSet<>();

    private final Set<TempDisplayBlock> geyser = new HashSet<>();


    public Geyser(Player player) {
        super(player, name);

        if (shouldStart()) {

            Block attemptedFacingSource = Blocks.getFacingBlockOrLiquid(player, sourceRange);
            if (attemptedFacingSource != null) {
                if (Blocks.getArchetypeBlocks(sPlayer).contains(attemptedFacingSource.getType())) {
                    sources = OceanUtils.freeze(attemptedFacingSource.getLocation(), radius, sPlayer);
                    this.loc = attemptedFacingSource.getLocation();
                    this.origin = loc.clone();
                }
            }
            if (sources.isEmpty()) {
                this.loc = player.getLocation();
                this.origin = loc.clone();
                sources = OceanUtils.freeze(player.getLocation().add(0, radius / 2, 0), radius, sPlayer);
            }

            if (!sources.isEmpty()) {
                sources.forEach(tempBlock -> {
                    TempDisplayBlock water = new TempDisplayBlock(tempBlock.getBlock().getLocation(), DisplayBlock.WATER, 60000, size);
                    geyser.add(water);
                });
//                Bukkit.broadcastMessage("geyser has " + geyser.size());
                abilityStatus = AbilityStatus.SOURCE_SELECTED;
                start();
            }
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        switch (abilityStatus) {
            case SOURCE_SELECTED -> {
                if (!player.isSneaking()) {
                    setHasClicked();
                    dir = new Vector(0, 1, 0);
                }
            }
            case SHOT -> {
                loc.add(dir.clone().multiply(speed));
                Entities.getEntitiesAroundPoint(loc, radius).forEach(entity -> entity.setVelocity(dir));
                geyser.forEach(tempDisplayBlock -> {
                    tempDisplayBlock.moveTo(tempDisplayBlock.getLoc().add(Vectors.getDirectionBetweenLocations(tempDisplayBlock.getLoc(), loc.clone().add(Vectors.getRandom().clone().multiply(radius))).normalize()));
                });
                if (loc.distanceSquared(origin) > range * range) {
                    this.remove();
                }
            }
        }
    }

    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.SOURCE_SELECTED) {
            dir = player.getEyeLocation().getDirection();

            abilityStatus = AbilityStatus.SHOT;
        }
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
        sources.stream().filter(Objects::nonNull).forEach(TempBlock::revert);
        geyser.stream().filter(Objects::nonNull).forEach(TempDisplayBlock::revert);
    }

    @Override
    public String getName() {
        return name;
    }
}
