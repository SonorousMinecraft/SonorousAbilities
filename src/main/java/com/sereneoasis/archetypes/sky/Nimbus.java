package com.sereneoasis.archetypes.sky;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.velocity.Jet;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Particles;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Nimbus extends CoreAbility {

    private final String name = "Nimbus";

    private Jet jet;

    public Nimbus(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        jet = new Jet(player, name);

        start();
    }

    @Override
    public void progress() {

        Particles.spawnParticle(Particle.SPELL, player.getEyeLocation().subtract(0, 2, 0), 5, 0.5, 0);
        if (jet.getAbilityStatus() == AbilityStatus.COMPLETE) {
            jet.remove();
            this.remove();
            sPlayer.addCooldown(name, cooldown);
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
