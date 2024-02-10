package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.BlockSmashSourced;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.entity.Player;

public class Boulder extends CoreAbility {

    private final String name = "Boulder";

    private BlockSmashSourced blockSmashSourced;
    public Boulder(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(name)) {
            return;
        }

        blockSmashSourced = new BlockSmashSourced(player, name, null);
        if (blockSmashSourced.getAbilityStatus() == AbilityStatus.SOURCE_SELECTED)
        {
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (blockSmashSourced.getAbilityStatus() == AbilityStatus.COMPLETE){
            this.remove();
            blockSmashSourced.remove();
            sPlayer.addCooldown(name, cooldown);
        }
    }

    public void setHasClicked() {
        blockSmashSourced.setHasClicked();
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
