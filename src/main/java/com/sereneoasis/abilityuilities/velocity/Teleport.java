package com.sereneoasis.abilityuilities.velocity;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {

            if (abilityStatus == AbilityStatus.TELEPORTING && System.currentTimeMillis() > startTime + chargeTime) {
                Location hitLoc = Blocks.getFacingBlockOrLiquidLoc(player, distance);

                Vector dir = player.getEyeLocation().getDirection();

                if (hitLoc == null) {
                    targetLoc = Locations.getFacingLocation(player.getLocation(), dir, distance);
                }
                else {
                    targetLoc = hitLoc.subtract(dir);
                }

                this.origin = player.getLocation();
                Bukkit.broadcastMessage("teleporting");
                player.teleport(targetLoc.setDirection(dir));
                abilityStatus = AbilityStatus.COMPLETE;
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
