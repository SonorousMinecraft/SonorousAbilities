package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.SourceBlockToPlayer;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftBlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class WaterWhip extends CoreAbility {

    private final String name = "WaterWhip";


    private TempDisplayBlock glowingSource;

    private Vector dir;

    private Location origin, loc;

    public WaterWhip(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(name)) {
            return;
        }

        abilityStatus = AbilityStatus.NO_SOURCE;
        Block source = Blocks.getFacingBlockOrLiquid(player, sourceRange);
        if (source != null && Blocks.getArchetypeBlocks(sPlayer).contains(source.getType())) {
            origin = Blocks.getFacingBlockOrLiquidLoc(player, sourceRange).clone().subtract(0,size,0);
            loc = origin.clone();
            glowingSource = Blocks.selectSourceAnimationManual(origin, sPlayer.getColor(), size);
            abilityStatus = AbilityStatus.SOURCED;
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (abilityStatus == AbilityStatus.SHOT) {
            loc.add(dir.clone().multiply(speed));
            TempDisplayBlock tb = new TempDisplayBlock(loc, DisplayBlock.WATER, 500, size);
            ((CraftBlockDisplay)tb.getBlockDisplay()).getHandle().setRot(Vectors.getYaw(dir, player), Vectors.getPitch(dir, player));
            if (loc.distance(origin) > range){
                this.remove();
            }

            Entity target = Entities.getAffected(loc, hitbox, player);
            if (target instanceof LivingEntity){
                DamageHandler.damageEntity(target, player, this, damage);
                target.setVelocity(dir.clone().multiply(speed));
                this.remove();
            }
        }
    }

    public void setHasClicked(){
        if (abilityStatus == AbilityStatus.SOURCED) {
            glowingSource.revert();
            abilityStatus = AbilityStatus.SHOT;
            dir = player.getEyeLocation().getDirection();
        }
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
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
