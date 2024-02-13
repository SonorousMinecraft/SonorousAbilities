package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.velocity.Levitate;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.entity.Player;

public class Sunrise extends CoreAbility {

    private final String name = "Sunrise";

    private Levitate levitate;

    public Sunrise(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        levitate = new Levitate(player, name);
        start();
    }

    @Override
    public void progress() {

        if (player.isSneaking() | levitate.getAbilityStatus() == AbilityStatus.COMPLETE) {
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
