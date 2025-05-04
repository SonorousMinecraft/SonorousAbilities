package com.sonorous.archetypes.earth;


import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.abilityuilities.blocks.BlockRingAroundPlayer;
import com.sonorous.abilityuilities.blocks.ShootBlocksFromLoc;
import com.sonorous.abilityuilities.blocks.SourceBlockToPlayer;
import com.sonorous.util.AbilityStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class RockRing extends CoreAbility {

    private final String name = "RockRing";


    private SourceBlockToPlayer sourceBlockToPlayer;

    private BlockRingAroundPlayer blockRingAroundPlayer;

    private ShootBlocksFromLoc shootBlocksFromLoc;

    private Material type;


    public RockRing(Player player) {
        super(player, "RockRing");

        if (shouldStart()) {

            sourceBlockToPlayer = new SourceBlockToPlayer(player, name, 4, 30);
            if (!(sourceBlockToPlayer.getSourceStatus() == AbilityStatus.NO_SOURCE)) {
                abilityStatus = AbilityStatus.SOURCE_SELECTED;
                this.type = sourceBlockToPlayer.getType();
                sourceBlockToPlayer.setAbilityStatus(AbilityStatus.SOURCE_SELECTED);
                start();
            }
        }
    }

    public void setHasSourced() {
        sourceBlockToPlayer.setAbilityStatus(AbilityStatus.SOURCING);
        abilityStatus = AbilityStatus.SOURCING;
    }


    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.SOURCING) {
            if (!player.isSneaking()) {
                sourceBlockToPlayer.remove();
                this.remove();
            }

            if (sourceBlockToPlayer.getSourceStatus() == AbilityStatus.SOURCED) {
                abilityStatus = AbilityStatus.SOURCED;
                blockRingAroundPlayer = new BlockRingAroundPlayer(player, name, sourceBlockToPlayer.getLocation(), type,
                        radius, 0, 30, true);
                sourceBlockToPlayer.remove();
            }
        } else if (abilityStatus == AbilityStatus.SHOT) {
            if (shootBlocksFromLoc.getAbilityStatus() == AbilityStatus.COMPLETE) {
                shootBlocksFromLoc.remove();

                this.remove();
            }
        } else if (abilityStatus == AbilityStatus.SOURCED && !player.isSneaking()) {
            this.remove();
        }
    }


    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.SOURCED) {
            abilityStatus = AbilityStatus.SHOT;
            shootBlocksFromLoc = new ShootBlocksFromLoc(player, name, player.getEyeLocation(), type, false, true, 30);
            blockRingAroundPlayer.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(this.getName(), this.cooldown);
        if (sourceBlockToPlayer != null) {
            sourceBlockToPlayer.remove();
        }
        if (blockRingAroundPlayer != null) {
            blockRingAroundPlayer.remove();
        }
        if (shootBlocksFromLoc != null) {
            shootBlocksFromLoc.remove();
        }
    }


    @Override
    public String getName() {
        return name;
    }
}