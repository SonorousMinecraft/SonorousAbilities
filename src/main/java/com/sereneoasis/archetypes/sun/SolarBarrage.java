package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.BlockSphereBlast;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.AbilityUtils;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.methods.Scheduler;
import com.sereneoasis.util.methods.Vectors;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Iterator;

public class SolarBarrage extends CoreAbility {
    private static final String name = "SolarBarrage";

    private HashMap<BlockSphereBlast, Vector> suns = new HashMap<>();

    private int sunAmount = 10;

    private double currentOrbitRadius = radius*20;

    public SolarBarrage(Player player) {
        super(player, name);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        abilityStatus = AbilityStatus.NOT_SHOT;


        Location aboveLoc = player.getEyeLocation().clone().add(0, radius + 2, 0);

        Vector startVec  =  Vectors.getLeftSide(player, radius*20);

        double angleDiff = (double) 360 / (sunAmount+1);

        for (int i = 0 ; i < sunAmount ; i++) {
            Vector offsetVec = startVec.clone().rotateAroundY(Math.toRadians(angleDiff * i));
            Location sunLoc = aboveLoc.clone().add(offsetVec);
            BlockSphereBlast sun = new BlockSphereBlast(player, name, sunLoc, false);
            suns.put(sun, offsetVec);
        }

        start();

    }

    @Override
    public void progress() {
        if (abilityStatus == AbilityStatus.SHOT) {
            suns.keySet().stream()
                    .filter(blockSphereBlast -> blockSphereBlast.getAbilityStatus() == AbilityStatus.COMPLETE)
                    .forEach(blockSphereBlast -> {
                        blockSphereBlast.revert();
                        SunUtils.blockExplode(player, name, blockSphereBlast.getLoc(), radius * 3, 1);
                    });

            if (suns.keySet().stream().allMatch(blockSphereBlast -> blockSphereBlast.getAbilityStatus() == AbilityStatus.COMPLETE)) {
                this.remove();
            }
        }
        else if (abilityStatus == AbilityStatus.NOT_SHOT){

            Location aboveLoc = player.getEyeLocation().clone().add(0, radius + 2, 0);

            suns.forEach((blockSphereBlast, vector) -> {
                blockSphereBlast.moveToLoc(aboveLoc.clone().add(vector.rotateAroundY(Math.toRadians(9)).clone().normalize().multiply(currentOrbitRadius)));
            });
            currentOrbitRadius -= speed/4;
            if (currentOrbitRadius < radius*5){
                abilityStatus = AbilityStatus.CHARGED;
            }
        } else if (abilityStatus == AbilityStatus.CHARGED){
            Location aboveLoc = player.getEyeLocation().clone().add(0, radius + 2, 0);

            suns.forEach((blockSphereBlast, vector) -> {
                blockSphereBlast.moveToLoc(aboveLoc.clone().add(vector.rotateAroundY(Math.toRadians(9)).clone().normalize().multiply(currentOrbitRadius)));
            });
            AbilityUtils.showCharged(this);
        }
    }

    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.CHARGED) {
            abilityStatus = AbilityStatus.SHOT;
            Iterator<BlockSphereBlast> it = suns.keySet().iterator();
            int currentSun = 0;
            while (it.hasNext()){
                BlockSphereBlast sun = it.next();

                Scheduler.performTaskLater(currentSun, () -> {
                    Location shotLoc = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(radius));
                    sun.moveToLoc(shotLoc);

                    sun.setAbilityStatus(AbilityStatus.SHOT);
                    Particles.spawnParticle(Particle.EXPLOSION_LARGE, shotLoc, 1, 0, 0);
                });
                currentSun+=2;
            }


//            centerSphere.setAbilityStatus(AbilityStatus.SHOT);
        }
    }

    @Override
    public void remove() {
        super.remove();
        suns.keySet().forEach(BlockSphereBlast::remove);
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