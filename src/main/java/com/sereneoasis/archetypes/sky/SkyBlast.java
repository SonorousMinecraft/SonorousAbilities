package com.sereneoasis.archetypes.sky;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.particles.SourcedBlast;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class SkyBlast extends CoreAbility {

    private final String name = "SkyBlast";
    private SourcedBlast sourcedBlast;

    public SkyBlast(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        this.sourcedBlast = new SourcedBlast(player, "SkyBlast", false, new ArchetypeVisuals.AirVisual(), true);
        start();
    }

    @Override
    public void progress() {
        if (sourcedBlast.getAbilityStatus() == AbilityStatus.COMPLETE) {
            sourcedBlast.remove();
            this.remove();
            sPlayer.addCooldown(name, cooldown);
        }
    }

    public void setHasClicked() {
        sourcedBlast.setHasClicked();
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
