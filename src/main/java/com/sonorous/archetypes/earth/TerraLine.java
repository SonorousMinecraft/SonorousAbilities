package com.sonorous.archetypes.earth;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.abilityuilities.blocks.RaiseBlockPillarLine;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.Scheduler;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public class TerraLine extends CoreAbility {

    private final String name = "TerraLine";

    private RaiseBlockPillarLine blockLine;

    public TerraLine(Player player) {
        super(player, "TerraLine");

        if (shouldStart()) {
            blockLine = new RaiseBlockPillarLine(player, name, Color.GREEN, true);
            if (blockLine.getAbilityStatus() == AbilityStatus.SOURCE_SELECTED) {
                abilityStatus = AbilityStatus.SOURCE_SELECTED;
                start();
            }
        }


    }

    @Override
    public void progress() {
        if (blockLine.getAbilityStatus() == AbilityStatus.COMPLETE) {
            Scheduler.performTaskLater(100L, () -> blockLine.remove());
            this.remove();
            sPlayer.addCooldown(name, cooldown);
        }
    }

    public void setHasClicked() {
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