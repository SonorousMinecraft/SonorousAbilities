package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.velocity.Levitate;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import org.bukkit.entity.Player;

public class Sunrise extends CoreAbility {

    private final static String name = "Sunrise";

    private Levitate levitate;

    public Sunrise(Player player) {
        super(player, name);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        levitate = new Levitate(player, name);
        start();
    }

    @Override
    public void progress() {

        new ArchetypeVisuals.SunVisual().playVisual(player.getEyeLocation().subtract(0, radius, 0), size, radius, 10, 5, 1);
        if (player.isSneaking() && sPlayer.getHeldAbility().equals(name) | levitate.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
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