package com.sonorous.archetypes.sky;

import com.sonorous.SonorousAbilitiesPlayer;
import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.abilityuilities.particles.Arc;
import com.sonorous.util.AbilityStatus;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;


public class Shocker extends CoreAbility {

    private static final String name = "Shocker";
    private Location origin, locleft, locright, loc, closestleft, closestright;
    private Vector dir;

    private ArrayList<Location> leftarcs;
    private ArrayList<Location> rightarcs;

    private Entity e;
    private Player target;
    private SonorousAbilitiesPlayer btarget;
    private double distance;

    private Arc leftArc, rightArc;

    public Shocker(Player player) {
        super(player, name);

        if (shouldStart()) {
            //        setFields();
            leftArc = new Arc(player, name, Particle.ELECTRIC_SPARK, Arc.OutputLocation.OFFHAND);
            rightArc = new Arc(player, name, Particle.ELECTRIC_SPARK, Arc.OutputLocation.MAINHAND);
            start();
            return;
        }


    }

//    private void setFields() {
//        this.origin = player.getEyeLocation();
//        this.dir = origin.getDirection();
//        this.loc = origin.clone().add(dir);
//
//        this.locleft = Locations.getLeftSide(loc, 0.5);
//        this.closestleft = locleft.clone();
//        this.locright = Locations.getRightSide(loc, 0.5);
//        this.closestright = locright.clone();
//
//        this.leftarcs = new ArrayList<>();
//        leftarcs.add(closestleft);
//        this.rightarcs = new ArrayList<>();
//        rightarcs.add(closestright);
//
//        this.distance = 0;
//    }

    @Override
    public void progress() {

        if (leftArc.getAbilityStatus() == AbilityStatus.COMPLETE) {
            leftArc.remove();
            rightArc.remove();
            this.remove();
        }
//        } else {
//            leftArc.getLocs().forEach(location -> location.add(Vectors.getVectorToOffHand(player)));
//            rightArc.getLocs().forEach(location -> location.add(Vectors.getVectorToMainHand(player)));
//        }
//        if (!player.isSneaking()) {
//            this.remove();
//        }
//
//        if (System.currentTimeMillis() > startTime + duration) {
//            this.remove();
//        }
//
//        if (player.isDead() || !player.isOnline()) {
//            this.remove();
//        }
//
//        distance = loc.distance(origin);
//
//
//        loc.add(dir.clone().multiply(speed));
//
//
//        if (Entities.getAffected(loc, hitbox, player) != null) {
//            e = Entities.getAffected(loc, hitbox, player);
//            if (e instanceof Player) {
//                target = (Player) e;
//                btarget = SonorousAbilitiesPlayer.getSonorousAbilitiesPlayer(target);
//                if (btarget != null) {
//                    if (btarget.getHeldAbility().equalsIgnoreCase(name) && target.isSneaking()) {
////                        Particles.spawnColoredParticle(target.getLocation(), 10, 1, 1, Color.fromRGB(1, 225, 255));
//                        new Shocker(target);
//                        this.remove();
//                    } else {
//                        DamageHandler.damageEntity(e, player, this, damage);
//                        setFields();
//                    }
//                } else {
//                    DamageHandler.damageEntity(e, player, this, damage);
//                    setFields();
//                }
//            } else {
//                DamageHandler.damageEntity(e, player, this, damage);
//                setFields();
//            }
//        }
//
//        locleft.add(dir.clone().multiply(speed));
//        if (locleft.distance(closestleft) > 1) {
//            closestleft = randomMidwayVertex(locleft, closestleft);
//            if (closestleft != null) {
//                leftarcs.add(closestleft);
//            }
//        }
//
//        for (int i = 0; i < leftarcs.size() - 2; i++) {
//            playParticlesBetweenPoints(leftarcs.get(i), leftarcs.get(i + 1));
//        }
//
//
//        locright.add(dir.clone().multiply(speed));
//        if (locright.distance(closestright) > 1) {
//            closestright = randomMidwayVertex(locright, closestright);
//            if (closestright != null) {
//                rightarcs.add(closestright);
//            }
//        }
//
//        for (int i = 0; i < rightarcs.size() - 2; i++) {
//            playParticlesBetweenPoints(rightarcs.get(i), rightarcs.get(i + 1));
//        }
//
//
//
//        if (distance > range) {
//            setFields();
//        }
//
//        if (loc.getBlock().getType().isSolid()) {
//            setFields();
//        }
    }

//    public Location randomMidwayVertex(Location start, Location end) {
//        Vector midpoint = end.clone().subtract(start.clone()).toVector().multiply(0.5);
//        Vector random = Vector.getRandom().normalize().add(new Vector(-0.5,-0.5,-0.5));
//        if (distance > 0){
//            random.multiply(Math.log(distance));
//        }
//        return (start.clone().add(midpoint).add(random));
//    }
//
//    public void playParticlesBetweenPoints(Location start, Location end) {
//        Vector difference = Vectors.getDirectionBetweenLocations(start,end);
//        double distance = difference.length();
//        Vector normalised = difference.clone().normalize();
//
//        for (double d = 0; d < distance; d += size) {
//            Location temploc = start.clone().add(normalised.clone().multiply(d));
//            //Particles.spawnColoredParticle(temploc, 1, 0.05, 1, Color.fromRGB(1, 225, 255));
//            TDBs.playTDBs(temploc, DisplayBlock.LIGHTNING, 1, size, Math.log(d+1));
//            //Particles.spawnColoredParticle(temploc, 11, 1, 1, Color.fromRGB(1, 225, 255));
//
//            Vector random = Vector.getRandom().normalize().add(new Vector(-0.5,-0.5,-0.5)).normalize().add(dir.clone().multiply(0.2)).normalize().multiply(0.4);
//            Particles.spawnParticleOffset(Particle.END_ROD, temploc, 0, random.getX(), random.getY(), random.getZ(), 0.15);
//            Particles.spawnColoredParticle(temploc, 1, Math.log(d+1), size*3, Color.fromRGB(1, 225, 255));
//            Particles.spawnParticle(Particle.ELECTRIC_SPARK, temploc, 1,Math.log(d+1),0);
//        }
//    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
    }

    @Override
    public String getName() {
        return name;
    }
}