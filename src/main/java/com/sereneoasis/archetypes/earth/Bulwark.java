package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.RaiseBlockPillar;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.enhancedmethods.EnhancedBlocks;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Bulwark extends CoreAbility {

    private final String name = "Bulwark";

    private Set<RaiseBlockPillar> raiseBlockPillars = new HashSet<>();

    public Bulwark(Player player) {
        super(player, "Bulwark");

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

//        if (!player.isSneaking()){
//            return;
//        }

        Set<Block> blocks = EnhancedBlocks.getTopCircleBlocks(this);

        if ( ! blocks.isEmpty()){
            for (Block b : blocks) {
                raiseBlockPillars.add(new RaiseBlockPillar(player, name, range, b));
            }

            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (!player.isSneaking()) {
            if (raiseBlockPillars.stream().allMatch(raiseBlockPillar -> raiseBlockPillar.getAbilityStatus() == AbilityStatus.DROPPED)) {
                raiseBlockPillars.forEach(RaiseBlockPillar::revertAllTempDisplayBlocks);
                this.remove();
                sPlayer.addCooldown(name, cooldown);
            } else {
                raiseBlockPillars.forEach(RaiseBlockPillar::drop);
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