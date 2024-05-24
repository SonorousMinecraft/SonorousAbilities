package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public class WaterSpout extends MasterAbility {
    public static final String name = "WaterSpout";

    private boolean isAllowedToFly;

    private long sinceLastSourced = System.currentTimeMillis();

    private long sinceLastSetPlayerLoc = System.currentTimeMillis(), intervalBetweenPlayerLocSet = 200;
    private Location previousPlayerLoc;


    public WaterSpout(Player player) {
        super(player, name);

        if (shouldStart()){
            isAllowedToFly = player.getAllowFlight();
            sPlayer.setFly(this);
            abilityStatus = AbilityStatus.MOVING;
            this.previousPlayerLoc = player.getLocation();
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (System.currentTimeMillis() - startTime > duration) {
            this.remove();
        }

        switch (abilityStatus){
            case MOVING -> {
                Block floor = Blocks.getFacingBlockOrLiquid(player.getEyeLocation(), new Vector(0,-1,0), range);
                if (floor !=null && floor.getType().equals(Material.WATER)){
                    double distance = player.getLocation().getBlockY() - floor.getY();
                    List<Location> spoutLocs = Locations.getPointsAlongLine(player.getLocation(), player.getLocation().subtract(0, distance , 0), size/2);

//                        Vector flyVelocity = Vectors.getDirectionBetweenLocations(previousSpoutFloor.getLocation(), floor.getLocation());
                    Vector flyVelocity = Vectors.getDirectionBetweenLocations(previousPlayerLoc, player.getLocation());
                    spoutLocs.stream()
                                .map(location -> location.clone().subtract(flyVelocity.clone().multiply(Math.log(Math.max(1,player.getLocation().getY() - location.getY())))))
                            .forEach(location -> new TempDisplayBlock(location, DisplayBlock.WATER, 100, size));


                    sinceLastSourced = System.currentTimeMillis();

                    if (System.currentTimeMillis() - sinceLastSetPlayerLoc > intervalBetweenPlayerLocSet){
                        previousPlayerLoc = player.getLocation();
                        sinceLastSetPlayerLoc = System.currentTimeMillis();
                    }

                }
                else if (System.currentTimeMillis() - sinceLastSourced > 500) {
                    this.remove();
                }
            }
        }
    }

    public void setHasClicked(){
        switch (abilityStatus) {
            case MOVING -> {
                abilityStatus = AbilityStatus.NOT_MOVING;
                sPlayer.removeFly(this);
            }
            case NOT_MOVING -> {
                abilityStatus = AbilityStatus.MOVING;
                sPlayer.setFly(this);
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
        sPlayer.removeFly(this);
    }
}
