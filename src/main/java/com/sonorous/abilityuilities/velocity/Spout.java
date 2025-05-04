package com.sonorous.abilityuilities.velocity;

import com.sonorous.ability.superclasses.CoreAbility;
import org.bukkit.entity.Player;

public class Spout extends CoreAbility {

    private final String name;

    public Spout(Player player, String name) {
        super(player, name);

        this.name = name;
    }

    @Override
    public void progress() {

    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
