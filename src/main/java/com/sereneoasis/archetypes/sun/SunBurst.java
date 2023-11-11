package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.archetypes.ocean.Blizzard;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.temp.TempBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SunBurst extends CoreAbility {

    private final String name = "SunBurst";

    private HashMap<Integer,TempDisplayBlock> explosion = new HashMap<>();

    private double currentRadius = 0;

    public SunBurst(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(name)) {
            return;
        }

        abilityStatus = AbilityStatus.CHARGING;
        start();
    }

    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.CHARGING) {
            if (!player.isSneaking()) {
                this.remove();
            }
            if (System.currentTimeMillis() > chargeTime + startTime) {
                abilityStatus = AbilityStatus.CHARGED;
            }
        }

        if (abilityStatus == AbilityStatus.CHARGED) {
            currentRadius += speed;
            if (currentRadius < radius) {
                explosion = Entities.handleDisplayBlockEntities(explosion,
                        Locations.getOutsideSphereLocs(player.getLocation(), currentRadius, 0.5),
                        DisplayBlock.SUN, 0.5);

                for (Block b : Blocks.getBlocksAroundPoint(player.getLocation(), radius)) {
                    Block topBlock = b.getLocation().add(0, 1, 0).getBlock();
                    if (topBlock.getType().isAir()) {
                        new TempBlock(topBlock, DisplayBlock.FIRE, 2000, true);
                    }
                }
            } else {
                this.remove();

            }
        }
    }



    @Override
    public void remove() {
        super.remove();
        for (TempDisplayBlock tb : explosion.values())
        {
            tb.revert();
        }
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
