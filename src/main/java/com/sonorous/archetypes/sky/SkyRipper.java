package com.sonorous.archetypes.sky;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.abilityuilities.particles.Blade;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.ArchetypeVisuals;
import com.sonorous.util.methods.Locations;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SkyRipper extends CoreAbility {

    private static final String name = "SkyRipper";

    private Blade blade;

    private Location loc1, loc2;

    private boolean selected1 = false, selected2 = false;


    public SkyRipper(Player player) {
        super(player, name);

        if (shouldStart()) {
            start();
            return;
        }


    }

    @Override
    public void progress() {
        if (!(abilityStatus == AbilityStatus.SHOT || abilityStatus == AbilityStatus.SOURCE_SELECTED) && !player.isSneaking()) {
            this.remove();
        }

        if (abilityStatus == AbilityStatus.SOURCE_SELECTED && !player.isSneaking()) {
            abilityStatus = AbilityStatus.SHOT;
            if (loc1 != null && loc2 != null) {
                blade = new Blade(player, name, new ArchetypeVisuals.AirVisual(), loc1, loc2);
            } else {
                this.remove();
            }
        }

        if (abilityStatus == AbilityStatus.SHOT) {
//            blade.getLocs().stream().map(Location::getBlock).collect(Collectors.toSet())
//                    .stream().filter(block -> !block.isPassable())
//                    .forEach(block -> SkyUtils.lightningStrike(this,block.getLocation()));
            if (blade.getAbilityStatus() == AbilityStatus.COMPLETE) {
                this.remove();
            }
        }

    }

    public void setHasClicked() {
        if (!selected1) {
            loc1 = Locations.getFacingLocation(player.getEyeLocation(), player.getEyeLocation().getDirection(), sourceRange).clone();
            selected1 = true;
        } else if (!selected2) {
            loc2 = Locations.getFacingLocation(player.getEyeLocation(), player.getEyeLocation().getDirection(), sourceRange).clone();
            selected2 = true;
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
        }

    }

    @Override
    public void remove() {
        super.remove();
        if (blade != null) {
            blade.remove();
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