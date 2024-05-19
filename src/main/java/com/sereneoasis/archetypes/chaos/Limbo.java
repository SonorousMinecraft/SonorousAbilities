package com.sereneoasis.archetypes.chaos;

import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Particles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Limbo extends MasterAbility {

    private static final String name = "Limbo";

    private LimboStates state;
    public Limbo(Player player) {
        super(player, name);

        if (shouldStart()){
            applyPotionEffects();
            start();
            state = LimboStates.LIMBO;
        }
    }

    private void applyPotionEffects(){
        Entities.applyPotionPlayerAmplifier(player, PotionEffectType.SPEED, 4, (int) duration);
        Entities.applyPotionPlayer(player, PotionEffectType.JUMP, (int) duration);
        Entities.applyPotionPlayer(player, PotionEffectType.INVISIBILITY, (int) duration);
        Entities.applyPotionPlayer(player, PotionEffectType.GLOWING, (int) duration);
        Entities.applyPotionPlayer(player, PotionEffectType.WEAKNESS, (int) duration);


    }

    private void removePotionEffects(){
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.removePotionEffect(PotionEffectType.WEAKNESS);
        player.removePotionEffect(PotionEffectType.GLOWING);
    }

    @Override
    public void progress() throws ReflectiveOperationException {


        if (state == LimboStates.LIMBO){
            Particles.spawnParticle(Particle.SONIC_BOOM, player.getLocation(), 1, 0, 0);

            if (isAgainstWall()){
                player.setVelocity(player.getEyeLocation().getDirection().clone().multiply( speed));
            }

            if (player.getLocation().getBlock().getType() == Material.AIR && player.getLocation().getBlock().getLocation().subtract(0,1,0).getBlock().isLiquid()){
                player.setVelocity(player.getEyeLocation().getDirection().setY(0.1).normalize().clone().multiply( speed));

            }
        }
    }

    public void setHasClicked(){
        if (state == LimboStates.LIMBO){
            state = LimboStates.NORMAL;
            removePotionEffects();
        } else {
            state = LimboStates.LIMBO;
            applyPotionEffects();
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
    }
}
