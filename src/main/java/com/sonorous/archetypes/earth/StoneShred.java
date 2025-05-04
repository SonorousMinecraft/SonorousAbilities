package com.sonorous.archetypes.earth;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.enhancedmethods.EnhancedBlocks;
import com.sonorous.util.enhancedmethods.EnhancedDisplayBlocks;
import com.sonorous.util.methods.Blocks;
import com.sonorous.util.methods.Vectors;
import com.sonorous.util.temp.TempBlock;
import com.sonorous.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class StoneShred extends CoreAbility {

    private final String name = "StoneShred";

    private Set<TempBlock> sourceTempBlocks = new HashSet<>();
    private HashMap<TempDisplayBlock, Vector> displayBlocks = new HashMap<>();

    private Vector previousDir, shotDir;

    private double displayBlockDistance;
    private Location origin;


    public StoneShred(Player player) {
        super(player, "StoneShred");

        if (shouldStart()) {

            Set<Block> sourceBlocks = EnhancedBlocks.getFacingSphereBlocks(this);

            if (!sourceBlocks.isEmpty()) {
                displayBlockDistance = Blocks.getFacingBlockLoc(player, sourceRange).distance(player.getEyeLocation());
                previousDir = player.getEyeLocation().getDirection();
                Location center = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(displayBlockDistance));
                for (Block b : sourceBlocks) {
                    if (b != null) {
                        TempDisplayBlock tdb = new TempDisplayBlock(b, b.getType(), 60000, 1);
                        Vector offset = Vectors.getDirectionBetweenLocations(center, b.getLocation());
                        displayBlocks.put(tdb, offset);
                        TempBlock tb = new TempBlock(b, Material.AIR, 60000);
                        sourceTempBlocks.add(tb);

                    }
                }
                abilityStatus = AbilityStatus.CHARGED;
                start();
            }
        }

    }


    @Override
    public void progress() throws ReflectiveOperationException {


        if (abilityStatus == AbilityStatus.CHARGED) {
            if (player.isSneaking()) {
                Vector newDir = player.getEyeLocation().getDirection();
                EnhancedDisplayBlocks.orientOrganisedDBs(displayBlocks, previousDir, newDir, player, displayBlockDistance);
                previousDir = newDir;
            } else {
                this.remove();
            }
        } else if (abilityStatus == AbilityStatus.SHOT) {
            EnhancedDisplayBlocks.handleMoveOrganisedDBsAndHit(displayBlocks, player, this);
        }
    }

    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.CHARGED) {
            abilityStatus = AbilityStatus.SHOT;
            shotDir = player.getEyeLocation().getDirection();
            origin = player.getLocation();
            displayBlocks.replaceAll((k, v) -> shotDir);
        }
    }

    @Override
    public void remove() {
        super.remove();
        displayBlocks.forEach((tempDisplayBlock, vector) -> tempDisplayBlock.revert());

        sourceTempBlocks.forEach(tempBlock -> tempBlock.revert());
        sPlayer.addCooldown(name, cooldown);
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