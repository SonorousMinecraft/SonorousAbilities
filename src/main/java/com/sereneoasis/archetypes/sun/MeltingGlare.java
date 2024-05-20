package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.forcetype.BlockExplodeSphere;
import com.sereneoasis.abilityuilities.blocks.Laser;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Locations;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MeltingGlare extends CoreAbility {

    private Laser leftEye, rightEye;

    private static final String name = "MeltingGlare";

    public MeltingGlare(Player player) {
        super(player, name);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        leftEye = new Laser(player, Locations.getLeftSide(player.getEyeLocation(), 0.2),
                "MeltingGlare", DisplayBlock.SUN);
        rightEye = new Laser(player, Locations.getRightSide(player.getEyeLocation(), 0.2),
                "MeltingGlare", DisplayBlock.SUN);
        start();
    }

    @Override
    public void progress() {
        if (!player.isSneaking()) {
            this.remove();
        }

        if (leftEye.getAbilityStatus() == AbilityStatus.COMPLETE &&
                rightEye.getAbilityStatus() == AbilityStatus.COMPLETE) {
            leftEye.remove();
            rightEye.remove();
            this.remove();
        }

        leftEye.setLoc(Locations.getLeftSide(player.getEyeLocation(), 0.2));
        rightEye.setLoc(Locations.getRightSide(player.getEyeLocation(), 0.2));


        double tempRange = range;
        Vector dir = player.getEyeLocation().getDirection();
        for (double d = 0; d < range; d+=1) {
            tempRange = d;
            if (! player.getEyeLocation().clone().add(dir.clone().multiply(d)).getBlock().isPassable()) {
                break;
            }
        }


        if (tempRange != range) {
            Location facing = Locations.getFacingLocation(player.getEyeLocation(), player.getEyeLocation().getDirection(), tempRange);

            new BlockExplodeSphere(player, name, facing, 2, 0.25);
        }

    }

    @Override
    public void remove() {
        super.remove();
        leftEye.remove();
        rightEye.remove();
        sPlayer.addCooldown("MeltingGlare", cooldown);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return "MeltingGlare";
    }
}