package com.sereneoasis.archetypes.sky;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.particles.SourcedBlast;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.equipment.ItemStackUtils;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

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

        if (!sourcedBlast.getLoc().getBlock().isPassable()){
            SkyUtils.lightningStrike(this,sourcedBlast.getLoc());
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