package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.BlockLine;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public class TerraLine extends CoreAbility {

    private final String name = "TerraLine";

    private BlockLine blockLine;
    public TerraLine(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        blockLine = new BlockLine(player, name, Color.GREEN, true);
        if (blockLine.getAbilityStatus() == AbilityStatus.SOURCE_SELECTED) {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (blockLine.getAbilityStatus() == AbilityStatus.COMPLETE)
        {
            blockLine.remove();
            this.remove();
            sPlayer.addCooldown(name, cooldown);
        }
    }

    public void setHasClicked()
    {
        blockLine.setHasClicked();
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
