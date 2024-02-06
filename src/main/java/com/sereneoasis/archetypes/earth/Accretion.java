package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.RaiseBlock;
import com.sereneoasis.abilityuilities.blocks.ShootBlockFromLoc;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Accretion extends CoreAbility {

    private List<RaiseBlock> raiseBlocks;

    private List<ShootBlockFromLoc> shootBlocksFromLoc;

    private final String name = "Accretion";

    private int shots = 5;


    public Accretion(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        boolean hasSources = false;
        raiseBlocks = new ArrayList<>();
        for (int i = 0; i < shots; i++) {
            RaiseBlock raiseBlock = new RaiseBlock(player, name, 3-size, false);
            if (raiseBlock.getAbilityStatus() == AbilityStatus.SOURCE_SELECTED) {
                raiseBlocks.add(raiseBlock);
                hasSources = true;
            }
        }
        if (hasSources) {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (abilityStatus == AbilityStatus.SOURCE_SELECTED) {
            abilityStatus = AbilityStatus.SOURCED;
        }
        if (abilityStatus == AbilityStatus.SHOT) {
            if (shootBlocksFromLoc.stream().allMatch(shotBlock -> (shotBlock.getAbilityStatus() == AbilityStatus.COMPLETE))) {
                this.remove();
                shootBlocksFromLoc.forEach(ShootBlockFromLoc::remove);
                sPlayer.addCooldown(name, cooldown);
            }
        }
    }

    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.SOURCED) {
            shootBlocksFromLoc = new ArrayList<>();

            for (int i = 0; i < raiseBlocks.size(); i++) {
                shootBlocksFromLoc.add(new ShootBlockFromLoc(player, name, raiseBlocks.get(i).getBlockEntity().getLocation().add(Vector.getRandom()),
                        raiseBlocks.get(i).getBlockEntity().getBlock().getMaterial(), false, false));
                abilityStatus = AbilityStatus.SHOT;
            }
            raiseBlocks.forEach(RaiseBlock::remove);
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
