package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.RaiseBlockPillar;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.enhancedmethods.EnhancedBlocks;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Bulwark extends CoreAbility {

    private static final String name = "Bulwark";

    private Set<RaiseBlockPillar> raiseBlockPillars = new HashSet<>();

    public Bulwark(Player player) {
        super(player, name);

        if (shouldStart()) {
            Set<Block> blocks = EnhancedBlocks.getTopHCircleBlocks(this);

            if ( ! blocks.isEmpty()){
                for (Block b : blocks) {
                    raiseBlockPillars.add(new RaiseBlockPillar(player, name, range, b));
                }

                start();
            }
        }

//        if (!player.isSneaking()){
//            return;
//        }


    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (!player.isSneaking()) {
            if (raiseBlockPillars.stream().allMatch(raiseBlockPillar -> raiseBlockPillar.getAbilityStatus() == AbilityStatus.DROPPED)) {
                raiseBlockPillars.forEach(RaiseBlockPillar::revertAllTempDisplayBlocks);
                this.remove();
            } else {
                raiseBlockPillars.forEach(RaiseBlockPillar::drop);

            }
        }
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);

//        sPlayer.addCooldown(name, cooldown);
    }

    @Override
    public String getName() {
        return name;
    }
}