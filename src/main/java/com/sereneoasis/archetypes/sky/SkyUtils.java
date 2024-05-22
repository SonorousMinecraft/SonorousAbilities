package com.sereneoasis.archetypes.sky;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.enhancedmethods.EnhancedBlocksArchetypeLess;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;

public class SkyUtils {

    public static void lightningStrike(Location loc){
        LightningStrike strike = (LightningStrike) loc.getWorld().spawn(loc, EntityType.LIGHTNING.getEntityClass(), ((entity) ->
        {
            LightningStrike lightning = (LightningStrike) entity;
        }));
    }

    public static void lightningStrikeFloorCircle(CoreAbility coreAbility, Location loc){
        EnhancedBlocksArchetypeLess.getTopCircleBlocksFloor(coreAbility, loc).forEach(block -> SkyUtils.lightningStrike(block.getLocation()));

    }
}
