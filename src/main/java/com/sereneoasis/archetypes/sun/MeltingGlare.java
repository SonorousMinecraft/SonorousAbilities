package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.Ability;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.particles.Laser;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MeltingGlare extends CoreAbility {

    private Laser leftEye, rightEye;
    public MeltingGlare(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        Vector dir = player.getEyeLocation().getDirection().rotateAroundY(Math.toRadians(90));

        leftEye = new Laser(player, player.getEyeLocation().clone().add(dir).multiply(0.2),
                "MeltingGlare", Particle.FLAME);
        rightEye = new Laser(player, player.getEyeLocation().clone().subtract(dir).multiply(0.2),
                "MeltingGlare", Particle.FLAME);
        start();
    }

    @Override
    public void progress() {
        if (!player.isSneaking())
        {
            this.remove();
        }

        if (leftEye.getAbilityStatus() == AbilityStatus.COMPLETE &&
                rightEye.getAbilityStatus() == AbilityStatus.COMPLETE)
        {
            this.remove();
        }

        Vector dir = player.getEyeLocation().getDirection().rotateAroundY(Math.toRadians(90));
        leftEye.setLoc(player.getEyeLocation().clone().add(dir).multiply(0.2));
        rightEye.setLoc(player.getEyeLocation().clone().subtract(dir).multiply(0.2));

    }

    @Override
    public void remove() {
        super.remove();
        leftEye.remove();
        rightEye.remove();
        sPlayer.addCooldown("MeltingGlare", cooldown);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return "MeltingGlare";
    }
}
