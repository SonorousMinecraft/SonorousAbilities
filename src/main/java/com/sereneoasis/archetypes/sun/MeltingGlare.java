package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.particles.Laser;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Locations;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class MeltingGlare extends CoreAbility {

    private Laser leftEye, rightEye;

    public MeltingGlare(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        leftEye = new Laser(player, Locations.getLeftSide(player.getEyeLocation(), 0.2),
                "MeltingGlare", Particle.FLAME);
        rightEye = new Laser(player, Locations.getRightSide(player.getEyeLocation(), 0.2),
                "MeltingGlare", Particle.FLAME);
        start();
    }

    @Override
    public void progress() {
        if (!player.isSneaking()) {
            this.remove();
        }

        if (leftEye.getAbilityStatus() == AbilityStatus.COMPLETE &&
                rightEye.getAbilityStatus() == AbilityStatus.COMPLETE) {
            leftEye.remove();
            rightEye.remove();
            this.remove();
        }

        leftEye.setLoc(Locations.getLeftSide(player.getEyeLocation(), 0.2));
        rightEye.setLoc(Locations.getRightSide(player.getEyeLocation(), 0.2));

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
