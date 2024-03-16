package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BlockLine extends CoreAbility {

    private final String name;

    private Location origin, loc;

    private Vector dir;


    private TempDisplayBlock glowingSource;

    private boolean directable;

    private Material type;

    private Vector offsetAdjustment = new Vector(-size/2, size/4, -size/2);

    public BlockLine(Player player, String name, Color color, boolean directable) {
        super(player, name);
        this.name = name;
        this.directable = directable;
        this.dir = player.getEyeLocation().getDirection().setY(0).normalize();
        abilityStatus = AbilityStatus.NO_SOURCE;
        Block source = Blocks.getFacingBlock(player, sourceRange);
        if (source != null && Blocks.getArchetypeBlocks(sPlayer).contains(source.getType())) {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            this.origin = Blocks.getFacingBlockLoc(player, sourceRange).subtract(0,size,0);
            glowingSource = Blocks.selectSourceAnimationManual(origin, color, size);
            this.loc = origin.clone();
            this.type = source.getType();
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (abilityStatus == AbilityStatus.SHOT) {
            getNextLoc();
            if (loc != null) {
                new TempDisplayBlock(loc.clone().add(offsetAdjustment), type, 500, size);
                Entity target = Entities.getAffected(loc.clone().add(0,size/2,0), hitbox, player);
                if (target instanceof  LivingEntity) {
                    DamageHandler.damageEntity(target, player, this, damage);
                    target.setVelocity(dir.clone().multiply(speed));
                    abilityStatus = AbilityStatus.COMPLETE;
                }


                if (loc.distanceSquared(origin) > range*range) {
                    abilityStatus = AbilityStatus.COMPLETE;
                }
            } else {
                abilityStatus = AbilityStatus.COMPLETE;
            }
        }
    }

    private void getNextLoc() {
        if (directable) {
            dir = player.getEyeLocation().getDirection().setY(0).normalize();
        }
        loc.add(dir.clone().multiply(speed));
        Location middleLoc = loc.clone();
        Location topLoc = middleLoc.clone().add(0, 1, 0);
        Location bottomLoc = middleLoc.clone().subtract(0, 1, 0);
        if (middleLoc.getBlock().isLiquid() || middleLoc.getBlock().getType().isAir()) {
            if (!(topLoc.getBlock().isLiquid() || topLoc.getBlock().getType().isAir())) {
                loc = topLoc;
            } else if (!(bottomLoc.getBlock().isLiquid() || bottomLoc.getBlock().getType().isAir())) {
                loc = bottomLoc;
            } else {
                loc = null;
            }
        } else if (!Blocks.isTopBlock(middleLoc.getBlock())) {
            middleLoc.add(0, 1, 0);
            if (!Blocks.isTopBlock(middleLoc.getBlock())) {
                loc = null;
            } else {
                loc = middleLoc;
            }
        }

    }

    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.SOURCE_SELECTED) {
            abilityStatus = AbilityStatus.SHOT;
            glowingSource.revert();
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
