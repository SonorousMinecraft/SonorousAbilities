package com.sereneoasis.archetypes.sky;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.enhancedmethods.EnhancedBlocks;
import com.sereneoasis.util.enhancedmethods.EnhancedBlocksArchetypeLess;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.methods.TDBs;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ThunderStrike extends CoreAbility {

    private static final String name = "ThunderStrike";

    private Location origin, loc;
    private Vector dir;

    public ThunderStrike(Player player) {
        super(player, name);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        abilityStatus = AbilityStatus.CHARGING;
        player.setVelocity(new Vector(0, speed * 4, 0));
        start();
    }

    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.CHARGING && !player.isSneaking()) {
            this.remove();
        }

        if (abilityStatus == AbilityStatus.CHARGING && System.currentTimeMillis() > startTime + duration) {
            abilityStatus = AbilityStatus.CHARGED;
        }

        if (abilityStatus == AbilityStatus.SHOT) {
            if (loc.distance(origin) > range) {
                this.remove();
            }

            if (Blocks.isSolid(loc)){
                SkyUtils.lightningStrikeFloorCircle(this, loc);
                this.remove();
            }
            loc.add(dir.clone().multiply(speed));
            Particles.spawnParticle(Particle.GUST, loc, 1, 0, 0);


//            TDBs.playTDBs(loc.clone().subtract(dir.clone().multiply(speed)), DisplayBlock.LIGHTNING, 10, size, radius);

//            Vector random = Vector.getRandom().normalize().add(new Vector(-0.5,-0.5,-0.5)).normalize().add(dir.clone().multiply(0.2)).normalize().multiply(0.4);

            new ArchetypeVisuals.LightningVisual().playShotVisual(loc, dir, 0, size, radius, 1, 1, 1);

//            Particles.spawnParticleOffset(Particle.END_ROD, loc, 0, random.getX(), random.getY(), random.getZ(), 0.15);
//            Particles.spawnColoredParticle(loc, 1, radius, size*3, Color.fromRGB(1, 225, 255));
//            Particles.spawnParticle(Particle.ELECTRIC_SPARK, loc, 1,radius,0);
        }
    }

    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.CHARGED) {
            this.loc = player.getEyeLocation().clone();
            this.origin = loc.clone();
            this.dir = loc.getDirection().clone().normalize();
            abilityStatus = AbilityStatus.SHOT;

        } else if (abilityStatus == AbilityStatus.SHOT) {
            SkyUtils.lightningStrikeFloorCircle(this, loc);

            this.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();
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