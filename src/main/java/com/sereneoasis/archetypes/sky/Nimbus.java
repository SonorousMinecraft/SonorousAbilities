package com.sereneoasis.archetypes.sky;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.velocity.Jet;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import org.bukkit.entity.Player;

public class Nimbus extends CoreAbility {

    private static final String name = "Nimbus";

    private Jet jet;

    public Nimbus(Player player) {
        super(player, name);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        jet = new Jet(player, name);

        start();
    }

    @Override
    public void progress() {

        new ArchetypeVisuals.AirVisual().playVisual(player.getEyeLocation().subtract(0, radius, 0), size, radius, 10, 5, 1);
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