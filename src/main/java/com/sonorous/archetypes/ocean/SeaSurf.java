package com.sonorous.archetypes.ocean;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.abilityuilities.velocity.Skate;
import com.sonorous.archetypes.DisplayBlock;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.Blocks;
import com.sonorous.util.methods.Constants;
import com.sonorous.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class SeaSurf extends CoreAbility {

    private static final String name = "SeaSurf";
    private Skate skate;

    private final Set<TempDisplayBlock> surf = new HashSet<>();


    public SeaSurf(Player player) {
        super(player, name);

        if (shouldStart()) {
            this.skate = new Skate(player, name, 10, 3, false);
            start();
        }


    }

    @Override
    public void progress() {
        if (skate.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
        }

        Location tempLoc = player.getEyeLocation();
        tempLoc.setPitch(0);

        if (skate.getFloorBlock() != null) {
            Blocks.getBlocksAroundPoint(skate.getFloorBlock().getLocation(), radius)
                    .stream()
                    .filter(block -> block.getType().equals(Material.WATER) && block.getRelative(BlockFace.UP).getType().equals(Material.AIR))
                    .forEach(block -> {
                        TempDisplayBlock tempDisplayBlock = new TempDisplayBlock(block, DisplayBlock.WATER, 500, 1);
                        surf.add(tempDisplayBlock);
                    });
        }

        surf.forEach(tempDisplayBlock -> tempDisplayBlock.moveToAndMaintainFacing(tempDisplayBlock.getLoc().add(0, Math.random() * Constants.BLOCK_RAISE_SPEED * speed * 5, 0)));

        if (!sPlayer.getHeldAbility().equals(name)) {
            this.remove();
        }
    }

    public void setHasClicked() {
        this.remove();
    }

    public ArmorStand getArmorStand() {
        return skate.getArmorStand();
    }

    @Override
    public void remove() {
        super.remove();
        if (skate != null) {
            skate.remove();
        }
        sPlayer.addCooldown(name, cooldown);
    }


    @Override
    public String getName() {
        return name;
    }
}