package com.sereneoasis.archetypes;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public enum DisplayBlock {


    WATER(new ArrayList<>(List.of(new Material[]{Material.BLUE_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS, Material.CYAN_STAINED_GLASS}))),

    ICE(new ArrayList<>(List.of(new Material[]{Material.ICE, Material.BLUE_ICE, Material.FROSTED_ICE, Material.PACKED_ICE}))),

    SNOW(new ArrayList<>(List.of(new Material[]{Material.SNOW_BLOCK}))),

    SUN(new ArrayList<>(List.of(new Material[]{Material.SHROOMLIGHT})));

    List<Material> blocks = new ArrayList<>();

    DisplayBlock(List<Material> blocks)
    {
        this.blocks = blocks;
    }

    public List<Material> getBlocks() {
        return blocks;
    }
}
