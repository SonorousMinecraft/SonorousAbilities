package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.velocity.Skate;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Tsunami extends CoreAbility {

    private final String name = "Tsunami";


    private HashMap<Integer, TempDisplayBlock> wave;

    private Skate skate;

    private long startTime;

    public Tsunami(Player player) {
        super(player);
        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(name))
        {
            return;
        }

        skate = new Skate(player, name, 5, Material.WATER);
        if (skate.getAbilityStatus() == AbilityStatus.MOVING) {
            wave = new HashMap<>();

            this.startTime = System.currentTimeMillis();
            start();
        }
    }

    @Override
    public void progress() {


        if (System.currentTimeMillis() > startTime+duration | skate.getAbilityStatus() == AbilityStatus.COMPLETE)
        {
            this.remove();
            return;
        }

        Vector dir = player.getEyeLocation().getDirection().setY(0).normalize();

        Location waveLoc = player.getLocation().clone();
        Set<Location> waveLocs = new HashSet<>();
        for (double i = 0; i < radius; i +=0.5) {
            waveLocs.addAll(Locations.getPerpArcFromVector(waveLoc.clone().add(0,i,0), dir, i, 90, 270, 10));
        }
        wave = Entities.handleDisplayBlockEntities(wave,
                waveLocs,
                Material.BLUE_STAINED_GLASS, 0.5);
    }

    @Override
    public void remove()
    {
        super.remove();
        skate.remove();
        sPlayer.addCooldown(name,cooldown);
        for (TempDisplayBlock tb : wave.values())
        {
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
