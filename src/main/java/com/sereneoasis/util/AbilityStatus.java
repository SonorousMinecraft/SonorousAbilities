package com.sereneoasis.util;

/**
 * @author Sakrajin
 * Represents all possible ability states.
 * Main ability classes should use these to get the progress of helper abilities.
 */
public enum AbilityStatus {
    NO_SOURCE,
    SOURCING,
    SOURCED,

    SOURCE_SELECTED,
    SHOT,
    COMPLETE,
    CHARGED,

}
