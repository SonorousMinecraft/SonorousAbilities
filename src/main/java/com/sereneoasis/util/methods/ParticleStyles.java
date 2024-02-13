package com.sereneoasis.util.methods;

import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleStyles {

    public static double PI = Math.PI;

    public static void playHelix(Location loc, Vector dir, double distance, int points, int height, int startAngle, boolean anticlockwise) {
        double tempDistance = 0;
        for (double d = 0 + startAngle; d < (2 * PI) + startAngle; d += (2 * PI / points)) {
            Location tempLoc = loc.clone();
            Vector tempDir = dir.clone();
            tempDistance += distance / points;
            Vector orthoDir = Vectors.getOrthogonalVector(tempDir, d, tempDistance);
            if (anticlockwise) {
                tempLoc.add(orthoDir.rotateAroundAxis(tempDir, d));
            } else {
                tempLoc.add(orthoDir.rotateAroundAxis(tempDir, -d));
            }
            loc.add(tempDir.multiply(height / points));

            new TempDisplayBlock(tempLoc, DisplayBlock.SUN, 200, 0.1);
        }
    }

    public static void playSeveralHelixes(Location loc, Vector dir, double distance, int points, int height, int startAngle, boolean anticlockwise, int helixes) {
        int currentAngle = 0;
        for (int i = 0; i < helixes; i++) {
            playHelix(loc, dir, distance, points, height, currentAngle, anticlockwise);
            currentAngle += 360 / helixes;
        }
    }


}
