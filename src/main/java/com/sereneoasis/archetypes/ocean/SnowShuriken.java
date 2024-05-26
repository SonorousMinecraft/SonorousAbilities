package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.particles.Blast;
import com.sereneoasis.archetypes.ocean.OceanUtils;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.AbilityUtils;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import com.sereneoasis.util.methods.Blocks;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SnowShuriken extends CoreAbility {

    private static final String name = "SnowShuriken";

    private int currentShots = 0, shots = 8;

    private HashMap<Integer, Blast> rays = new HashMap<>();

    public SnowShuriken(Player player) {
        super(player, name);

        if (shouldStart()) {
            abilityStatus = AbilityStatus.CHARGING;
            start();
        }


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
        AbilityUtils.showCharged(this);
        AbilityUtils.showShots(this, currentShots, shots);
        if (abilityStatus == AbilityStatus.SHOOTING) {
            for (int i = 0; i < currentShots; i++) {
                Blast blast = rays.get(i);
                if (blast.getAbilityStatus() == AbilityStatus.COMPLETE) {
                    if (i == shots-1) {
                        blast.remove();
                        this.remove();
                    }
                } else {
                    if (Blocks.isSolid(blast.getLoc())) {
                        OceanUtils.freeze(blast.getLoc(), radius, sPlayer);
                        blast.remove();
//                        SunUtils.blockExplode(player, name, blast.getLoc(), radius*10, 1);

//                        new BlockExplodeSphere(player, name, blast.getLoc(), radius*10, 1);
//
//                        Scheduler.performTaskLater(5, () -> {
//                            Blocks.getBlocksAroundPoint(blast.getLoc(), (radius * 10)+1).forEach(block -> {
//                                if (!block.isPassable()) {
//                                    new TempBlock(block, DisplayBlock.SUN, 60000);
//                                }
//                            });
//                        });
                    } else if (blast.getLoc().getBlock().getType().equals(Material.WATER)){
                        OceanUtils.freeze(blast.getLoc(), radius, sPlayer);
                    }
                }
            }

        }

    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
    }

    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.CHARGED) {
            abilityStatus = AbilityStatus.SHOOTING;
        }
        if (abilityStatus == AbilityStatus.SHOOTING && currentShots<shots){
            Blast blast = new Blast(player, name, false, new ArchetypeVisuals.SnowVisual());
            rays.put(currentShots, blast);
            currentShots++;
        }
    }


    @Override
    public String getName() {
        return name;
    }
}