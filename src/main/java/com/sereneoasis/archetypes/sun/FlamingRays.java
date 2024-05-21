package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.forcetype.BlockExplodeSphere;
import com.sereneoasis.abilityuilities.particles.Blast;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.AbilityUtils;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Scheduler;
import com.sereneoasis.util.temp.TempBlock;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FlamingRays extends CoreAbility {

    private static final String name = "FlamingRays";

    private int currentShots = 0, shots = 5;

    private HashMap<Integer, Blast> rays = new HashMap<>();

    public FlamingRays(Player player) {
        super(player, name);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
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
                        SunUtils.blockExplode(player, name, blast.getLoc(), radius*10, 1);

//                        new BlockExplodeSphere(player, name, blast.getLoc(), radius*10, 1);
//
//                        Scheduler.performTaskLater(5, () -> {
//                            Blocks.getBlocksAroundPoint(blast.getLoc(), (radius * 10)+1).forEach(block -> {
//                                if (!block.isPassable()) {
//                                    new TempBlock(block, DisplayBlock.SUN, 60000);
//                                }
//                            });
//                        });
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
            Blast blast = new Blast(player, name, false, new ArchetypeVisuals.SunVisual());
            rays.put(currentShots, blast);
            currentShots++;
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