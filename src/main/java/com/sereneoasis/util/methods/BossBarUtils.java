package com.sereneoasis.util.methods;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarUtils {

    public static BossBar initBar(Player player, String name, BarColor barColor) {
        BossBar bar = Bukkit.getServer().createBossBar(name, barColor, BarStyle.SOLID);
        bar.addPlayer(player);
        return bar;
    }

    public static void manageBarDuration(BossBar bar, Player player, long startTime, long duration) {
        long timeElapsed = System.currentTimeMillis() - startTime;
        double progress = 1 - (double) timeElapsed / (double) duration;
        if (progress < 0) {
            bar.setProgress(0);
        } else {
            bar.setProgress(progress);
        }
    }
}
