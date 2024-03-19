package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.RaiseBlockCircle;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.AbilityUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EarthQuake extends CoreAbility {

    private final String name = "EarthQuake";
    private double currentRadius = 0.5;
    private List<RaiseBlockCircle> quakes;

    public EarthQuake(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        abilityStatus = AbilityStatus.CHARGING;
        quakes = new ArrayList<>();
        start();
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
                abilityStatus = AbilityStatus.SHOT;
            }
            AbilityUtils.showCharged(this);
        }

        if (abilityStatus == AbilityStatus.SHOT) {
            if (currentRadius > radius) {
                abilityStatus = AbilityStatus.COMPLETE;
            }
            currentRadius += speed;
            RaiseBlockCircle shockwaveRing = new RaiseBlockCircle(player, name, 3, currentRadius, true);
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

    public void setCharged(){
        if (this.abilityStatus == AbilityStatus.CHARGING)
        {
            abilityStatus = AbilityStatus.CHARGED;
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
