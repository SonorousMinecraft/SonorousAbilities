package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.velocity.Skate;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.enhancedmethods.EnhancedBlocks;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class TerraSurf extends CoreAbility {

    private final String name = "TerraSurf";


    private HashMap<Integer, TempDisplayBlock> wave;

    private Skate skate;

    private long startTime;

    private Material type;

    public TerraSurf(Player player) {
        super(player, "TerraSurf");

        if (shouldStart()) {
            skate = new Skate(player, name, 8, 3, false);
            if (skate.getAbilityStatus() == AbilityStatus.MOVING) {
                wave = new HashMap<>();

                this.startTime = System.currentTimeMillis();
                start();
            }
        }
    }

    @Override
    public void progress() {


        if (System.currentTimeMillis() > startTime + duration | skate.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
            return;
        }

        Vector dir = player.getEyeLocation().getDirection().setY(0).normalize();
//        EnhancedBlocks.getFacingSphereBlocks(this, player.getEyeLocation().add(dir.clone().multiply(speed ))).forEach(block -> {
//            new TempBlock(block, Material.AIR, 1000, true);
//        });

        EnhancedBlocks.getFacingSphereBlocks(this, player.getEyeLocation().subtract(dir.clone().multiply(speed * 2)).subtract(0, 4, 0)).forEach(block -> {
            TempDisplayBlock tdb = new TempDisplayBlock(block, block.getType(), 200, 1);
            tdb.moveToAndMaintainFacing(tdb.getLoc().add(0, 0.5, 0));


        });

        if (skate.getFloorBlock() != null) {
            type = skate.getFloorBlock().getType();
        }

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