package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.RaiseBlock;
import com.sereneoasis.abilityuilities.blocks.ShootBlockFromLoc;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Entities;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class RockKick extends CoreAbility {

    private RaiseBlock raiseBlock;

    private ShootBlockFromLoc shootBlockFromLoc;

    private final String name = "RockKick";
    public RockKick(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        raiseBlock = new RaiseBlock(player, name, 1.5, true);
        if (raiseBlock.getAbilityStatus() == AbilityStatus.SOURCE_SELECTED) {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (abilityStatus == AbilityStatus.SOURCE_SELECTED)
        {
            if (raiseBlock.getAbilityStatus() == AbilityStatus.SOURCED)
            {
                abilityStatus = AbilityStatus.SOURCED;
            }
        }
        if (abilityStatus == AbilityStatus.SHOT)
        {
            if (shootBlockFromLoc.getAbilityStatus() == AbilityStatus.COMPLETE)
            {
                this.remove();
                shootBlockFromLoc.remove();
                sPlayer.addCooldown(name, cooldown);
            }
        }
    }

    public void setHasClicked()
    {
        if (abilityStatus == AbilityStatus.SOURCED) {
            if (Blocks.playerLookingAtBlockDisplay(player, raiseBlock.getBlockEntity(), sourceRange, 1)) {
                shootBlockFromLoc = new ShootBlockFromLoc(player, name, raiseBlock.getBlockEntity().getLocation(), raiseBlock.getBlockEntity().getBlock().getMaterial(), true, false, 1);
                raiseBlock.remove();
                abilityStatus = AbilityStatus.SHOT;
            }
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
