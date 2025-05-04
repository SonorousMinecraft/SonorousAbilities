package com.sonorous.abilityuilities.blocks;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.enhancedmethods.EnhancedBlocksArchetypeLess;
import com.sonorous.util.methods.Constants;
import com.sonorous.util.methods.Vectors;
import com.sonorous.util.temp.TempBlock;
import com.sonorous.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
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
            if (b != null && !b.isPassable()) {


                TempDisplayBlock tdb = new TempDisplayBlock(b, b.getType(), 60000, 1);
                Vector offset = Vectors.getDirectionBetweenLocations(centerLoc, b.getLocation()).add(new Vector(0, radius, 0)).normalize();
                displayBlocks.put(tdb, offset);


                TempBlock tb = new TempBlock(b.getLocation().getBlock(), Material.AIR, 60000);


//                sourceTempBlocks.add(tb);
            }
        }

        displayBlocks.forEach((tempDisplayBlock, vector) -> {
            tempDisplayBlock.moveTo(tempDisplayBlock.getLoc().clone().add(vector.clone().normalize()));
            vector.subtract(new Vector(0, Constants.GRAVITY, 0));
        });


        if (radius - increment <= 0) {
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
