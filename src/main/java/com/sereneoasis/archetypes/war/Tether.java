package com.sereneoasis.archetypes.war;

import com.sereneoasis.Serenity;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.items.ShootItemDisplay;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.Laser;
import com.sereneoasis.util.methods.Display;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Vectors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.*;

import java.lang.Math;

public class Tether extends CoreAbility {

    private Location loc;
    private Vector dir;
    private final String name = "Tether";

    private ShootItemDisplay tether;

    private ArmorStand armorStand;

    //private FishHook fishHook;
    
    private Laser.GuardianLaser guardianLaser;

    public Tether(Player player) throws ReflectiveOperationException {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        loc = player.getEyeLocation().clone();
        dir = loc.getDirection().clone();
        tether = new ShootItemDisplay(player, name, loc, dir, Material.ARROW, 0.5);
        armorStand = tether.getArmorStand();

//        fishHook = (FishHook) loc.getWorld().spawn(loc, EntityType.FISHING_HOOK.getEntityClass(), (entity) ->
//        {
//            FishHook fhook = (FishHook) entity;
//            fhook.setHookedEntity(armorStand);
//        });
        guardianLaser = new Laser.GuardianLaser(Locations.getMainHandLocation(player), armorStand.getLocation(), -1, 50);
        guardianLaser.start(Serenity.getPlugin());

        start();
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        
        guardianLaser.moveStart(Locations.getMainHandLocation(player));
        guardianLaser.moveEnd(armorStand.getLocation());

        if (tether.getAbilityStatus() == AbilityStatus.COMPLETE)
        {
            player.setVelocity(Vectors.getDirectionBetweenLocations(player.getLocation(), armorStand.getLocation()).normalize());
            if (armorStand.getLocation().distance(player.getLocation()) < 2)
            {
                this.remove();
            }
        }
    }

    @Override
    public void remove() {
        super.remove();
        //fishHook.remove();
        guardianLaser.stop();
        tether.remove();
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
