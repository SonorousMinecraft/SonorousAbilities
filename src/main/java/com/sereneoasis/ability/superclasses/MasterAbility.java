package com.sereneoasis.ability.superclasses;

import com.sereneoasis.util.AbilityStatus;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class MasterAbility extends CoreAbility{

    public interface HelperTick {
        void doHelperTick(AbilityStatus status);
    }
    protected final HashMap<CoreAbility, HelperTick> helpers = new HashMap<>();
    public MasterAbility(Player player, String name) {
        super(player, name);
    }

    protected void iterateHelpers(AbilityStatus status){
        helpers.values().forEach(helperTick -> helperTick.doHelperTick(status));
    }


}
