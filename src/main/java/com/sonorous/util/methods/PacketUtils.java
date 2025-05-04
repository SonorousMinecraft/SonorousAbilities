package com.sonorous.util.methods;

import com.sonorous.SonorousAbilities;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;


public class PacketUtils {

//    public static void setClientChestplate(Player player, Material material){
//
//        CraftPlayer craftPlayer = (CraftPlayer) player;
//        ServerPlayerConnection playerConnection = craftPlayer.getHandle().connection;
//        List<Pair<EquipmentSlot, ItemStack>> newEquipment = new ArrayList<>();
//        newEquipment.add(new Pair<>(EquipmentSlot.CHEST, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(material))));
//        ClientboundSetEquipmentPacket fakeElytra = new ClientboundSetEquipmentPacket(player.getEntityId(), newEquipment);
//        playerConnection.send(fakeElytra);
//    }

    public static void oneTwo(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayerConnection playerConnection = craftPlayer.getHandle().connection;
        ServerPlayer nmsPlayer = craftPlayer.getHandle();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        HumanoidArm mainHand = nmsPlayer.getMainArm();
        HumanoidArm offHand = mainHand.getOpposite();

        ClientboundAnimatePacket clientboundAnimatePacket = new ClientboundAnimatePacket(craftPlayer.getHandle(), 0);
        playerConnection.send(clientboundAnimatePacket);

        scheduler.runTaskLater(SonorousAbilities.getPlugin(), () -> {
            nmsPlayer.setMainArm(offHand);
            ClientboundAnimatePacket clientboundAnimatePacket2 = new ClientboundAnimatePacket(craftPlayer.getHandle(), 0);
            playerConnection.send(clientboundAnimatePacket2);

        }, 10L /*<-- the delay */);
        scheduler.runTaskLater(SonorousAbilities.getPlugin(), () -> {
            nmsPlayer.setMainArm(mainHand);
        }, 20L /*<-- the delay */);
    }

    public static void playRiptide(Player player, int ticks) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().startAutoSpinAttack(ticks);
        ServerPlayer nmsPlayer = craftPlayer.getHandle();

    }

    public static void setCamera(Player player, Entity target) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer nmsPlayer = craftPlayer.getHandle();
        nmsPlayer.setCamera(target);
    }

    public static void leashEntity(Player player, org.bukkit.entity.Entity target) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer nmsPlayer = craftPlayer.getHandle();
        ServerPlayerConnection playerConnection = craftPlayer.getHandle().connection;
        Entity nmsTarget = ((CraftEntity) target).getHandle();
        ClientboundSetEntityLinkPacket clientboundSetEntityLinkPacket = new ClientboundSetEntityLinkPacket(nmsTarget, nmsPlayer);
        playerConnection.send(clientboundSetEntityLinkPacket);
    }

//    public static void lookAtEntity(Player player, org.bukkit.entity.Entity target){
//        CraftPlayer craftPlayer = (CraftPlayer) player;
//        CraftEntity nmsTarget = ((CraftEntity) target);
//        craftPlayer.lookAt(nmsTarget, LookAnchor.EYES, LookAnchor.EYES);
//    }

    public static void flipEntity(Player player, org.bukkit.entity.Entity target) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer nmsPlayer = craftPlayer.getHandle();
        nmsPlayer.setXRot(-90F);

        ServerPlayerConnection playerConnection = craftPlayer.getHandle().connection;

        CraftEntity craftEntity = (CraftEntity) target;


//        ArmorStand armorStand = Display.createArmorStandClip(player.getLocation());
//        CraftArmorStand craftArmorStand = (CraftArmorStand) armorStand;


//        SynchedEntityData.DataValue<?> value = new SynchedEntityData.DataValue<>(9, EntityDataSerializers.ROTATIONS, new Rotations(0, 0, 90F));
//        List<SynchedEntityData.DataValue<?>> dataValues = List.of(value);
//        ClientboundSetEntityDataPacket clientboundSetEntityDataPacket = new ClientboundSetEntityDataPacket(craftEntity.getEntityId(), dataValues);
//        playerConnection.send(clientboundSetEntityDataPacket);


        //player.setSpectatorTarget(armorStand);
    }

    public static void upsideDownArmorStand(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayerConnection playerConnection = craftPlayer.getHandle().connection;
        ArmorStand armorStand = Display.createArmorStandClip(player.getEyeLocation());
        net.minecraft.world.entity.decoration.ArmorStand nmsStand = ((CraftArmorStand) armorStand).getHandle();
        nmsStand.setXRot(-180F);

        Boat boat = Vehicle.createBoatVehicle(player.getEyeLocation());

        boat.addPassenger(player);
        boat.addPassenger(armorStand);

        ClientboundSetCameraPacket clientboundSetCameraPacket = new ClientboundSetCameraPacket(nmsStand);
        playerConnection.send(clientboundSetCameraPacket);


    }

}
