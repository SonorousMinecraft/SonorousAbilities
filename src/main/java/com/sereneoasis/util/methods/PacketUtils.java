package com.sereneoasis.util.methods;

import com.mojang.datafixers.util.Pair;
import com.sereneoasis.Serenity;
import io.papermc.paper.entity.LookAnchor;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;


public class PacketUtils {

    public static void setClientChestplate(Player player, Material material){

        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayerConnection playerConnection = craftPlayer.getHandle().connection;
        List<Pair<EquipmentSlot, ItemStack>> newEquipment = new ArrayList<>();
        newEquipment.add(new Pair<>(EquipmentSlot.CHEST, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(material))));
        ClientboundSetEquipmentPacket fakeElytra = new ClientboundSetEquipmentPacket(player.getEntityId(), newEquipment);
        playerConnection.send(fakeElytra);
    }

    public static void oneTwo(Player player){
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayerConnection playerConnection = craftPlayer.getHandle().connection;
        ServerPlayer nmsPlayer = craftPlayer.getHandle();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        HumanoidArm mainHand = nmsPlayer.getMainArm();
        HumanoidArm offHand = mainHand.getOpposite();

        scheduler.runTaskLater(Serenity.getPlugin(), () -> {
            nmsPlayer.setMainArm(offHand);
            ClientboundAnimatePacket clientboundAnimatePacket = new ClientboundAnimatePacket(craftPlayer.getHandle(),0 );
            playerConnection.send(clientboundAnimatePacket);

        }, 10L /*<-- the delay */);
        scheduler.runTaskLater(Serenity.getPlugin(), () -> {
            nmsPlayer.setMainArm(mainHand);
        }, 20L /*<-- the delay */);
    }

    public static void playRiptide(Player player, int ticks){
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().startAutoSpinAttack(ticks);
        ServerPlayer nmsPlayer = craftPlayer.getHandle();

    }

    public static void setCamera(Player player, Entity target)
    {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer nmsPlayer = craftPlayer.getHandle();
        nmsPlayer.setCamera(target);
    }

    public static void leashEntity(Player player, org.bukkit.entity.Entity target){
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer nmsPlayer = craftPlayer.getHandle();
        ServerPlayerConnection playerConnection = craftPlayer.getHandle().connection;
        Entity nmsTarget = ((CraftEntity) target).getHandle();
        ClientboundSetEntityLinkPacket clientboundSetEntityLinkPacket = new ClientboundSetEntityLinkPacket(nmsTarget, nmsPlayer);
        playerConnection.send(clientboundSetEntityLinkPacket);
    }

    public static void lookAtEntity(Player player, org.bukkit.entity.Entity target){
        CraftPlayer craftPlayer = (CraftPlayer) player;
        CraftEntity nmsTarget = ((CraftEntity) target);
        craftPlayer.lookAt(nmsTarget, LookAnchor.EYES, LookAnchor.EYES);
    }
}
