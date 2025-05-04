//package com.sereneoasis.util;
//
//import com.sereneoasis.SonorousAbilitiesPlayer;
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SonorousAbilitiesPlayerEquipment {
//
//    private final SonorousAbilitiesPlayer sPlayer;
//    private final Player player;
//
//    private List<ItemStack>playerSonorousAbilitiesHotBarEquipment = new ArrayList<>();
//    private List<ItemStack>playerHotBar = new ArrayList<>();
//
//    public SonorousAbilitiesPlayerEquipment(SonorousAbilitiesPlayer owner, Player player){
//        sPlayer = owner;
//        this.player = player;
//        for (int i = 0 ; i < 8 ; i++){
//            playerSonorousAbilitiesHotBarEquipment.add(i, new ItemStack(Material.AIR));
//            playerHotBar.add(player.getInventory().getItem(i));
//        }
//    }
//
//    public void switchToSonorousAbilities(){
//        for (int i = 0 ; i < 8 ; i++){
//            playerHotBar.set(i, player.getInventory().getItem(i));
//            player.getInventory().setItem(i, playerSonorousAbilitiesHotBarEquipment.get(i));
//        }
//    }
//
//    public void switchToNormal(){
//        for (int i = 0 ; i < 8 ; i++){
//            playerSonorousAbilitiesHotBarEquipment.set(i, player.getInventory().getItem(i));
//            player.getInventory().setItem(i, playerHotBar.get(i));
//        }
//    }
//
//}
