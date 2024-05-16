package com.sereneoasis.archetypes.chaos;

import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.PacketUtils;
import com.sereneoasis.util.temp.TempBlock;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class AbyssalFall extends MasterAbility {

    private static final String name = "AbyssalFall";

    private Vector dir;


    public AbyssalFall(Player player) {
        super(player, name);

        if (shouldStart()){
            player.setVelocity(new Vector(0,3 * speed, 0));
            Entities.applyPotionPlayer(player, PotionEffectType.SLOW_FALLING, 60000);
            abilityStatus = AbilityStatus.CHARGED;
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (abilityStatus == AbilityStatus.MOVING){
            PacketUtils.playRiptide(player, 20);
            player.setVelocity(dir.clone().multiply(speed));
            if (Blocks.isSolid(player.getLocation().subtract(0,1,0))) {

                Blocks.getBlocksAroundPoint(player.getLocation(), radius).forEach(b -> {
                    TempBlock tb = new TempBlock(b, Material.AIR, duration, true);
                });

                Blocks.getBlocksAroundPoint(player.getLocation(), radius +1).stream().filter(b -> Blocks.isTopBlock(b) && !b.isPassable()).forEach(b -> {
                    TempBlock tb = new TempBlock(b, Material.SOUL_SAND, duration, true);
                });
                abilityStatus = AbilityStatus.SHOT;
                player.removePotionEffect(PotionEffectType.SLOW_FALLING);
                this.remove();
            }
        }

    }

    public void setHasClicked(){
        if (abilityStatus == AbilityStatus.CHARGED) {
            abilityStatus = AbilityStatus.MOVING;
            this.dir = player.getEyeLocation().getDirection();
        }
    }

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
