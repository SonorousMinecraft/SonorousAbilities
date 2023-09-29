package com.sereneoasis.ability;

import com.sereneoasis.Element;
import org.bukkit.entity.Player;

public interface Ability {

    public void progress();

    public void remove();

    public Player getPlayer();

    public String getName();
}
