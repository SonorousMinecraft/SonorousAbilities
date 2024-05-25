package com.sereneoasis.archetypes.earth;

import com.sereneoasis.Serenity;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.BlockLine;
import com.sereneoasis.abilityuilities.blocks.RaiseBlockPillarLine;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Scheduler;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public class TerraLine extends CoreAbility {

    private final String name = "TerraLine";

    private RaiseBlockPillarLine blockLine;

    public TerraLine(Player player) {
        super(player, "TerraLine");

        if (shouldStart()) {
            return;
        }

        blockLine = new RaiseBlockPillarLine(player, name, Color.GREEN, true);
        if (blockLine.getAbilityStatus() == AbilityStatus.SOURCE_SELECTED) {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            start();
        }
    }

    @Override
    public void progress() {
        if (blockLine.getAbilityStatus() == AbilityStatus.COMPLETE) {
            Scheduler.performTaskLater(100L,()-> blockLine.remove());
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