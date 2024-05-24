package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.abilityuilities.blocks.ShootBlockFromLoc;
import com.sereneoasis.abilityuilities.blocks.ShootBlockShapeFromLoc;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.AbilityUtils;
import com.sereneoasis.util.methods.Vectors;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Random;

public class OctopusForm extends MasterAbility {

    private static final String name = "OctopusForm";

    private HashMap<Tendril, Vector> tendrils = new HashMap<>();

    private long sinceLastMoved = System.currentTimeMillis();

    private boolean hasBeenHoldingSneak;
    private long sinceLastHeldSneak;

    private Random random = new Random();

    private double previousYaw = player.getEyeLocation().getYaw(), previousPitch = player.getEyeLocation().getPitch();



    private OctopusFormState state = OctopusFormState.MOVING;

    private enum OctopusFormState {
        MOVING,
        SHOOT_TENDRIL,
        ICE_ATTACK,
        GRAB,
        GRAPPLE
    }

    public OctopusForm(Player player) {
        super(player, name);

        if (shouldStart()){

            for (int i = 0; i < 16 ; i++) {
                Tendril tendril = new Tendril(player, name, sourceRange);
                tendrils.put(tendril, getRandomOffset().normalize());
            }
            start();
        }
    }

    private Vector getRandomOffset(){
        Vector randomiser = Vectors.getRightSide(player, random.nextDouble(-5, 5)).add(new Vector(0, random.nextDouble(-2,2), 0).rotateAroundAxis(Vectors.getRightSideNormalisedVector(player), Math.toRadians(-player.getEyeLocation().getPitch())));
        return randomiser;
    }

    @Override
    public void progress() throws ReflectiveOperationException {

        if (System.currentTimeMillis() - sinceLastMoved > 100) {
            sinceLastMoved = System.currentTimeMillis();
            double currentYaw = player.getEyeLocation().getYaw();
            double currentPitch = player.getEyeLocation().getPitch();

            double yawDiff = currentYaw - previousYaw;
            double pitchDiff = currentPitch - previousPitch;
            tendrils.forEach((tendril, vector) -> {

                if (tendril.getAbilityStatus().equals(AbilityStatus.MOVING))
                {
                    vector.rotateAroundY(-Math.toRadians(yawDiff));
                    vector.rotateAroundAxis(Vectors.getRightSideNormalisedVector(player), - Math.toRadians(pitchDiff));

                    previousYaw = currentYaw;
                    previousPitch = currentPitch;
                    tendril.move(vector);
                }

            });
        }

        switch (state) {
            case MOVING -> {
                AbilityUtils.sendActionBar(player, "MOVING", ChatColor.BLUE);
            }
            case GRAB -> {
                AbilityUtils.sendActionBar(player, "GRAB", ChatColor.BLUE);

            }
            case GRAPPLE -> {
                AbilityUtils.sendActionBar(player, "GRAPPLE", ChatColor.BLUE);


            }

            case ICE_ATTACK -> {
                AbilityUtils.sendActionBar(player, "ICE_ATTACK", ChatColor.BLUE);

            }

            case SHOOT_TENDRIL -> {
                AbilityUtils.sendActionBar(player, "SHOOTING_TENDRIL", ChatColor.BLUE);

            }
        }

        if (player.isSneaking()){
            if (!hasBeenHoldingSneak) {
                sinceLastHeldSneak = System.currentTimeMillis();
                hasBeenHoldingSneak = true;
            } else {
                if (System.currentTimeMillis() - sinceLastHeldSneak > chargeTime) {
                    Bukkit.broadcastMessage("shit happens");
                    hasBeenHoldingSneak = false;
                    switch (state) {
                        case MOVING -> {
                            state = OctopusFormState.GRAB;
                        }
                        case GRAB -> {
                            state = OctopusFormState.GRAPPLE;
                            tendrils.keySet().stream().filter(tendril -> tendril.getAbilityStatus() == AbilityStatus.GRAB).forEach(Tendril::endGrab);
                        }
                        case GRAPPLE -> {
                            state = OctopusFormState.ICE_ATTACK;
                            tendrils.keySet().forEach(tendril ->tendril.setTendrilBlock(DisplayBlock.ICE) );
                            tendrils.keySet().stream().filter(tendril -> tendril.getAbilityStatus() == AbilityStatus.GRAPPLE).forEach(Tendril::endGrapple);

                        }

                        case ICE_ATTACK -> {
                            state = OctopusFormState.SHOOT_TENDRIL;
                            tendrils.keySet().forEach(tendril ->tendril.setTendrilBlock(DisplayBlock.WATER) );
                        }

                        case SHOOT_TENDRIL -> {
                            state = OctopusFormState.MOVING;
                        }
                    }
                }
            }
        }



        if (tendrils.keySet().stream().allMatch(tendril -> tendril.getAbilityStatus() == AbilityStatus.COMPLETE)) {
            tendrils.keySet().forEach(Tendril::remove);
            sPlayer.addCooldown(name, cooldown);
            this.remove();
        }
    }

    public void setHasClicked(){
        switch (state) {
            case MOVING -> {

            }
            case GRAB -> {
                tendrils.keySet().stream()
                        .filter(tendril -> tendril.getAbilityStatus() == AbilityStatus.MOVING)
                        .findFirst().ifPresent(tendril -> tendril.grab(player.getEyeLocation().getDirection()));
            }
            case GRAPPLE -> {
                tendrils.keySet().stream()
                        .filter(tendril -> tendril.getAbilityStatus() == AbilityStatus.MOVING)
                        .findFirst().ifPresent(tendril -> tendril.grapple(player.getEyeLocation().getDirection()));
            }

            case ICE_ATTACK -> {
                tendrils.keySet().stream()
                        .filter(tendril -> tendril.getAbilityStatus() == AbilityStatus.MOVING)
                        .findFirst().ifPresent(tendril -> {
                            new ShootBlockFromLoc(player, name, tendril.getEnd().getLoc(), Material.SNOW, false, true);
                            tendrils.remove(tendril);
                            tendril.remove();
                        });
            }

            case SHOOT_TENDRIL -> {
                tendrils.keySet().stream()
                        .filter(tendril -> tendril.getAbilityStatus() == AbilityStatus.MOVING)
                        .findFirst().ifPresent(tendril -> {
                            new ShootBlockShapeFromLoc(player, name, tendril.getEnd().getLoc(), tendril.getTendrils(), size, true, player.getEyeLocation().getDirection());
                            tendrils.remove(tendril);
                            tendril.clearTendrilMap();
                        });
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
