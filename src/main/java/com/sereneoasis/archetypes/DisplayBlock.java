package com.sereneoasis.archetypes;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public enum DisplayBlock {


    WATER(new ArrayList<>(List.of(new Material[]{Material.BLUE_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS, Material.CYAN_STAINED_GLASS}))),

    ICE(new ArrayList<>(List.of(new Material[]{Material.ICE, Material.BLUE_ICE, Material.FROSTED_ICE, Material.PACKED_ICE}))),

    SNOW(new ArrayList<>(List.of(new Material[]{Material.SNOW_BLOCK}))),

    SUN(new ArrayList<>(List.of(new Material[]{Material.RED_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS}))),

    FIRE(new ArrayList<>(List.of(new Material[]{Material.FIRE, Material.SOUL_FIRE}))),

    AIR(new ArrayList<>(List.of(new Material[]{Material.WHITE_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS}))),

    LIGHTNING(new ArrayList<>(List.of(new Material[]{Material.WHITE_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS})));

    List<Material> blocks = new ArrayList<>();

    DisplayBlock(List<Material> blocks) {
        this.blocks = blocks;
    }

    public List<Material> getBlocks() {
        return blocks;
    }
}
