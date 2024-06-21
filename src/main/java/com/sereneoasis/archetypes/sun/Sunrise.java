package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.velocity.Levitate;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.AbilityUtils;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import com.sereneoasis.util.methods.Particles;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Sunrise extends CoreAbility {

    private final static String name = "Sunrise";

    private Levitate levitate;

    private long sinceLastDashed = System.currentTimeMillis();

    public Sunrise(Player player) {
        super(player, name);

        if (shouldStart()) {
            levitate = new Levitate(player, name);

            Particles.spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 10, 0.5, 0);
            sinceLastDashed = System.currentTimeMillis();
            player.setVelocity(new Vector(0, speed, 0));

            start();
        }


    }

    @Override
    public void progress() {

        new ArchetypeVisuals.SunVisual().playVisual(player.getEyeLocation().subtract(0, radius, 0), size, radius, 10, 5, 1);
        if ((player.isSneaking() && sPlayer.getHeldAbility().equals(name)) || levitate.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
        }

        if (System.currentTimeMillis() - sinceLastDashed > chargeTime && sPlayer.getHeldAbility().equals(name)) {
            AbilityUtils.sendActionBar(player, "READY", ChatColor.RED);
        }

    }

    public void setHasClicked() {
        if (System.currentTimeMillis() - sinceLastDashed > chargeTime) {
            Particles.spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 10, 1, 0);
            sinceLastDashed = System.currentTimeMillis();
            player.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(speed));
        }
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
        levitate.remove();

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