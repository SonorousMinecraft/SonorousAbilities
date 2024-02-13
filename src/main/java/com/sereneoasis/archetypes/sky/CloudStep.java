package com.sereneoasis.archetypes.sky;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.velocity.Jump;
import com.sereneoasis.abilityuilities.velocity.Levitate;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Particles;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class CloudStep extends CoreAbility {

    private Jump jump;
    private Levitate levitate;

    private final String name = "CloudStep";

    public CloudStep(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        this.levitate = new Levitate(player, name);
        start();
    }

    @Override
    public void progress() {
        if (levitate.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
        }

        if (jump != null && jump.getAbilityStatus() == AbilityStatus.COMPLETE) {
            Particles.spawnParticle(Particle.CLOUD, player.getLocation().subtract(0, 1, 0), 5, 0.5, 0);
        }

    }

    public void setHasShifted() {
        if (jump == null || jump.getAbilityStatus() == AbilityStatus.COMPLETE) {
            jump = new Jump(player, name, true);
        }
    }

    @Override
    public void remove() {
        super.remove();
        if (jump != null) {
            jump.remove();
        }
        if (levitate != null) {
            levitate.remove();
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
