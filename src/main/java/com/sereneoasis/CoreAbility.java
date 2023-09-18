package com.sereneoasis;

import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CoreAbility implements Ability{

    private static final Set<CoreAbility> INSTANCES = Collections.newSetFromMap(new ConcurrentHashMap<CoreAbility, Boolean>());

    protected Player player;

    public CoreAbility(final Player player)
    {
        this.player = player;
    }

    public void start()
    {
        INSTANCES.add(this);
    }

    public static void progressAll()
    {
        for (CoreAbility abil : INSTANCES)
        {
            abil.progress();
        }
    }
}
