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
    NOT_SHOT,
    SOURCE_SELECTED,

    SHOT,
    COMPLETE,
    DAMAGED,
    HIT_SOLID,
    CHARGED,

    CHARGING,

    MOVING,
    NOT_MOVING,
    ATTACKING,
    SHOOTING,
    ATTACHED,
    DROPPED,

    TELEPORTING,
    REVERTING,

    FLOATING,
    JUMPING,

    GRAB,
    GRAPPLE

}
