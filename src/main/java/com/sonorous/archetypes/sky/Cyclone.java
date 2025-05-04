package com.sonorous.archetypes.sky;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.abilityuilities.velocity.Skate;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.ArchetypeVisuals;
import com.sonorous.util.methods.Locations;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.List;

public class Cyclone extends CoreAbility {

    private static final String name = "Cyclone";
    private Skate skate;


    public Cyclone(Player player) {
        super(player, name);

        if (shouldStart()) {
            this.skate = new Skate(player, name, 10, 3, true);
            start();

        }


    }

    @Override
    public void progress() {
        if (skate.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
        }

        Location tempLoc = player.getEyeLocation();
        tempLoc.setPitch(0);
        List<Location> locs = Locations.getCircle(player.getEyeLocation().subtract(player.getEyeLocation().getDirection().clone().multiply(speed)), radius, 20, tempLoc.getDirection(), Math.toRadians(90));
        for (Location loc : locs) {
            new ArchetypeVisuals.AirVisual().playVisual(loc, size, 0.1, 10, 1, 5);
//            TDBs.playTDBs(loc, DisplayBlock.AIR, 1, size, 0);
        }
        //Particles.playLocParticles(locs, Particle.SPELL, 1, 0, 0);
    }

    public void setHasClicked() {
        this.remove();
    }

    public ArmorStand getArmorStand() {
        return skate.getArmorStand();
    }

    @Override
    public void remove() {
        super.remove();
        if (skate != null) {
            skate.remove();
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