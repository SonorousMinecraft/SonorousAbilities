package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.enhancedmethods.EnhancedBlocks;
import com.sereneoasis.util.enhancedmethods.EnhancedDisplayBlocks;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        Set<Block> sourceBlocks = EnhancedBlocks.getFacingSphereBlocks(this);

        if (!sourceBlocks.isEmpty()){
            displayBlockDistance = Blocks.getFacingBlockLoc(player, sourceRange).distance(player.getEyeLocation());
            previousDir = player.getEyeLocation().getDirection();
            Location center = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(displayBlockDistance));
            for (Block b : sourceBlocks){
                if (b!=null) {
                    TempDisplayBlock tdb = new TempDisplayBlock(b.getLocation(), b.getType(), 60000, 1);
                    Vector offset = Vectors.getDirectionBetweenLocations(center, b.getLocation());
                    displayBlocks.put(tdb, offset);
                    if (TempBlock.isTempBlock(b)){
                        TempBlock.getTempBlock(b).revert();
                    }
                    TempBlock tb = new TempBlock(b, Material.AIR, 60000, true);
                    sourceTempBlocks.add(tb);
                }
            }
            abilityStatus = AbilityStatus.CHARGED;
            start();
        }

    }


    @Override
    public void progress() throws ReflectiveOperationException {



        if (abilityStatus == AbilityStatus.CHARGED){
            if (player.isSneaking()) {
                Vector newDir = player.getEyeLocation().getDirection();
                EnhancedDisplayBlocks.orientOrganisedDBs(displayBlocks, previousDir, newDir, player, displayBlockDistance);
                previousDir = newDir;
            } else{
                this.remove();
            }
        }
        else if (abilityStatus == AbilityStatus.SHOT){
            EnhancedDisplayBlocks.handleMoveOrganisedDBsAndHit(displayBlocks, player, this);
        }
    }

    public void setHasClicked(){
        if (abilityStatus == AbilityStatus.CHARGED){
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
        sPlayer.addCooldown(name,cooldown);
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