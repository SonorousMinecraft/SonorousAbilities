package com.sereneoasis.archetypes;

import java.lang.invoke.VarHandle;

/**
 * @author Sakrajin
 * Enums to represent different archetypes
 */
public enum Archetype {
    NONE("none"),
    OCEAN("ocean"),
    SUN("sun");
    //WAR("war"),
    //NETHER("nether"),
    //EARTH("earth"),
    //NATURE("nature");

    private String name;

    Archetype(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
