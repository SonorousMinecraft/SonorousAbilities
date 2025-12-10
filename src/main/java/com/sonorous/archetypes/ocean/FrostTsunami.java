package com.sonorous.archetypes.ocean;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.abilityuilities.blocks.forcetype.BlockSweepGivenType;
import com.sonorous.archetypes.DisplayBlock;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.Constants;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public class FrostTsunami extends CoreAbility {

    private final String name = "FrostTsunami";

    private BlockSweepGivenType sweep;

    public FrostTsunami(Player player) {
        super(player, "FrostTsunami");

        if (shouldStart()) {
            sweep = new BlockSweepGivenType(player, name, Color.BLUE, DisplayBlock.ICE);
            if (sweep.getAbilityStatus() == AbilityStatus.SOURCE_SELECTED) {
                abilityStatus = AbilityStatus.SOURCE_SELECTED;
                start();
            }
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (sweep.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
        } else {
            sweep.getTempDisplayBlocks().forEach(tempDisplayBlock -> tempDisplayBlock.moveToAndMaintainFacing(tempDisplayBlock.getLoc().add(0, Math.random() * Constants.BLOCK_RAISE_SPEED * speed * 5, 0)));

        }
    }

    public void setHasClicked() {

        if (abilityStatus == AbilityStatus.SOURCE_SELECTED) {
            sweep.setHasClicked();
            abilityStatus = AbilityStatus.SHOT;
        }
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
        sweep.remove();

    }

    @Override
    public String getName() {
        return name;
    }
}
