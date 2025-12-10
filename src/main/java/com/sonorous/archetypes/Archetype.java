package com.sonorous.archetypes;

import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;


public enum Archetype {
    NONE("none", 0),

    EARTH("earth", 5, TrimMaterial.EMERALD, TrimPattern.HOST),

    CHAOS("chaos", 3, TrimMaterial.AMETHYST, TrimPattern.DUNE),

    SUN("sun", 1, TrimMaterial.GOLD, TrimPattern.COAST),

    SKY("sky", 2, TrimMaterial.QUARTZ, TrimPattern.EYE),
    OCEAN("ocean", 4, TrimMaterial.LAPIS, TrimPattern.RIB);


    private String name;
    private int value;

    private ArmorTrim trim;

    Archetype(String name, int value) {
        this.name = name;
        this.value = value;
    }

    Archetype(String name, int value, TrimMaterial trimMaterial, TrimPattern trimPattern) {
        this.name = name;
        this.value = value;
        this.trim = new ArmorTrim(trimMaterial, trimPattern);
    }

    public ArmorTrim getTrim() {
        return trim;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }

}
