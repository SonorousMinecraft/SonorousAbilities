package com.sereneoasis.archetypes.war;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Transformation;

public class Jetpack extends CoreAbility {

    private final String name = "Jetpack";


    private double size = 1.5;

    private Transformation defaultTransformation;


    private BossBar barduration;


    private ArmorStand jetpack;

    public Jetpack(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }
        abilityStatus = AbilityStatus.CHARGING;
        start();
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (System.currentTimeMillis() > startTime + chargeTime + duration) {
            this.remove();
        }



        if (abilityStatus == AbilityStatus.CHARGING) {
            if (player.isSneaking()) {
                if (System.currentTimeMillis() > startTime + chargeTime) {
                    abilityStatus = AbilityStatus.CHARGED;

                    PacketUtils.setClientChestplate(player, Material.ELYTRA);

                    barduration = Bukkit.getServer().createBossBar(name, BarColor.BLUE, BarStyle.SEGMENTED_10);
                    barduration.addPlayer(player);
                    this.jetpack = Display.createArmorStandClip(player.getLocation());

                    jetpack.addPassenger(player);
                    AbilityUtils.showCharged(this);
                }
            } else {
                this.remove();
            }
        } else if (abilityStatus == AbilityStatus.CHARGED) {
            //Vector offsetFix = new Vector(size / 2, 0, size / 2).rotateAroundY(-Math.toRadians(player.getEyeLocation().getYaw()));

//            if (!player.getLocation().subtract(0,0.5,0).getBlock().getType().isSolid() && player.isSneaking()){
//                if (!player.isGliding()) {
//                    Entities.applyPotionPlayerAmplifier(player, PotionEffectType.SLOW_DIGGING,10000000, Math.round(duration));
//                    player.setGliding(true);
//                }
//            }
//            else if (player.getLocation().subtract(0,0.5,0).getBlock().getType().isSolid()){
//                player.setGliding(false);
//                player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
//            }

            Particles.spawnParticleOffset(Particle.FLAME, player.getLocation().subtract(0,1,0), 10, size/6, size/6, size/6, 0);

            Long timeelapsed = System.currentTimeMillis() - (startTime + chargeTime);
            Double progress = 1 - (double) timeelapsed / (double) duration;

            if (progress < 0) {
                barduration.setProgress(0);
            } else {
                barduration.setProgress(progress);
            }

            
        }

    }



    @Override
    public void remove() {
        super.remove();
        PacketUtils.setClientChestplate(player, Material.AIR);
        jetpack.eject();
        jetpack.remove();

    }

    public ArmorStand getArmorStand(){
        return jetpack;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return name;
    }

    public double getSpeed() {
        return speed;
    }
}
















