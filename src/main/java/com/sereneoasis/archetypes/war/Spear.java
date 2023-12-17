package com.sereneoasis.archetypes.war;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.items.ShootItemDisplay;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Display;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Spear extends CoreAbility {

    public final String name = "Spear";

    private ShootItemDisplay spear;

    private Location origin;

    public Spear(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        origin = player.getEyeLocation().clone();
        spear = new ShootItemDisplay(player, name,origin , origin.getDirection().clone(), Material.TRIDENT, 3, false, false);
        abilityStatus = AbilityStatus.SHOT;
        start();
    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (spear.getAbilityStatus() == AbilityStatus.COMPLETE)
        {
            this.remove();
            spear.remove();
            sPlayer.addCooldown(name,cooldown);
        }

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
