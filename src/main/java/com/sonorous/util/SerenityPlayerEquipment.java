//package com.sereneoasis.util;
//
//import com.sereneoasis.SereneAbilitiesPlayer;
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SereneAbilitiesPlayerEquipment {
//
//    private final SereneAbilitiesPlayer sPlayer;
//    private final Player player;
//
//    private List<ItemStack>playerSereneAbilitiesHotBarEquipment = new ArrayList<>();
//    private List<ItemStack>playerHotBar = new ArrayList<>();
//
//    public SereneAbilitiesPlayerEquipment(SereneAbilitiesPlayer owner, Player player){
//        sPlayer = owner;
//        this.player = player;
//        for (int i = 0 ; i < 8 ; i++){
//            playerSereneAbilitiesHotBarEquipment.add(i, new ItemStack(Material.AIR));
//            playerHotBar.add(player.getInventory().getItem(i));
//        }
//    }
//
//    public void switchToSereneAbilities(){
//        for (int i = 0 ; i < 8 ; i++){
//            playerHotBar.set(i, player.getInventory().getItem(i));
//            player.getInventory().setItem(i, playerSereneAbilitiesHotBarEquipment.get(i));
//        }
//    }
//
//    public void switchToNormal(){
//        for (int i = 0 ; i < 8 ; i++){
//            playerSereneAbilitiesHotBarEquipment.set(i, player.getInventory().getItem(i));
//            player.getInventory().setItem(i, playerHotBar.get(i));
//        }
//    }
//
//}
