package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.util.temp.TB;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class EarthFist extends CoreAbility {

    private Location loc, origin;

    private Vector dir;

    public EarthFist(Player player) {
        super(player, "EarthFist");

        if (shouldStart()){
            this.loc = player.getEyeLocation();
            this.origin = loc.clone();
            this.dir = loc.getDirection();
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (loc.distanceSquared(origin) > range*range){
            this.remove();
            sPlayer.addCooldown("EarthFist", cooldown);
        }
        loc.add(dir.clone().multiply(speed));
        TB tb = new TB(loc.getBlock(), 500, Material.STONE);
    }



    @Override
    public String getName() {
        return "EarthFist";
    }
}
