package com.sereneoasis.util.equipment;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemStackUtils {

    public static void createEquipmentSetPlayerSlot(Player player, Material material, String displayName, List<String>lore, EquipmentSlot equipmentSlot){
        player.getEquipment().setItem(equipmentSlot, createItem(material, displayName, lore));
    }


    private  static ItemStack createItem(Material material, String displayName, List<String>lore){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        meta.setUnbreakable(true);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
