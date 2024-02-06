package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.velocity.Skate;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class EarthSurf extends CoreAbility {

    private final String name = "EarthSurf";


    private HashMap<Integer, TempDisplayBlock> wave;

    private Skate skate;

    private long startTime;

    private Material type;

    public EarthSurf(Player player) {
        super(player);
        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(name)) {
            return;
        }

        skate = new Skate(player, name, 5, false);
        if (skate.getAbilityStatus() == AbilityStatus.MOVING) {
            wave = new HashMap<>();

            this.startTime = System.currentTimeMillis();
            start();
        }
    }

    @Override
    public void progress() {


        if (System.currentTimeMillis() > startTime + duration | skate.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
            return;
        }

        Vector dir = player.getEyeLocation().getDirection().setY(0).normalize();

        Location waveLoc = player.getLocation().clone().subtract(dir.clone().multiply(speed * 3));
        Set<Location> waveLocs = new HashSet<>();
        for (double i = 0; i < radius; i += 0.5) {
            waveLocs.addAll(Locations.getPerpArcFromVector(waveLoc.clone().subtract(0,i,0), dir, i, 90, 270, 10));
        }

        if (skate.getFloorBlock() != null){
            type = skate.getFloorBlock().getType();
        }
        Entities.handleDisplayBlockEntities(wave,
                waveLocs,
                type, 0.5);
    }

    @Override
    public void remove() {
        super.remove();
        skate.remove();
        sPlayer.addCooldown(name, cooldown);
        for (TempDisplayBlock tb : wave.values()) {
            tb.revert();
        }

    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return name;
    }
}
