package com.sereneoasis.ability.superclasses;

import com.sereneoasis.util.AbilityStatus;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

public abstract class MasterAbility extends CoreAbility{

    public interface HelperTick {
        void doHelperTick(AbilityStatus status) throws ReflectiveOperationException;
    }
    protected final WeakHashMap<CoreAbility, HelperTick> helpers = new WeakHashMap<>();

    public WeakHashMap<CoreAbility, HelperTick> getHelpers() {
        return helpers;
    }

    public MasterAbility(Player player, String name) {
        super(player, name);
    }

    private Set<CoreAbility>helpersToRemove = new HashSet<>();

    protected void addHelper(CoreAbility coreAbility, HelperTick helperTick){
        helpers.put(coreAbility, helperTick);
    }

    protected void removeHelper(CoreAbility coreAbility){
        helpersToRemove.add(coreAbility);
    }

    protected void iterateHelpers(AbilityStatus status){
        helpers.forEach((coreAbility, helperTick) -> {
            try {
                helperTick.doHelperTick(status);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        });

        helpersToRemove.forEach(coreAbility -> helpers.remove(coreAbility));

    }



}
