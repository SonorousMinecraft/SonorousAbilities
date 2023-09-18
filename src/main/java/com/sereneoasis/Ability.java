package com.sereneoasis;

import org.bukkit.entity.Player;

public interface Ability {

    public void progress();

    public void remove();

    public boolean isSneakAbility();

    public boolean isEnabled();

    public long getCooldown();

    public Player getPlayer();

    public String getName();

    public String getInstructions();

    public String getDescription();

    public Element getElement();
}
