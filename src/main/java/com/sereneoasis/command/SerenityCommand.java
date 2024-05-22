package com.sereneoasis.command;

import com.sereneoasis.SerenityPlayer;
import com.sereneoasis.ability.data.AbilityData;
import com.sereneoasis.ability.data.AbilityDataManager;
import com.sereneoasis.archetypes.Archetype;
import com.sereneoasis.displays.SerenityBoard;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.PacketUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.entity.monster.Spider;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftSpider;
import org.bukkit.entity.*;

import java.util.Arrays;

import static com.sereneoasis.SerenityPlayer.initialisePlayer;


/**
 * @author Sakrajin
 * Handles all serenity commands
 */
public class SerenityCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayer(player);
            if (sPlayer != null) {
                switch (strings[0]) {
                    case "test":
                        PacketUtils.upsideDownArmorStand(player);
                        return true;
                    case "flip":
                        for (Entity e : Entities.getEntitiesAroundPoint(player.getLocation(), 10)){
                            PacketUtils.flipEntity(player, e);
                        }
                        return true;
                    case "dismount":
                        if (player.getVehicle() instanceof LivingEntity rideable){
                            rideable.eject();

                        }
                        return true;
                    case "swing":
                        CraftPlayer craftPlayer = (CraftPlayer) player;
                        ServerPlayerConnection playerConnection = craftPlayer.getHandle().connection;

                        //ClientboundAnimatePacket clientboundAnimatePacket = new ClientboundAnimatePacket(craftPlayer.getHandle(),0 );
//                        ClientboundAnimatePacket clientboundAnimatePacket2 = new ClientboundAnimatePacket(craftPlayer.getHandle(),3 );
//
//                        //playerConnection.send(clientboundAnimatePacket);
//                        playerConnection.send(clientboundAnimatePacket2);

                        //craftPlayer.playEffect(player.getEyeLocation().add(player.getEyeLocation().getDirection()), Effect.END_PORTAL_FRAME_FILL, null );
                        ServerPlayer nmsPlayer = craftPlayer.getHandle();
                        //nmsPlayer.setMainArm(HumanoidArm.LEFT);
                        //nmsPlayer.setDiscardFriction(true);
                        //nmsPlayer.startAutoSpinAttack(100);
                        //nmsPlayer.swing(InteractionHand.OFF_HAND, true);
                        //nmsPlayer.startSleeping(BlockPos.containing(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
                        //player.swingOffHand();
                        return true;
                    case "choose":
                        if (strings.length == 1) {
                            player.sendMessage("What archetype do you want to choose?");
                            return false;
                        }
                        if (strings.length > 2) {
                            player.sendMessage("Too many arguments");
                            return false;
                        } else {
                            Archetype archetype = null;
                            for (Archetype archetypes : Archetype.values()) {
                                if (strings[1].equalsIgnoreCase(archetypes.toString())) {
                                    archetype = archetypes;
                                }
                            }
                            if (archetype == null) {
                                player.sendMessage("This is not an archetype!");
                                return false;
                            } else {
                                sPlayer.setArchetype(archetype);
                                initialisePlayer(player);
                                return true;
                            }
                        }

                    case "bind":
                        if (strings.length == 1) {
                            player.sendMessage("What ability do you want to bind?");
                            return false;
                        }
                        if (strings.length > 2) {
                            player.sendMessage("Too many arguments");
                            return false;
                        } else {
                            String ability = strings[1];
                            if (!AbilityDataManager.isAbility(ability)) {
                                player.sendMessage("This isn't an ability.");
                                return false;
                            }
                            else if (AbilityDataManager.isCombo(ability)){
                                player.sendMessage("This is a combo, you cannot bind it.");
                                return false;
                            }
                            int slot = player.getInventory().getHeldItemSlot() + 1;
                            sPlayer.setAbility(slot, ability);
                            player.sendMessage("Successfully bound " + ability + " to slot " + slot + ".");
                            return true;
                        }

                    case "display":
                        if (strings.length == 1) {
                            player.sendMessage("What archetype do you want to show abilities for?");
                            return false;
                        }
                        if (strings.length > 2) {
                            player.sendMessage("Too many arguments");
                            return false;
                        } else {
                            String archetypeString = strings[1].toUpperCase();

                            if (!Arrays.stream(Archetype.values()).anyMatch(archetype -> archetype.toString().equalsIgnoreCase(archetypeString))) {
                                player.sendMessage("This isn't an archetype.");
                                return false;
                            }
                            Archetype archetype = Archetype.valueOf(archetypeString);
                            for (String abil : AbilityDataManager.getArchetypeAbilities(archetype)) {
                                player.sendMessage(abil);
                            }
                            return true;
                        }

                    case "help":
                        if (strings.length == 1) {
                            player.sendMessage("To do help section");
                            return false;
                        }
                        if (strings.length > 2) {
                            player.sendMessage("Too many arguments");
                            return false;
                        } else {
                            String ability = strings[1];
                            if (!AbilityDataManager.isAbility(ability)) {
                                player.sendMessage("This isn't an ability.");
                                return false;
                            }
                            AbilityData abilityData = AbilityDataManager.getAbilityData(ability);
                            player.sendMessage(ability + "\n" + abilityData.getDescription() + "\n" + abilityData.getInstructions());
                            return true;
                        }
                    case "unbind":
                        if (strings.length == 1) {
                            player.sendMessage("Specify a slot");
                            return false;
                        }
                        if (strings.length > 2) {
                            player.sendMessage("Too many arguments");
                            return false;
                        } else {
                            String possibleSlot = strings[1];
                            int slot;
                            try {
                                slot = Integer.parseInt(possibleSlot);
                            } catch (NumberFormatException e) {
                                player.sendMessage("This isn't a slot number.");
                                return false;
                            }
                            String nullAbility = ChatColor.DARK_GRAY + "=-=-Slot" + "_" + slot + "-=-=";
                            sPlayer.setAbility(slot, nullAbility);
                            SerenityBoard.getByPlayer(player).setAbilitySlot(slot, nullAbility);
                            return true;
                        }
                    case "preset":
                        if (strings.length == 1) {
                            player.sendMessage("What preset do you want to do a command for for?");
                            String message = ("You have these presets: ");
                            for (String preset : sPlayer.getPresetNames()) {
                                message = message + "\n" + preset;
                            }
                            player.sendMessage(message);
                            return false;
                        }
                        if (strings.length > 3) {
                            player.sendMessage("Too many arguments");
                            return false;
                        } else {
                            String presetOption = strings[1];

                            switch (presetOption) {
                                case "display":
                                    String message = ("You have these presets: ");
                                    for (String preset : sPlayer.getPresetNames()) {
                                        message = message + "\n" + preset;
                                    }
                                    return true;
                                case "create":
                                    String newName = strings[2];
                                    if (sPlayer.existsPreset(newName)) {
                                        player.sendMessage("This preset already exists");
                                        return false;
                                    }
                                    sPlayer.setPreset(newName, sPlayer.getAbilities());
                                    return true;
                                case "bind":
                                    String presetToBind = strings[2];
                                    if (!sPlayer.existsPreset(presetToBind)) {
                                        player.sendMessage("This preset does not exist");
                                        return false;
                                    }
                                    sPlayer.getPreset(presetToBind).forEach((slot, preset) -> {
                                        player.sendMessage("\n" + slot + "is " + preset);
                                    });
                                    sPlayer.setAbilities(sPlayer.getPreset(presetToBind));

                                    return true;
                                case "delete":
                                    String presetToDelete = strings[2];
                                    if (!sPlayer.existsPreset(presetToDelete)) {
                                        player.sendMessage("This preset does not exist");
                                        return false;
                                    }
                                    sPlayer.deletePreset(presetToDelete);
                                    return true;
                                default:
                                    String invalidMessage = "The valid options are: \n" +
                                            "display, create, bind and delete";
                                    player.sendMessage(invalidMessage);
                            }
                            return true;
                        }
                }
            }
        }
        return false;
    }
}
