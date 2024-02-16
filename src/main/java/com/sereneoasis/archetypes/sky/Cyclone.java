package com.sereneoasis.archetypes.sky;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.velocity.Skate;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.methods.TDBs;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.List;

public class Cyclone extends CoreAbility {

    private final String name = "Cyclone";
    private Skate skate;


    public Cyclone(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        this.skate = new Skate(player, name, 10,3, true);
        start();
    }

    @Override
    public void progress() {
        if (skate.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
        }

        Location tempLoc = player.getEyeLocation();
        tempLoc.setPitch(0);
        List<Location> locs = Locations.getCircle(player.getEyeLocation().subtract(player.getEyeLocation().getDirection().clone().multiply(speed)), radius, 20,tempLoc.getDirection(), Math.toRadians(90));
        for (Location loc: locs){
            TDBs.playTDBs(loc, DisplayBlock.AIR, 1, size, 0);
        }
        //Particles.playLocParticles(locs, Particle.SPELL, 1, 0, 0);
    }

    public void setHasClicked() {
        this.remove();
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
