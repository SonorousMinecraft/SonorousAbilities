package com.sereneoasis.archetypes.war;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.AbilityUtils;
import com.sereneoasis.util.methods.Display;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Particles;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Katana extends CoreAbility {

    private final String name = "Katana";

    private ArmorStand armorStand;

    private ItemDisplay katana1, katana2;

    private double arcAngle = 144, currentArcAngle, size = 1.5;

    private Transformation defaultTransformation1, defaultTransformation2;

    private float previousYaw;

    public Katana(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }
        abilityStatus = AbilityStatus.CHARGING;
        armorStand = Display.createArmorStand(player.getEyeLocation());
        player.addPassenger(armorStand);
        this.previousYaw = player.getEyeLocation().getYaw();
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
                    //katana1 = Display.createItemDisplay(Locations.getMainHandLocation(player), Material.IRON_SWORD, size, true);
                    //katana2 = Display.createItemDisplay(Locations.getOffHandLocation(player), Material.IRON_SWORD, size, true);
                    katana1 = Display.createItemDisplayOffset(Locations.getMainHandLocation(player), Material.IRON_SWORD,
                            size, size, size, true,
                            -0.5, -1.5, 0.5);
                    armorStand.addPassenger(katana1);
                    katana2 = Display.createItemDisplayOffset(Locations.getMainHandLocation(player), Material.IRON_SWORD,
                            size, size, size, true,
                            0.5, -1.5, 0.5);
                    armorStand.addPassenger(katana2);

                    defaultTransformation1 = katana1.getTransformation();
                    defaultTransformation2 = katana2.getTransformation();
                    player.setGlowing(true);
                }
            } else {
                this.remove();
            }
        } else if (abilityStatus == AbilityStatus.CHARGED || abilityStatus == AbilityStatus.ATTACKING) {
            // Vector offsetFix = new Vector(size / 2, 0, size / 2).rotateAroundY(-Math.toRadians(player.getEyeLocation().getYaw()));
            //katana1.teleport(Locations.getMainHandLocation(player).clone().add(offsetFix));
            //katana2.teleport(Locations.getOffHandLocation(player).clone().add(offsetFix));

            float yawChange = (float) Math.toRadians( player.getEyeLocation().getYaw() - previousYaw );
          //  if (yawChange > 0) {

            Display.rotateItemDisplayProperly(katana1, size, 0 , 0, yawChange );
            Display.rotateItemDisplayProperly(katana2, size, 0, 0, yawChange );
                this.previousYaw = player.getEyeLocation().getYaw();
            //}

        }
        if (abilityStatus == AbilityStatus.ATTACKING) {
            Transformation transformation = katana1.getTransformation();
            Quaternionf quaternionf = transformation.getLeftRotation();
            quaternionf.rotateZ((float) Math.toRadians(16));
            currentArcAngle += 16;
            katana1.setTransformation(transformation);

            Transformation transformation2 = katana2.getTransformation();
            Quaternionf quaternionf2 = transformation2.getLeftRotation();
            quaternionf2.rotateZ((float) -Math.toRadians(16));
            katana2.setTransformation(transformation2);


            Vector offsetFix = new Vector(size / 2, 0, size / 2).rotateAroundY(-Math.toRadians(player.getEyeLocation().getYaw()));
            Particles.spawnParticle(Particle.SWEEP_ATTACK, Locations.getMainHandLocation(player).clone().add(player.getEyeLocation().getDirection()),
                    1, 0, 0);
            Particles.spawnParticle(Particle.SWEEP_ATTACK, Locations.getOffHandLocation(player).clone().add(player.getEyeLocation().getDirection()),
                    1, 0, 0);
            if (currentArcAngle > arcAngle) {
                abilityStatus = AbilityStatus.CHARGED;
//                katana1.setTransformation(defaultTransformation);
//                katana2.setTransformation(defaultTransformation);
                katana1.setTransformation(defaultTransformation1);
                katana2.setTransformation(defaultTransformation2);
            }
        }
    }

    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.CHARGED) {
            Transformation transformation = katana1.getTransformation();
            Quaternionf quaternionf = transformation.getLeftRotation();
            quaternionf.rotateXYZ(0, (float) -Math.toRadians(90), (float) -Math.toRadians(90));
            katana1.setTransformation(transformation);

            Transformation transformation2 = katana2.getTransformation();
            Quaternionf quaternionf2 = transformation2.getLeftRotation();
            quaternionf2.rotateXYZ(0, (float) -Math.toRadians(90), (float) Math.toRadians(0));
            katana2.setTransformation(transformation2);

            currentArcAngle = 0;
            abilityStatus = AbilityStatus.ATTACKING;
        }
    }

    @Override
    public void remove() {
        super.remove();
        katana1.remove();
        katana2.remove();
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
