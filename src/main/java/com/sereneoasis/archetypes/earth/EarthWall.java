package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.RaiseBlockPillar;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.entity.Player;

public class EarthWall extends CoreAbility {

    private final String name = "EarthWall";

    private RaiseBlockPillar raiseBlockPillar;

    public EarthWall(Player player) {
        super(player, "EarthWall");

        if (shouldStart()) {

            raiseBlockPillar = new RaiseBlockPillar(player, name, range);
            if (raiseBlockPillar.getAbilityStatus() == AbilityStatus.SOURCE_SELECTED) {
                start();
            }
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (raiseBlockPillar.getAbilityStatus() == AbilityStatus.COMPLETE) {
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