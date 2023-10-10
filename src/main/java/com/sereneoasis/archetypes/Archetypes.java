package com.sereneoasis.archetypes;

public enum Archetypes {
    NONE("none"),
    OCEAN("ocean");
    //SUN("sun"),
    //WAR("war"),
    //NETHER("nether"),
    //EARTH("earth"),
    //NATURE("nature");

    private String name;

    Archetypes(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
