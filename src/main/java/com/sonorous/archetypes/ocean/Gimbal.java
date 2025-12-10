package com.sonorous.archetypes.ocean;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.abilityuilities.blocks.forcetype.BlockRingAroundPlayerGivenType;
import com.sonorous.abilityuilities.blocks.forcetype.ShootBlocksFromLocGivenType;
import com.sonorous.abilityuilities.blocks.forcetype.SourceBlockToPlayerGivenType;
import com.sonorous.archetypes.DisplayBlock;
import com.sonorous.util.AbilityStatus;
import org.bukkit.entity.Player;


public class Gimbal extends CoreAbility {

    private static final String name = "Gimbal";

    private SourceBlockToPlayerGivenType sourceBlockToPlayerGivenType;

    //1 is shot first
    private BlockRingAroundPlayerGivenType blockRingAroundPlayerGivenType1;

    private BlockRingAroundPlayerGivenType blockRingAroundPlayerGivenType2;

    private ShootBlocksFromLocGivenType shootBlocksFromLocGivenType1;

    private ShootBlocksFromLocGivenType shootBlocksFromLocGivenType2;

    private boolean hasSourced = false;
    private boolean hasShot1 = false;
    private boolean hasShot2 = false;


    public Gimbal(Player player) {
        super(player, name);

        if (shouldStart()) {
            sourceBlockToPlayerGivenType = new SourceBlockToPlayerGivenType(player, name, DisplayBlock.WATER, 4);
            if (!(sourceBlockToPlayerGivenType.getSourceStatus() == AbilityStatus.NO_SOURCE)) {
                sourceBlockToPlayerGivenType.setAbilityStatus(AbilityStatus.SOURCE_SELECTED);
                start();
            }
        }


    }

    public void setHasSourced() {
        sourceBlockToPlayerGivenType.setAbilityStatus(AbilityStatus.SOURCING);
    }

    @Override
    public void progress() {

        if (!hasSourced && sourceBlockToPlayerGivenType.getSourceStatus() == AbilityStatus.SOURCED) {

            hasSourced = true;
            blockRingAroundPlayerGivenType1 = new BlockRingAroundPlayerGivenType(player, name, sourceBlockToPlayerGivenType.getLocation(),
                    DisplayBlock.WATER, radius, 45, 30, true);
            blockRingAroundPlayerGivenType2 = new BlockRingAroundPlayerGivenType(player, name, sourceBlockToPlayerGivenType.getLocation(),
                    DisplayBlock.WATER, radius, -45, 30, false);
            sourceBlockToPlayerGivenType.remove();
        }


        if (hasShot1) {

            if (shootBlocksFromLocGivenType1 != null && shootBlocksFromLocGivenType1.getAbilityStatus() == AbilityStatus.COMPLETE) {

                shootBlocksFromLocGivenType1.remove();
            }
            if (hasShot2) {
                if (shootBlocksFromLocGivenType2 != null && shootBlocksFromLocGivenType2.getAbilityStatus() == AbilityStatus.COMPLETE) {

                    shootBlocksFromLocGivenType2.remove();
                    this.remove();
                }
            }
        }
    }

    public void setHasClicked() {
        if (hasSourced) {

            if (!hasShot1) {
                hasShot1 = true;
                shootBlocksFromLocGivenType1 = new ShootBlocksFromLocGivenType(player, name, player.getEyeLocation(), DisplayBlock.WATER, true, false);
                blockRingAroundPlayerGivenType1.remove();
            } else {
                if (!hasShot2) {
                    hasShot2 = true;
                    shootBlocksFromLocGivenType2 = new ShootBlocksFromLocGivenType(player, name, player.getEyeLocation(), DisplayBlock.WATER, true, false);
                    blockRingAroundPlayerGivenType2.remove();
                }
            }
        }
    }

    @Override
    public void remove() {
        super.remove();
        if (blockRingAroundPlayerGivenType1 != null) {
            blockRingAroundPlayerGivenType1.remove();
        }
        if (blockRingAroundPlayerGivenType2 != null) {
            blockRingAroundPlayerGivenType2.remove();
        }
        sPlayer.addCooldown(this.getName(), cooldown);
    }


    @Override
    public String getName() {
        return name;
    }
}