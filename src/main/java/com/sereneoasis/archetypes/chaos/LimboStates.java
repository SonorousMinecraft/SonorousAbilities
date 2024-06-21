package com.sereneoasis.archetypes.chaos;

public enum LimboStates {

    NORMAL("normal"),
    LIMBO("limbo");


    private String name;

    LimboStates(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
