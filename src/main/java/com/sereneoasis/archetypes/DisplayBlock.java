package com.sereneoasis.archetypes;

import org.bukkit.Material;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public enum DisplayBlock {


    WATER(new ArrayList<>(List.of(new Material[]{ Material.BLUE_STAINED_GLASS}))),
//    WATER(new ArrayList<>(List.of(new Material[]{Material.TUBE_CORAL_WALL_FAN, Material.TUBE_CORAL}))),

    ICE(new ArrayList<>(List.of(new Material[]{Material.ICE, Material.BLUE_ICE, Material.FROSTED_ICE, Material.PACKED_ICE}))),

    SNOW(new ArrayList<>(List.of(new Material[]{Material.SNOW_BLOCK}))),

//    SUN(new ArrayList<>(List.of(new Material[]{Material.RED_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS}))),
//    SUN(new ArrayList<>(List.of(new Material[]{Material.HONEY_BLOCK, Material.HONEYCOMB_BLOCK, Material.OCHRE_FROGLIGHT, Material.MAGMA_BLOCK}))),
//    SUN(new ArrayList<>(List.of(new Material[]{Material.HONEY_BLOCK, Material.GOLD_BLOCK}))),
//    SUN(new ArrayList<>(List.of(new Material[]{Material.RAW_GOLD_BLOCK, Material.SHROOMLIGHT, Material.HONEYCOMB_BLOCK}))),

//    SUN(new ArrayList<>(List.of(new Material[]{Material.HONEY_BLOCK}))),
//    SUN(new ArrayList<>(List.of(new Material[]{Material.RAW_GOLD_BLOCK,Material.SHROOMLIGHT, Material.OCHRE_FROGLIGHT}))),
//    SUN(new ArrayList<>(List.of(new Material[]{Material.RAW_GOLD_BLOCK,Material.SHROOMLIGHT, Material.HONEY_BLOCK}))),


//    SUN(new ArrayList<>(List.of(new Material[]{Material.RAW_GOLD_BLOCK,Material.NETHER_WART_BLOCK, Material.CRIMSON_HYPHAE, Material.NETHERRACK, Material.NETHER_GOLD_ORE, Material.NETHER_BRICKS, Material.RED_NETHER_BRICKS, Material.MAGMA_BLOCK}))),
    SUN(new ArrayList<>(List.of(new Material[]{Material.RAW_GOLD_BLOCK,Material.RAW_GOLD_BLOCK,Material.RAW_GOLD_BLOCK,Material.NETHER_WART_BLOCK, Material.CRIMSON_HYPHAE, Material.NETHERRACK, Material.NETHER_GOLD_ORE, Material.MAGMA_BLOCK}))),

//    SUN(new ArrayList<>(List.of(new Material[]{ Material.OCHRE_FROGLIGHT, Material.HONEY_BLOCK}))),
//    SUN(new ArrayList<>(List.of(new Material[]{Material.RAW_GOLD_BLOCK, Material.HONEYCOMB_BLOCK, Material.YELLOW_WOOL, Material.YELLOW_CONCRETE, Material.YELLOW_TERRACOTTA, Material.SHROOMLIGHT}))),

//    SUN(new ArrayList<>(List.of(new Material[]{Material.FIRE_CORAL_BLOCK, Material.NETHER_WART_BLOCK, Material.MAGMA_BLOCK}))),

    FIRE(new ArrayList<>(List.of(new Material[]{Material.FIRE, Material.SOUL_FIRE}))),

    AIR(new ArrayList<>(List.of(new Material[]{Material.WHITE_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS}))),

//    LIGHTNING(new ArrayList<>(List.of(new Material[]{Material.BLUE_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS}))),
    LIGHTNING(new ArrayList<>(List.of(new Material[]{Material.DIAMOND_BLOCK, Material.WHITE_CONCRETE}))),

    CHAOS(new ArrayList<>(List.of(new Material[] {Material.BLUE_STAINED_GLASS})));

    List<Material> blocks = new ArrayList<>();

    DisplayBlock(List<Material> blocks) {
        this.blocks = blocks;
    }

    public List<Material> getBlocks() {
        return blocks;
    }
}
