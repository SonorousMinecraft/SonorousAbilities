package com.sereneoasis.archetypes.war;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.*;
import com.sereneoasis.util.methods.Display;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.Rotations;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftItemDisplay;
import org.bukkit.entity.*;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;

public class Katana extends CoreAbility {

    private final String name = "Katana";

    private ItemDisplay katana1, katana2;

    private double arcAngle = 144, currentArcAngle;


    private ArmorStand armorStand;

    private long lastSwingTime;

    private long swingCooldown = 2000;




    public Katana(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }
        abilityStatus = AbilityStatus.CHARGING;


        start();

    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (System.currentTimeMillis() > startTime + duration) {
            this.remove();
        }

        AbilityUtils.showCharged(this);

        if (abilityStatus == AbilityStatus.CHARGING) {
            if (player.isSneaking()) {
                if (System.currentTimeMillis() > startTime + chargeTime) {
                    abilityStatus = AbilityStatus.CHARGED;
                    this.armorStand = Display.createArmorStandClip(player.getEyeLocation());
                    katana1 = Display.createItemDisplayOffset(player.getEyeLocation(), Material.IRON_SWORD, size, size, size, true, -0.55, -1.3, size/2);
                    katana2 = Display.createItemDisplayOffset(player.getEyeLocation(), Material.IRON_SWORD, size, size, size, true, 0.55, -1.3, size/2);
                    armorStand.addPassenger(katana1);
                    armorStand.addPassenger(katana2);
                    player.addPassenger(armorStand);

                    this.lastSwingTime = System.currentTimeMillis();
                    player.setGlowing(true);
                }

            } else {
                this.remove();
            }
        } else if ((abilityStatus == AbilityStatus.CHARGED ) || (abilityStatus == AbilityStatus.ATTACKING) ) {

            float yaw = player.getEyeLocation().getYaw();
            float pitch = player.getEyeLocation().getPitch();
            katana1.setRotation(yaw, pitch);
            katana2.setRotation(yaw, pitch);

        }
        if (abilityStatus == AbilityStatus.CHARGED){
            if (System.currentTimeMillis() - lastSwingTime > swingCooldown){

                Location attackLoc1 =  Locations.getMainHandLocation(player).clone().add(player.getEyeLocation().getDirection());
                Location attackLoc2 =  Locations.getOffHandLocation(player).clone().add(player.getEyeLocation().getDirection());

                Particles.spawnParticle(Particle.CRIT, attackLoc1,
                        1, 0, 0);
                Particles.spawnParticle(Particle.CRIT, attackLoc2,
                        1, 0, 0);
            }
        }

        if (abilityStatus == AbilityStatus.ATTACKING) {

            Display.rotateItemDisplayProperlyWithOffsetDegs(katana1, size, -0.55, -1.3, size/2 + 1 , 0, 0, -16 );
            Display.rotateItemDisplayProperlyWithOffsetDegs(katana2, size, -0.55, -1.3, size/2 + 1, 0, 0, -16 );
            currentArcAngle += 16;


            Location attackLoc1 =  Locations.getMainHandLocation(player).clone().add(player.getEyeLocation().getDirection());
            Location attackLoc2 =  Locations.getOffHandLocation(player).clone().add(player.getEyeLocation().getDirection());
            Entities.getAffectedList(attackLoc1, hitbox, player).stream().forEach(entity -> DamageHandler.damageEntity(entity,player, this, damage));
            Entities.getAffectedList(attackLoc2, hitbox, player).stream().forEach(entity -> DamageHandler.damageEntity(entity,player, this, damage));
            Particles.spawnParticle(Particle.SWEEP_ATTACK, attackLoc1,
                    1, 0, 0);
            Particles.spawnParticle(Particle.SWEEP_ATTACK, attackLoc2,
                    1, 0, 0);
            if (currentArcAngle > arcAngle) {
                abilityStatus = AbilityStatus.CHARGED;

                Display.rotateItemDisplayProperlyWithOffsetDegs(katana1, size, -0.55, -1.3, size/2 , 0, 0, (float) currentArcAngle );
                Display.rotateItemDisplayProperlyWithOffsetDegs(katana1, size, -0.55, -1.3, size/2 , 0, -90, 0 );

                Display.rotateItemDisplayProperlyWithOffsetDegs(katana2, size, 0.55, -1.3, size/2 , 0, 0, (float) currentArcAngle );
                Display.rotateItemDisplayProperlyWithOffsetDegs(katana2, size, 0.55, -1.3, size/2 , 0, 90, 0 );
            }
        }
    }

    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.CHARGED && System.currentTimeMillis() - lastSwingTime > swingCooldown) {

            lastSwingTime = System.currentTimeMillis();

            Display.rotateItemDisplayProperlyWithOffsetDegs(katana1, size, -0.55, -1.3, size/2 , 0, 90, 0 );

            Display.rotateItemDisplayProperlyWithOffsetDegs(katana2, size, 0.55, -1.3, size/2 , 0, -90, 0 );

            currentArcAngle = 0;
            abilityStatus = AbilityStatus.ATTACKING;
        }
    }

    @Override
    public void remove() {
        super.remove();
        katana1.remove();
        katana2.remove();
        player.setGlowing(false);
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
