package com.sereneoasis.archetypes.sky;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.BlockExplodeSphere;
import com.sereneoasis.util.enhancedmethods.EnhancedBlocksArchetypeLess;
import com.sereneoasis.util.methods.Scheduler;
import com.sereneoasis.util.methods.Vectors;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;

public class SkyUtils {

    public static void lightningStrike(CoreAbility coreAbility, Location loc) {
        Player player = coreAbility.getPlayer();
        String name = coreAbility.getName();

        LightningStrike strike = (LightningStrike) loc.getWorld().spawn(loc, EntityType.LIGHTNING.getEntityClass(), ((entity) ->
        {
            LightningStrike lightning = (LightningStrike) entity;
            lightning.setFlashes(1);
            lightning.setCausingPlayer(player);
            lightning.setSilent(true);
        }));

    }

    public static void lightningStrikeFloorCircle(CoreAbility coreAbility, Location loc, double radius) {
        Player player = coreAbility.getPlayer();
        String name = coreAbility.getName();
        for (int i = 0; i < 20; i += 2) {
            Scheduler.performTaskLater(i, () -> {
                EnhancedBlocksArchetypeLess.getTopCircleBlocksFloor(coreAbility, loc.clone().add(0, 100, 0)).forEach(block -> SkyUtils.lightningStrike(coreAbility, block.getLocation()));
            });
        }
        new BlockExplodeSphere(player, name, loc.clone().add(Vectors.getRandom()), radius, 1);


    }
}
