package com.sonorous.archetypes.chaos;

import com.sonorous.ability.superclasses.MasterAbility;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.AbilityUtils;
import com.sonorous.util.methods.BossBarUtils;
import com.sonorous.util.methods.Entities;
import com.sonorous.util.methods.Particles;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;

public class Limbo extends MasterAbility {

    private static final String name = "Limbo";

    private LimboStates state;

    private long sinceStartedChargingJump;

    private BossBar bar;


    public Limbo(Player player) {
        super(player, name);

        if (shouldStart()) {

            bar = BossBarUtils.initBar(player, name, BarColor.PURPLE);

            state = LimboStates.LIMBO;
            AbilityUtils.sendActionBar(player, "ENABLED", ChatColor.of(Color.MAGENTA));
            applyPotionEffects();
            start();
        }
    }

    private void applyPotionEffects() {
        Entities.applyPotionPlayerAmplifier(player, PotionEffectType.SPEED, 4, (int) duration);
        Entities.applyPotionPlayer(player, PotionEffectType.JUMP, (int) duration);
        Entities.applyPotionPlayer(player, PotionEffectType.INVISIBILITY, (int) duration);
        Entities.applyPotionPlayer(player, PotionEffectType.GLOWING, (int) duration);
        Entities.applyPotionPlayer(player, PotionEffectType.WEAKNESS, (int) duration);


    }

    private void removePotionEffects() {
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.removePotionEffect(PotionEffectType.WEAKNESS);
        player.removePotionEffect(PotionEffectType.GLOWING);
    }

    @Override
    public void progress() throws ReflectiveOperationException {

        BossBarUtils.manageBarDuration(bar, player, startTime, duration);

        if (System.currentTimeMillis() - startTime > duration) {
            this.remove();
        }

        if (state == LimboStates.LIMBO) {
            Particles.spawnParticle(Particle.SONIC_BOOM, player.getLocation().subtract(0, 1, 0), 1, 0, 0);

//            if (isAgainstWall()){
//                player.setVelocity(player.getEyeLocation().getDirection().clone().multiply( speed));
//            }

            if (player.getLocation().getBlock().getType() == Material.AIR && player.getLocation().getBlock().getLocation().subtract(0, 1, 0).getBlock().isLiquid()) {
                player.setVelocity(player.getEyeLocation().getDirection().setY(0.1).normalize().clone().multiply(speed));
            }

            if (sPlayer.getHeldAbility().equals(name)) {
                if (player.isSneaking()) {
                    if (abilityStatus == AbilityStatus.MOVING) {
                        abilityStatus = AbilityStatus.CHARGING;
                        sinceStartedChargingJump = System.currentTimeMillis();

                    } else {
                        long chargedFor = System.currentTimeMillis() - sinceStartedChargingJump;
                        if (chargedFor > chargeTime) {
                            abilityStatus = AbilityStatus.CHARGED;
                            AbilityUtils.sendActionBar(player, "READY", ChatColor.of(Color.MAGENTA));
                        } else {
                            AbilityUtils.sendActionBar(player, "CHARGING", ChatColor.of(Color.MAGENTA));

                        }
                    }
                } else {
                    if (abilityStatus == AbilityStatus.CHARGED) {
                        Particles.spawnParticle(Particle.SONIC_BOOM, player.getLocation(), 20, 2, 1);
                        player.setVelocity(player.getEyeLocation().getDirection().multiply(speed * 5));
                        abilityStatus = AbilityStatus.MOVING;
                    }
                }
            }
        }
    }

    public void setHasClicked() {
        if (state == LimboStates.LIMBO) {
            state = LimboStates.NORMAL;
            removePotionEffects();
            AbilityUtils.sendActionBar(player, "DISABLED", ChatColor.of(Color.MAGENTA));

        } else {
            state = LimboStates.LIMBO;
            applyPotionEffects();

            AbilityUtils.sendActionBar(player, "ENABLED", ChatColor.of(Color.MAGENTA));

        }
    }

    private boolean isAgainstWall() {
        Location location = player.getLocation();
        if (location.getBlock().getRelative(BlockFace.NORTH).getType().isSolid()) {
            return true;
        } else if (location.getBlock().getRelative(BlockFace.SOUTH).getType().isSolid()) {
            return true;
        } else if (location.getBlock().getRelative(BlockFace.WEST).getType().isSolid()) {
            return true;
        } else return location.getBlock().getRelative(BlockFace.EAST).getType().isSolid();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void remove() {
        super.remove();
        removePotionEffects();
        sPlayer.addCooldown(name, cooldown);
        bar.removePlayer(player);
    }
}
