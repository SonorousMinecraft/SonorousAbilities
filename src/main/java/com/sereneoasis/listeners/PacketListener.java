package com.sereneoasis.listeners;


import com.sereneoasis.Serenity;
import com.sereneoasis.SerenityPlayer;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.sky.Cyclone;
import com.sereneoasis.archetypes.war.Jetpack;
import io.netty.channel.*;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_20_R2.entity.*;
import org.bukkit.entity.*;

public class PacketListener {

    public void removePlayer(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().connection.connection.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

    public void injectPlayer(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                //Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "PACKET READ: " + ChatColor.RED + packet.toString());

                if (packet instanceof ServerboundPlayerInputPacket moveInputPacket) {
                    ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();

                    Player spigotPlayer = player.getPlayer();
                    if (spigotPlayer == null) {
                        return;
                    }

                    SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayer(spigotPlayer);
                    if (sPlayer == null) {
                        return;
                    }
                    float sidewards = moveInputPacket.getXxa(); // Sidewards
                    float forewards = moveInputPacket.getZza(); //forewards


                    // For riding any entity
                    if (spigotPlayer.getVehicle() instanceof org.bukkit.entity.LivingEntity livingEntity) {
                        LivingEntity rideable = ((CraftLivingEntity) livingEntity).getHandle();

                        if (livingEntity instanceof Blaze | livingEntity instanceof Wither | livingEntity instanceof Phantom | livingEntity instanceof Ghast | livingEntity instanceof EnderDragon | livingEntity instanceof Allay |
                                livingEntity instanceof Ghast | livingEntity instanceof Bee | livingEntity instanceof Vex) {
                            Vec3 newMovement = new Vec3(sidewards, rideable.getDeltaMovement().y, forewards).yRot((float) -Math.toRadians(nmsPlayer.getBukkitYaw()));
                            newMovement.normalize().scale(livingEntity.getAttribute(Attribute.GENERIC_FLYING_SPEED).getValue());
                            rideable.setDeltaMovement(newMovement);
                            rideable.setYRot(nmsPlayer.getBukkitYaw());
                            rideable.setXRot(spigotPlayer.getEyeLocation().getPitch());

                            if (moveInputPacket.isJumping()) {
                                livingEntity.setVelocity(livingEntity.getVelocity().setY(0.42F));
                            }
                            if (moveInputPacket.isShiftKeyDown()) {
                                livingEntity.setVelocity(livingEntity.getVelocity().setY(-0.42F));
                            }
                        } else {
                            boolean isRidingAbility = false;
                            if (CoreAbility.hasAbility(player, Cyclone.class)) {
                                ArmorStand spigotStand = CoreAbility.getAbility(player, Cyclone.class).getArmorStand();
                                net.minecraft.world.entity.decoration.ArmorStand nmsStand = ((CraftArmorStand) spigotStand).getHandle();

                                nmsStand.setDeltaMovement(new Vec3(sidewards, 0, forewards).yRot((float) -Math.toRadians(nmsPlayer.getBukkitYaw())));
                                isRidingAbility = true;
                            }
                            else if (CoreAbility.hasAbility(player, Jetpack.class)) {
                                ArmorStand spigotStand = CoreAbility.getAbility(player, Jetpack.class).getArmorStand();
                                net.minecraft.world.entity.decoration.ArmorStand nmsStand = ((CraftArmorStand) spigotStand).getHandle();

                                Vec3 newMovement = new Vec3(sidewards, nmsStand.getDeltaMovement().y, forewards).yRot((float) -Math.toRadians(nmsPlayer.getBukkitYaw()));
                                newMovement.normalize().scale(CoreAbility.getAbility(player, Jetpack.class).getSpeed());
                                nmsStand.setDeltaMovement(newMovement);
                                nmsStand.setYRot(nmsPlayer.getBukkitYaw());
                                nmsStand.setXRot(spigotPlayer.getEyeLocation().getPitch());

                                if (moveInputPacket.isJumping()) {
                                    spigotStand.setVelocity(spigotStand.getVelocity().setY(0.42F));
                                }
                                if (moveInputPacket.isShiftKeyDown()) {
                                    spigotStand.setVelocity(spigotStand.getVelocity().setY(-0.42F));
                                }
                                isRidingAbility = true;
                            }
                            if (!isRidingAbility) {
                                Vec3 newMovement = new Vec3(sidewards, rideable.getDeltaMovement().y, forewards).yRot((float) -Math.toRadians(nmsPlayer.getBukkitYaw()));
                                newMovement.normalize().scale(livingEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue());
                                rideable.setDeltaMovement(newMovement);

                                rideable.setYRot(nmsPlayer.getBukkitYaw());
                                rideable.setXRot(spigotPlayer.getEyeLocation().getPitch());
                                if (moveInputPacket.isJumping() && livingEntity.isOnGround()) {
                                    livingEntity.setVelocity(livingEntity.getVelocity().setY(0.42F + rideable.getJumpBoostPower()));
                                }
                            }
                        }
                    }
                }
                super.channelRead(channelHandlerContext, packet);
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
                //Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "PACKET WRITE: " + ChatColor.GREEN + packet.toString());
                if (packet instanceof ClientboundMoveEntityPacket moveEntityPacket) {
                    ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
                    Serenity.getPlugin().getServer().getScheduler().runTask(Serenity.getPlugin(), () -> {

                        Entity packetEntity = moveEntityPacket.getEntity(nmsPlayer.getCommandSenderWorld());
                        //Bukkit.broadcastMessage("1");
                        //Bukkit.broadcastMessage(String.valueOf(packetEntity));

                        if (packetEntity == nmsPlayer) {
                            Bukkit.broadcastMessage("2");
                            Entity target = nmsPlayer.getCamera();
                            if (target != nmsPlayer) {
                                Bukkit.broadcastMessage("3");
                                if (target instanceof LivingEntity livingEntity) {

                                }
                            }
                        }
                    });


                }
                //if the server is sending a packet, the function "write" will be called. If you want to cancel a specific packet, just use return; Please keep in mind that using the return thing can break the intire server when using the return thing without knowing what you are doing.
                super.write(channelHandlerContext, packet, channelPromise);
            }


        };

        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().connection.connection.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);

    }
}
