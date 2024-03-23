package com.sereneoasis.archetypes.war;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.items.ThrowItemDisplay;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.AbilityUtils;
import com.sereneoasis.util.methods.Particles;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Formless extends CoreAbility {

    private final String name = "Formless";

    private List<ThrowItemDisplay> arrows = new ArrayList<>();

    private net.minecraft.world.entity.player.Player nmsPlayer = ((CraftPlayer)player).getHandle();

    private int usages = 0;
    private int maxUsages = 5;

    public Formless(Player player) {
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

        if (abilityStatus == AbilityStatus.CHARGING) {
            if (player.isSneaking()) {
                if (System.currentTimeMillis() > startTime + chargeTime) {
                    abilityStatus = AbilityStatus.CHARGED;
                    player.setGlowing(true);
                    return;
                }
            } else {
                this.remove();
            }
        }

        if (abilityStatus == AbilityStatus.CHARGED) {
            AbilityUtils.showShots(this, usages, maxUsages);

            Particles.spawnParticle(Particle.SONIC_BOOM, player.getLocation(), 1, 0, 0);
            for (ThrowItemDisplay shot : arrows) {
                if (shot.getAbilityStatus() == AbilityStatus.COMPLETE) {
                    shot.remove();
                }
            }
            if (usages == maxUsages){
                this.remove();
            }
        }
    }

    public void setHasClicked(Action action) {

        if (action == Action.RIGHT_CLICK_BLOCK) {
            usages ++;
            player.setVelocity(player.getEyeLocation().getDirection().clone().multiply(-1));
        } else if (action == Action.RIGHT_CLICK_AIR) {
            ItemStack holding = player.getInventory().getItemInMainHand();
            if (holding.getType() == Material.ARROW) {
                holding.setAmount(holding.getAmount() - 1);
                usages ++;
                arrows.add(new ThrowItemDisplay(player, name, player.getEyeLocation(),
                        player.getEyeLocation().getDirection().clone(), Material.ARROW, size/4, size*2, false, false, false));
            }
        } else if (action == Action.LEFT_CLICK_AIR) {
            if (isAgainstWall()) {
                usages ++;
                player.setVelocity(player.getEyeLocation().getDirection().clone());
            }
        } else if (action == Action.LEFT_CLICK_BLOCK) {
            usages ++;
            player.setVelocity(new Vector(0, 0, 1).rotateAroundY(-Math.toRadians(player.getEyeLocation().getYaw())));
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

    public double getSpeed(){
        return speed * 2;
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
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
