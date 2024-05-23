package com.sereneoasis.archetypes;

/**
 * @author Sakrajin
 * Enums to represent different archetypes
 */
public enum Archetype {
    NONE("none"),

    EARTH("earth"),

    CHAOS("chaos"),

    SUN("sun"),

    SKY("sky"),
    OCEAN("ocean");


    private String name;

    Archetype(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
