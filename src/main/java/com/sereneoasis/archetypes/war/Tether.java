package com.sereneoasis.archetypes.war;

import com.sereneoasis.Serenity;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.items.ThrowItemDisplay;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.Laser;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Vectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Tether extends CoreAbility {

    private final String name = "Tether";

    private ThrowItemDisplay tether1, tether2;

    private boolean hasShot2 = false, hasTarget = false;

    private ArmorStand armorStand1, armorStand2;

    private Laser.CrystalLaser crystalLaser;

    private Entity target;


    public Tether(Player player) throws ReflectiveOperationException {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        Location loc = player.getEyeLocation().clone();
        Vector dir = loc.getDirection().clone();
        tether1 = new ThrowItemDisplay(player, name, loc, dir, Material.SPECTRAL_ARROW, size, true, true);
        armorStand1 = tether1.getArmorStand();

        crystalLaser = new Laser.CrystalLaser(Locations.getMainHandLocation(player), tether1.getLoc(), -1, 200);
        crystalLaser.start(Serenity.getPlugin());

        start();
    }

    @Override
    public void progress() throws ReflectiveOperationException {

        Location startLoc = Locations.getMainHandLocation(player);
        Location endLoc = tether1.getLoc();
        if (!hasShot2) {
            if (!hasTarget){
                if (Entities.getAffected(tether1.getLoc(), hitbox, player) instanceof LivingEntity livingEntity){
                    tether1.remove();
                    target = livingEntity;
                    hasTarget = true;
                    endLoc = target.getLocation();
                }
            } else{
                endLoc = target.getLocation();
            }

        } else {
            startLoc = tether2.getLoc();
        }
        crystalLaser.moveStart(startLoc);
        crystalLaser.moveEnd(endLoc);


        if (player.isSneaking() && tether1.getAbilityStatus() == AbilityStatus.COMPLETE) {
            if (hasShot2) {
                if (tether2.getAbilityStatus() == AbilityStatus.COMPLETE) {
                    Entity between = Entities.getEntityBetweenPoints(tether2.getLoc(), tether1.getLoc());
                    if (between instanceof Player zipliner && zipliner.isSneaking()) {
                        Vector playerLooking = player.getEyeLocation().getDirection().clone();
                        Vector vec1to2 = Vectors.getDirectionBetweenLocations(tether1.getLoc(), tether2.getLoc()).normalize().multiply(0.5);
                        Vector vec2to1 = vec1to2.clone().multiply(-1);
                        if (Vectors.getAngleBetweenVectors(playerLooking, vec1to2) < Vectors.getAngleBetweenVectors(playerLooking, vec2to1)) {
                            player.setVelocity(vec1to2);
                        } else {
                            player.setVelocity(vec2to1);
                        }
                    }
                }
            } else {
                if (!hasTarget) {
                    player.setVelocity(Vectors.getDirectionBetweenLocations(player.getLocation(), tether1.getLoc()).normalize());
                    if (tether1.getLoc().distance(player.getLocation()) < 2) {
                        this.remove();
                    }
                }
                else{
                    target.setVelocity(Vectors.getDirectionBetweenLocations(tether1.getLoc(),  player.getLocation()).normalize());

                    if (tether1.getLoc().distance(player.getLocation()) < 2) {
                        this.remove();
                    }
                }
            }
        }
    }

    public void setHasClicked() {
        if (!hasShot2 && !player.isSneaking()) {
            hasShot2 = true;
            Location loc = player.getEyeLocation().clone();
            Vector dir = loc.getDirection().clone();
            tether2 = new ThrowItemDisplay(player, name, loc, dir, Material.ARROW, size, true, true);
            armorStand2 = tether2.getArmorStand();
        } else {
            this.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();
        crystalLaser.stop();
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
