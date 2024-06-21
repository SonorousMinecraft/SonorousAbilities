package com.sereneoasis.util;

import com.sereneoasis.SerenityPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SerenityPlayerEquipment {

    private final SerenityPlayer sPlayer;
    private final Player player;

    private List<ItemStack>playerSerenityHotBarEquipment = new ArrayList<>();
    private List<ItemStack>playerHotBar = new ArrayList<>();

    public SerenityPlayerEquipment(SerenityPlayer owner, Player player){
        sPlayer = owner;
        this.player = player;
        for (int i = 0 ; i < 8 ; i++){
            playerSerenityHotBarEquipment.add(i, new ItemStack(Material.AIR));
            playerHotBar.add(player.getInventory().getItem(i));
        }
    }

    public void switchToSerenity(){
        for (int i = 0 ; i < 8 ; i++){
            playerHotBar.set(i, player.getInventory().getItem(i));
            player.getInventory().setItem(i, playerSerenityHotBarEquipment.get(i));
        }
    }

    public void switchToNormal(){
        for (int i = 0 ; i < 8 ; i++){
            playerSerenityHotBarEquipment.set(i, player.getInventory().getItem(i));
            player.getInventory().setItem(i, playerHotBar.get(i));
        }
    }

}
