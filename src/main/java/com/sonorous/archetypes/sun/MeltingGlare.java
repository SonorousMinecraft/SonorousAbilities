package com.sonorous.archetypes.sun;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.abilityuilities.blocks.Laser;
import com.sonorous.archetypes.DisplayBlock;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.Locations;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MeltingGlare extends CoreAbility {

    private static final String name = "MeltingGlare";
    private Laser leftEye, rightEye;

    public MeltingGlare(Player player) {
        super(player, name);

        if (shouldStart()) {

            leftEye = new Laser(player, Locations.getLeftSide(player.getEyeLocation(), 0.25),
                    "MeltingGlare", DisplayBlock.SUN);
            rightEye = new Laser(player, Locations.getRightSide(player.getEyeLocation(), 0.25),
                    "MeltingGlare", DisplayBlock.SUN);
            start();
        }


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

        leftEye.setLoc(Locations.getLeftSide(player.getEyeLocation(), 0.25));
        rightEye.setLoc(Locations.getRightSide(player.getEyeLocation(), 0.25));


        double tempRange = range;
        Vector dir = player.getEyeLocation().getDirection();
        for (double d = 0; d < range; d += 1) {
            tempRange = d;
            if (!player.getEyeLocation().clone().add(dir.clone().multiply(d)).getBlock().isPassable()) {
                break;
            }
        }


        if (tempRange != range) {
            Location facing = Locations.getFacingLocation(player.getEyeLocation(), player.getEyeLocation().getDirection(), tempRange);

            if (player.getLocation().getPitch() > 50) {
                player.setVelocity(dir.clone().multiply(-speed * 2));
            }
            SunUtils.blockExplode(player, name, facing, 2, 0.25);
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