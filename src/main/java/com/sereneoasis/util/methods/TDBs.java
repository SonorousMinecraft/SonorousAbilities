package com.sereneoasis.util.methods;

import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class TDBs {

    public static void playTDBs(Location loc, DisplayBlock type, int amount, double size, double radius) {
        for (int i = 0; i < amount; i++) {
            if (radius != 0) {
                new TempDisplayBlock(loc.clone().add(Vector.getRandom().normalize().add(new Vector(-0.5, -0.5, -0.5)).normalize().multiply(radius)), type, 100, size);
            } else {
                new TempDisplayBlock(loc.clone(), type, 100, size);
            }
        }
    }
}
