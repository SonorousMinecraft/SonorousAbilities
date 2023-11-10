package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.BlockSphereBlast;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SolarBarrage extends CoreAbility {
    private final String name = "SolarBarrage";

    private AbilityStatus abilityStatus = AbilityStatus.NOT_SHOT;

    private BlockSphereBlast leftSphere, centerSphere, rightSphere;
    public SolarBarrage(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        Vector dir = player.getEyeLocation().getDirection().multiply(radius);

        Location aboveLoc = player.getEyeLocation().clone().add(0,radius + 2, 0)
                .subtract(dir);

        leftSphere = new BlockSphereBlast(player, name,
                aboveLoc.add(dir.clone().rotateAroundY(Math.toRadians(90))), true );

        centerSphere = new BlockSphereBlast(player, name, aboveLoc, true);

        rightSphere = new BlockSphereBlast(player, name,
                aboveLoc.add(dir.clone().rotateAroundY(Math.toRadians(270))), true );
        start();

    }

    @Override
    public void progress() {
        if (abilityStatus == AbilityStatus.SHOT)
        {
            if (leftSphere.getAbilityStatus() == AbilityStatus.COMPLETE &&
                    centerSphere.getAbilityStatus() == AbilityStatus.COMPLETE &&
                    rightSphere.getAbilityStatus() == AbilityStatus.COMPLETE)
            {
                this.remove();
            }
        }
    }

    public void setHasClicked()
    {
        if (abilityStatus == AbilityStatus.NOT_SHOT)
        {
            abilityStatus = AbilityStatus.SHOT;
            leftSphere.setAbilityStatus(abilityStatus);
            rightSphere.setAbilityStatus(abilityStatus);
            centerSphere.setAbilityStatus(abilityStatus);
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
