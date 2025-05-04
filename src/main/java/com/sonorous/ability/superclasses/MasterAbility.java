package com.sonorous.ability.superclasses;

import com.sonorous.util.AbilityStatus;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

/***
 * Provides an optional easy to use interface for helper abilities
 */
public abstract class MasterAbility extends CoreAbility {

    protected final WeakHashMap<CoreAbility, HelperTick> helpers = new WeakHashMap<>();
    private final Set<CoreAbility> helpersToRemove = new HashSet<>();

    public MasterAbility(Player player, String name) {
        super(player, name);
    }

    public WeakHashMap<CoreAbility, HelperTick> getHelpers() {
        return helpers;
    }

    protected void addHelper(CoreAbility coreAbility, HelperTick helperTick) {
        helpers.put(coreAbility, helperTick);
    }

    protected void removeHelper(CoreAbility coreAbility) {
        helpersToRemove.add(coreAbility);
    }

    protected void iterateHelpers(AbilityStatus status) {
        helpers.forEach((coreAbility, helperTick) -> {
            try {
                helperTick.doHelperTick(status);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        });

        helpersToRemove.forEach(helpers::remove);

    }

    public interface HelperTick {
        void doHelperTick(AbilityStatus status) throws ReflectiveOperationException;
    }


}
