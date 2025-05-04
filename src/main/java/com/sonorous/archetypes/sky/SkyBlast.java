package com.sonorous.archetypes.sky;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.abilityuilities.particles.SourcedBlast;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.ArchetypeVisuals;
import org.bukkit.entity.Player;

public class SkyBlast extends CoreAbility {

    private static final String name = "SkyBlast";
    private SourcedBlast sourcedBlast;

    public SkyBlast(Player player) {
        super(player, name);

        if (shouldStart()) {
            this.sourcedBlast = new SourcedBlast(player, "SkyBlast", false, new ArchetypeVisuals.AirVisual(), true, false);
            start();
        }


    }

    @Override
    public void progress() {

        if (!sourcedBlast.getLoc().getBlock().isPassable()) {
            SkyUtils.lightningStrike(this, sourcedBlast.getLoc());
            this.remove();
        }

        if (sourcedBlast.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();

        }
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
        sourcedBlast.remove();
    }

    public void setHasClicked() {
        sourcedBlast.setHasClicked();
        sourcedBlast.setDir();
    }


    @Override
    public String getName() {
        return name;
    }
}