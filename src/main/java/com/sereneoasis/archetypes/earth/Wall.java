package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.RaiseBlockPillar;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.entity.Player;

public class Wall extends CoreAbility {

    private final String name = "Wall";

    private RaiseBlockPillar raiseBlockPillar;
    public Wall(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        raiseBlockPillar = new RaiseBlockPillar(player, name, range);
        if (raiseBlockPillar.getAbilityStatus() == AbilityStatus.SOURCE_SELECTED) {
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (raiseBlockPillar.getAbilityStatus() == AbilityStatus.COMPLETE)
        {
            this.remove();
            sPlayer.addCooldown(name, cooldown);
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
