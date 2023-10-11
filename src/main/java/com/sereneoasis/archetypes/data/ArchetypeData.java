package com.sereneoasis.archetypes.data;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ArchetypeData {

    private Map<Attribute, Double> ARCHETYPE_ATTRIBUTES = new HashMap<>();

    private Set<Material>blocks = new HashSet<>();

    private String color;

    public ArchetypeData(Map<Attribute, Double>attributes, Set<Material> blocks, String color)
    {
        this.ARCHETYPE_ATTRIBUTES = attributes;
        this.blocks = blocks;
        this.color = color;
    }

    public Map<Attribute, Double> getArchetypeAttributes() {
        return ARCHETYPE_ATTRIBUTES;
    }

    public Set<Material> getBlocks() {
        return blocks;
    }

    public String getColor() {
        return color;
    }
}
