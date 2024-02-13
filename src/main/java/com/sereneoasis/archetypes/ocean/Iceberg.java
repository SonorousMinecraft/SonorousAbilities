package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.BlockSmashSourced;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.entity.Player;

/**
 * @author Sakrajin
 */
public class Iceberg extends CoreAbility {

    private final String name = "Iceberg";

    private BlockSmashSourced blockSmashSourced;

    public Iceberg(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(name)) {
            return;
        }

        blockSmashSourced = new BlockSmashSourced(player, name, DisplayBlock.ICE);
        if (blockSmashSourced.getAbilityStatus() == AbilityStatus.SOURCE_SELECTED)
        {
            start();
        }
    }

    @Override
    public void progress() {

        if (blockSmashSourced.getAbilityStatus() == AbilityStatus.COMPLETE){
            blockSmashSourced.remove();
            this.remove();
        }

    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
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
