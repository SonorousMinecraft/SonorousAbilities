package com.sereneoasis.archetypes.war;

import com.mojang.datafixers.util.Pair;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.AbilityUtils;
import com.sereneoasis.util.methods.Display;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.PacketUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;

import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Transformation;

import java.util.ArrayList;
import java.util.List;

public class Wings extends CoreAbility {

    private final String name = "Wings";


    private double size = 1.5;

    private Transformation defaultTransformation;


    private BossBar barduration;


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
        if (System.currentTimeMillis() > startTime + chargeTime + duration) {
            this.remove();
        }

        AbilityUtils.showCharged(this);

        if (abilityStatus == AbilityStatus.CHARGING) {
            if (player.isSneaking()) {
                if (System.currentTimeMillis() > startTime + chargeTime) {
                    abilityStatus = AbilityStatus.CHARGED;

                    PacketUtils.setClientChestplate(player, Material.ELYTRA);

                    barduration = Bukkit.getServer().createBossBar(name, BarColor.BLUE, BarStyle.SEGMENTED_10);

                }
            } else {
                this.remove();
            }
        } else if (abilityStatus == AbilityStatus.CHARGED) {
            //Vector offsetFix = new Vector(size / 2, 0, size / 2).rotateAroundY(-Math.toRadians(player.getEyeLocation().getYaw()));

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
