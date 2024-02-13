package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.BlockSphereBlast;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Locations;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SolarBarrage extends CoreAbility {
    private final String name = "SolarBarrage";


    private BlockSphereBlast leftSphere, centerSphere, rightSphere;

    public SolarBarrage(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        abilityStatus = AbilityStatus.NOT_SHOT;

        Vector dir = player.getEyeLocation().getDirection().clone();

        Location aboveLoc = player.getEyeLocation().clone().add(0, radius + 2, 0)
                .subtract(dir);

        leftSphere = new BlockSphereBlast(player, name,
                Locations.getLeftSide(aboveLoc, radius), true);

        centerSphere = new BlockSphereBlast(player, name, aboveLoc, true);

        rightSphere = new BlockSphereBlast(player, name,
                Locations.getRightSide(aboveLoc, radius), true);
        start();

    }

    @Override
    public void progress() {
        if (abilityStatus == AbilityStatus.SHOT) {
            if (leftSphere.getAbilityStatus() == AbilityStatus.COMPLETE &&
                    centerSphere.getAbilityStatus() == AbilityStatus.COMPLETE &&
                    rightSphere.getAbilityStatus() == AbilityStatus.COMPLETE) {
                this.remove();
            }
        }
    }

    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.NOT_SHOT) {
            abilityStatus = AbilityStatus.SHOT;
            leftSphere.setAbilityStatus(AbilityStatus.SHOT);
            rightSphere.setAbilityStatus(AbilityStatus.SHOT);
            centerSphere.setAbilityStatus(AbilityStatus.SHOT);
        }
    }

    @Override
    public void remove() {
        super.remove();
        leftSphere.remove();
        rightSphere.remove();
        centerSphere.remove();
        sPlayer.addCooldown(name, cooldown);
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
