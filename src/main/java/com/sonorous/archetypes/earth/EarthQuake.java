package com.sonorous.archetypes.earth;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.abilityuilities.blocks.RaiseBlockCircle;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.AbilityUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EarthQuake extends CoreAbility {

    private final String name = "EarthQuake";
    private double currentRadius = 0.5;
    private List<RaiseBlockCircle> quakes;

    private Location loc;

    public EarthQuake(Player player) {
        super(player, "EarthQuake");

        if (shouldStart()) {
            abilityStatus = AbilityStatus.CHARGING;
            quakes = new ArrayList<>();
            start();
        }


    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (abilityStatus == AbilityStatus.CHARGING) {
            if (startTime + chargeTime < System.currentTimeMillis()) {
                abilityStatus = AbilityStatus.CHARGED;
            }

            if (!player.isSneaking()) {
                this.remove();
            }
        }

        if (abilityStatus == AbilityStatus.CHARGED) {
            if (!player.isSneaking()) {
                this.loc = player.getLocation();
                abilityStatus = AbilityStatus.SHOT;
            }
            AbilityUtils.showCharged(this);
        }

        if (abilityStatus == AbilityStatus.SHOT) {
            if (currentRadius > radius) {
                abilityStatus = AbilityStatus.COMPLETE;
            }
            currentRadius += size;
            RaiseBlockCircle shockwaveRing = new RaiseBlockCircle(player, name, loc, 3, currentRadius, true);
            quakes.add(shockwaveRing);


        }
        if (abilityStatus == AbilityStatus.COMPLETE) {
            for (RaiseBlockCircle shockwaveRing : quakes) {
                if (shockwaveRing.getAbilityStatus() == AbilityStatus.COMPLETE) {
                    shockwaveRing.remove();
                }
            }
            if (quakes.stream().noneMatch(quake -> quake.getAbilityStatus() != AbilityStatus.COMPLETE)) {
                this.remove();
            }
        }
    }

    public void setCharged() {
        if (this.abilityStatus == AbilityStatus.CHARGING) {
            abilityStatus = AbilityStatus.CHARGED;
        }
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
    }

    @Override
    public String getName() {
        return name;
    }
}