package com.sereneoasis.archetypes.war;

import com.sereneoasis.Serenity;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.items.ThrowItemDisplay;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.Laser;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.methods.Vectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Tether extends CoreAbility {

    private final String name = "Tether";

    private ThrowItemDisplay tether1, tether2;

    private boolean hasShot2 = false;

    private ArmorStand armorStand1, armorStand2;

    private Laser.GuardianLaser guardianLaser;

    public Tether(Player player) throws ReflectiveOperationException {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        Location loc = player.getEyeLocation().clone();
        Vector dir = loc.getDirection().clone();
        tether1 = new ThrowItemDisplay(player, name, loc, dir, Material.ARROW, 1.0, true, true);
        armorStand1 = tether1.getArmorStand();

        guardianLaser = new Laser.GuardianLaser(Locations.getMainHandLocation(player), armorStand1.getLocation(), -1, 50);
        guardianLaser.start(Serenity.getPlugin());

        start();
    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (!hasShot2) {
            guardianLaser.moveStart(Locations.getMainHandLocation(player));
        } else {
            guardianLaser.moveStart(armorStand2.getLocation());
        }
        guardianLaser.moveEnd(armorStand1.getLocation());


        if (player.isSneaking() && tether1.getAbilityStatus() == AbilityStatus.COMPLETE) {
            if (hasShot2) {
                if (tether2.getAbilityStatus() == AbilityStatus.COMPLETE) {
                    Entity between = Entities.getEntityBetweenPoints(armorStand2.getLocation(), armorStand1.getLocation());
                    if (between instanceof Player zipliner && zipliner.equals(player)) {
                        Particles.spawnParticle(Particle.SMOKE_NORMAL, armorStand1.getLocation(), 1, 0, 0);
                        Particles.spawnParticle(Particle.SMOKE_NORMAL, armorStand2.getLocation(), 1, 0, 0);
                        Vector playerLooking = player.getEyeLocation().getDirection().clone();
                        Vector vec1to2 = Vectors.getDirectionBetweenLocations(armorStand1.getLocation(), armorStand2.getLocation()).normalize().multiply(0.5);
                        Vector vec2to1 = vec1to2.clone().multiply(-1);
                        if (Vectors.getAngleBetweenVectors(playerLooking, vec1to2) < Vectors.getAngleBetweenVectors(playerLooking, vec2to1)) {
                            player.setVelocity(vec1to2);
                        } else {
                            player.setVelocity(vec2to1);
                        }
                    }
                }
            } else {
                player.setVelocity(Vectors.getDirectionBetweenLocations(player.getLocation(), armorStand1.getLocation()).normalize());
                if (armorStand1.getLocation().distance(player.getLocation()) < 2) {
                    this.remove();
                }
            }
        }
    }

    public void setHasClicked() {
        if (!hasShot2) {
            hasShot2 = true;
            Location loc = player.getEyeLocation().clone();
            Vector dir = loc.getDirection().clone();
            tether2 = new ThrowItemDisplay(player, name, loc, dir, Material.ARROW, 1.0, true, true);
            armorStand2 = tether2.getArmorStand();
        } else {
            this.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();
        guardianLaser.stop();
        tether1.remove();
        if (hasShot2) {
            tether2.remove();
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
