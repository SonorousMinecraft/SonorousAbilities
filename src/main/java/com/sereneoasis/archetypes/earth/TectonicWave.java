package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.abilityuilities.blocks.BlockSweep;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Constants;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public class TectonicWave extends CoreAbility {

    private final String name = "TectonicWave";

    private BlockSweep sweep;
    public TectonicWave(Player player) {
        super(player, "TectonicWave");

        if (shouldStart()){
            sweep = new BlockSweep(player, name, Color.GREEN);
            if (sweep.getAbilityStatus() == AbilityStatus.SOURCE_SELECTED) {
                start();
            }
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (sweep.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
        } else {
            sweep.getTempDisplayBlocks().stream().forEach(tempDisplayBlock -> tempDisplayBlock.moveToAndMaintainFacing(tempDisplayBlock.getLoc().add(0, Math.random() * Constants.BLOCK_RAISE_SPEED * speed, 0)));
        }
    }

    public void setHasClicked(){
        sweep.setHasClicked();
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
