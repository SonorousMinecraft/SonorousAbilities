package com.sereneoasis.archetypes.war;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.AbilityUtils;
import com.sereneoasis.util.methods.Display;
import com.sereneoasis.util.methods.Entities;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Transformation;

public class Wings extends CoreAbility {

    private final String name = "Wings";

    private ItemDisplay wings;

    private double size = 1.5;

    private Transformation defaultTransformation;

    public Wings(Player player) {
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
                    wings = Display.createItemDisplayNoTransform(player.getEyeLocation().subtract(player.getEyeLocation().getDirection().multiply(0.2)), Material.ELYTRA, size,
                            ItemDisplay.ItemDisplayTransform.NONE, org.bukkit.entity.Display.Billboard.CENTER);
                }
            } else {
                this.remove();
            }
        } else if (abilityStatus == AbilityStatus.CHARGED) {
            //Vector offsetFix = new Vector(size / 2, 0, size / 2).rotateAroundY(-Math.toRadians(player.getEyeLocation().getYaw()));
            wings.teleport(player.getEyeLocation().subtract(player.getEyeLocation().getDirection().multiply(0.2)).subtract(0,size/2,0));

            if (!player.getLocation().subtract(0,0.5,0).getBlock().getType().isSolid() && player.isSneaking()){
                if (!player.isGliding()) {
                    Entities.applyPotionPlayerAmplifier(player, PotionEffectType.SLOW_DIGGING,10000000, Math.round(duration));
                    player.setGliding(true);
                }
            }
            else if (player.getLocation().subtract(0,0.5,0).getBlock().getType().isSolid()){
                player.setGliding(false);
                player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
            }
        }

    }

    public void setWingLocation(Location newLoc){
        wings.teleport(newLoc);
    }

    @Override
    public void remove() {
        super.remove();
        wings.remove();
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
