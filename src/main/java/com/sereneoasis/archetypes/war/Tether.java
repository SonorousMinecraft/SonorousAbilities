package com.sereneoasis.archetypes.war;

import com.sereneoasis.Serenity;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.items.ShootItemDisplay;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.Laser;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Vectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

public class Tether extends CoreAbility {

    private final String name = "Tether";

    private ShootItemDisplay tether1, tether2;

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
        tether1 = new ShootItemDisplay(player, name, loc, dir, Material.ARROW, 0.5);
        armorStand1 = tether1.getArmorStand();

        guardianLaser = new Laser.GuardianLaser(Locations.getMainHandLocation(player), armorStand1.getLocation(), -1, 50);
        guardianLaser.start(Serenity.getPlugin());

        start();
    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (!hasShot2) {
            guardianLaser.moveStart(Locations.getMainHandLocation(player));
        }
        else{
            guardianLaser.moveStart(armorStand2.getLocation());
        }
        guardianLaser.moveEnd(armorStand1.getLocation());


        if (player.isSneaking() && tether1.getAbilityStatus() == AbilityStatus.COMPLETE)
        {
            if (hasShot2)
            {
                if (tether2.getAbilityStatus() == AbilityStatus.COMPLETE)
                {
                    this.remove();
                }
            }
            else {
                player.setVelocity(Vectors.getDirectionBetweenLocations(player.getLocation(), armorStand1.getLocation()).normalize());
                if (armorStand1.getLocation().distance(player.getLocation()) < 2) {
                    this.remove();
                }
            }
        }
    }

    public void setHasClicked()
    {
        if (!hasShot2)
        {
            hasShot2 = true;
            Location loc = player.getEyeLocation().clone();
            Vector dir = loc.getDirection().clone();
            tether2 = new ShootItemDisplay(player, name, loc, dir, Material.ARROW, 0.5);
            armorStand2 = tether2.getArmorStand();
        }
    }

    @Override
    public void remove() {
        super.remove();
        guardianLaser.stop();
        tether1.remove();
        if (hasShot2)
        {
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
