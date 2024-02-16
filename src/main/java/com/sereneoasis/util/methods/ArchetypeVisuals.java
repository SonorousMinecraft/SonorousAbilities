package com.sereneoasis.util.methods;

import com.sereneoasis.archetypes.DisplayBlock;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ArchetypeVisuals {

    public interface ArchetypeVisual
    {
        void playVisual(Location loc, double size, double radius, int tb, int cloud, int white);
    }

    public static class AirVisual implements ArchetypeVisual{

        @Override
        public void playVisual(Location loc, double size, double radius, int tb, int cloud, int white) {
            TDBs.playTDBs(loc, DisplayBlock.AIR, tb, size, radius);
            Particles.spawnParticle(Particle.CLOUD, loc, cloud, 0.5, 0);
            Particles.spawnColoredParticle(loc, white, radius, size*3, Color.fromRGB(242, 243, 244));
        }
    }
}
