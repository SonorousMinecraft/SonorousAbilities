package com.sonorous.archetypes.sun;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.abilityuilities.particles.Beam;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.AbilityUtils;
import com.sonorous.util.methods.ArchetypeVisuals;
import com.sonorous.util.methods.Particles;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class SolarBeam extends CoreAbility {

    private static final String name = "SolarBeam";

//    private SphereBlast blast;

    private Beam beam;

    public SolarBeam(Player player) {
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


        if (abilityStatus == AbilityStatus.SHOT) {
//            if (blast.getAbilityStatus() == AbilityStatus.COMPLETE) {
//                blast.remove();
//                this.remove();
//            }
            Particles.spawnParticle(Particle.EXPLOSION_LARGE, beam.getBeamOrigin(), 5, 0.5, 0);


            if (beam.getAbilityStatus() == AbilityStatus.COMPLETE) {
                beam.remove();
                this.remove();
            }
        } else {
            if (System.currentTimeMillis() > chargeTime + startTime) {
                AbilityUtils.showCharged(this);
            }
        }
    }

    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.CHARGED) {
//            blast = new SphereBlast(player, name, false, new ArchetypeVisuals.SunVisual());
            beam = new Beam(player, name, new ArchetypeVisuals.SunVisual(), player.getEyeLocation().add(0, 10, 0));
            abilityStatus = AbilityStatus.SHOT;
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