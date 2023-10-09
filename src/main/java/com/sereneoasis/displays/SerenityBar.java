package com.sereneoasis.displays;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SerenityBar {

    private final static Map<Player, BossBar> combatBar = new ConcurrentHashMap<>();

    public SerenityBar()
    {
        BossBar bossBar = Bukkit.getServer().createBossBar("Combat", BarColor.RED, BarStyle.SOLID);
    }

}
