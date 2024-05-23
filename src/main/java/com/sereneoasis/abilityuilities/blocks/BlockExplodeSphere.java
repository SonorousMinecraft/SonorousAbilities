package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.SourceBlockToLoc;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.enhancedmethods.EnhancedBlocks;
import com.sereneoasis.util.enhancedmethods.EnhancedBlocksArchetypeLess;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Constants;
import com.sereneoasis.util.methods.Particles;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Sakrajin
 * Causes a spherical shaped blast to be shot from the player
 */
public class BlockExplodeSphere extends CoreAbility {

    private Location centerLoc;

    private String name;

    private double increment;


//    private Set<TempBlock> sourceTempBlocks = new HashSet<>();

    private HashMap<TempDisplayBlock, Vector> displayBlocks = new HashMap<>();


    private Random random = new Random();


    public BlockExplodeSphere(Player player, String name, Location startLoc, double increment) {
        super(player, name);

        this.name = name;

        this.centerLoc = startLoc.clone();
        this.increment = increment;
        start();
    }

    public BlockExplodeSphere(Player player, String name, Location startLoc, double radius, double increment) {
        super(player, name);

        this.name = name;

        this.centerLoc = startLoc.clone();
        this.increment = increment;
        this.radius = radius;
        start();
    }

    @Override
    public void progress() {
        radius -= increment;

        Set<Block> sourceBlocks = EnhancedBlocksArchetypeLess.getOutsideSphereBlocks(this, centerLoc);

        for (Block b : sourceBlocks) {
            if (b != null && !b.isPassable() ) {


                TempDisplayBlock tdb = new TempDisplayBlock(b, b.getType(), 60000, 1);
                Vector offset = Vectors.getDirectionBetweenLocations(centerLoc, b.getLocation()).add(new Vector(0,radius,0)).normalize();
                displayBlocks.put(tdb, offset);


//                if (TempBlock.isTempBlock(b) && !sourceTempBlocks.contains(TempBlock.getTempBlock(b))) {
                if (TempBlock.isTempBlock(b) && !b.getType().equals(Material.LIGHT)) {
                    TempBlock.getTempBlock(b).revert();
                }

                TempBlock tb = new TempBlock(b, Material.LIGHT, 60000, true);
//                sourceTempBlocks.add(tb);
            }
        }

        displayBlocks.forEach((tempDisplayBlock, vector) -> {
            tempDisplayBlock.moveTo(tempDisplayBlock.getLoc().clone().add(vector.clone().normalize()));
            vector.subtract(new Vector(0, Constants.GRAVITY,0));
        });



        if (radius-increment <= 0) {
            this.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();
        displayBlocks.forEach((tempDisplayBlock, vector) -> tempDisplayBlock.revert());


    }
    @Override
    public String getName() {
        return name;
    }
}
