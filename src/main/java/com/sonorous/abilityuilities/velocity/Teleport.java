package com.sonorous.abilityuilities.velocity;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.Blocks;
import com.sonorous.util.methods.Locations;
import com.sonorous.util.methods.Particles;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Teleport extends CoreAbility {

    private final String name;

    private Location targetLoc, origin;

    private double distance;

    public Teleport(Player player, String name, double distance) {
        super(player, name);
        this.name = name;

        if (shouldStartCanHaveMultiple()) {
            this.distance = distance;
            abilityStatus = AbilityStatus.TELEPORTING;

            Location hitLoc = Blocks.getFacingBlockOrLiquidLoc(player, distance);

            Vector dir = player.getEyeLocation().getDirection().clone();

            if (hitLoc == null) {
                targetLoc = Locations.getFacingLocation(player.getEyeLocation(), dir, distance).subtract(0, 1, 0);
            } else {
                targetLoc = hitLoc.clone().subtract(dir.clone());
            }
//            Particles.spawnVibrationParticleEntity(targetLoc, 10, 0.2, 1, player, Math.round((float) chargeTime /50));
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (abilityStatus == AbilityStatus.TELEPORTING) {
            if (System.currentTimeMillis() > startTime + chargeTime) {

                targetLoc.setDirection(player.getEyeLocation().getDirection());
                this.origin = player.getLocation().clone();
                player.teleport(targetLoc);
                Particles.spawnParticle(Particle.FLASH, targetLoc, 1, 0, 0);

                abilityStatus = AbilityStatus.COMPLETE;
            } else {
                Particles.spawnParticleOffset(Particle.SCULK_SOUL, targetLoc, 10, 0.25, 1, 0.25, 0);
                Particles.spawnVibrationParticleBlock(player.getLocation(), 1, 0, 1, targetLoc.getBlock(), Math.round((float) (chargeTime - (System.currentTimeMillis() - startTime)) / 50));
//                    Particles.spawnVibrationParticleEntity(targetLoc, 10, 0.2, 0, player, 20);

            }
        }


    }

    public Location getOrigin() {
        return origin;
    }

    @Override
    public String getName() {
        return name;
    }
}
