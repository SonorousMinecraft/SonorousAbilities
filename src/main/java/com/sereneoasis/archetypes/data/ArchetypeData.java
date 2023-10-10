package com.sereneoasis.archetypes.data;

import org.bukkit.Color;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ArchetypeData {

    private Map<Attribute, Double> ARCHETYPE_ATTRIBUTES = new HashMap<>();

    private Set<String>blocks = new HashSet<>();

    private String color;

    public ArchetypeData(Map<Attribute, Double>attributes, Set<String> blocks, String color)
    {
        this.ARCHETYPE_ATTRIBUTES = attributes;
        this.blocks = blocks;
        this.color = color;
    }



}
