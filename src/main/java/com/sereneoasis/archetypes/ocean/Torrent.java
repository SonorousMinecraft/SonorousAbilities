package com.sereneoasis.archetypes.ocean;


import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.forcetype.BlockRingAroundPlayerGivenType;
import com.sereneoasis.abilityuilities.blocks.forcetype.ShootBlocksFromLocGivenType;
import com.sereneoasis.abilityuilities.blocks.forcetype.SourceBlockToPlayerGivenType;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Sakrajin
 */
public class Torrent extends CoreAbility {

    private static final String name = "Torrent";


    private SourceBlockToPlayerGivenType sourceBlockToPlayerGivenType;

    private BlockRingAroundPlayerGivenType blockRingAroundPlayerGivenType;

    private ShootBlocksFromLocGivenType shootBlocksFromLocGivenType;


    public Torrent(Player player) {
        super(player, name);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(name)) {
            return;
        }

        sourceBlockToPlayerGivenType = new SourceBlockToPlayerGivenType(player, name, DisplayBlock.WATER, 1);
        if (!(sourceBlockToPlayerGivenType.getSourceStatus() == AbilityStatus.NO_SOURCE)) {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            sourceBlockToPlayerGivenType.setAbilityStatus(AbilityStatus.SOURCE_SELECTED);
            start();
        }
    }

    public void setHasSourced() {
        sourceBlockToPlayerGivenType.setAbilityStatus(AbilityStatus.SOURCING);
        abilityStatus = AbilityStatus.SOURCING;
    }


    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.SOURCING) {
            if (!player.isSneaking()) {
                sourceBlockToPlayerGivenType.remove();
                this.remove();
            }

            if (sourceBlockToPlayerGivenType.getSourceStatus() == AbilityStatus.SOURCED) {
                abilityStatus = AbilityStatus.SOURCED;
                blockRingAroundPlayerGivenType = new BlockRingAroundPlayerGivenType(player, name, sourceBlockToPlayerGivenType.getLocation(), DisplayBlock.WATER,
                        radius, 0, 45, true);
                sourceBlockToPlayerGivenType.remove();
            }
        }


        else if (abilityStatus == AbilityStatus.SHOT) {
            if (shootBlocksFromLocGivenType == null) {
//                Bukkit.broadcastMessage("shit" + blockRingAroundPlayerGivenType.getLocation().distance(player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(4))));
//                if (blockRingAroundPlayerGivenType.getLocation().distanceSquared(player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(4))) < 1) {
                if (blockRingAroundPlayerGivenType.isReadyToShoot()) {
                    shootBlocksFromLocGivenType = new ShootBlocksFromLocGivenType(player, name, player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(blockRingAroundPlayerGivenType.getRingSize() /2)), DisplayBlock.WATER, true, true);
                    blockRingAroundPlayerGivenType.remove();
                }
            } else {

                if (shootBlocksFromLocGivenType.getAbilityStatus() == AbilityStatus.COMPLETE) {
                    shootBlocksFromLocGivenType.remove();

                    this.remove();
                }
            }
        }

        else if (abilityStatus == AbilityStatus.SOURCED && !player.isSneaking())
        {
            this.remove();
        }
    }


    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.SOURCED) {
            abilityStatus = AbilityStatus.SHOT;

        }
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(this.getName(), this.cooldown);
        if (sourceBlockToPlayerGivenType != null) {
            sourceBlockToPlayerGivenType.remove();
        }
        if (blockRingAroundPlayerGivenType != null) {
            blockRingAroundPlayerGivenType.remove();
        }
        if (shootBlocksFromLocGivenType != null) {
            shootBlocksFromLocGivenType.remove();
        }
    }


    @Override
    public String getName() {
        return name;
    }
}