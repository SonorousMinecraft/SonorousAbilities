package com.sereneoasis.archetypes.chaos;

import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.abilityuilities.particles.ChargeSphere;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.enhancedmethods.EnhancedBlocks;
import com.sereneoasis.util.enhancedmethods.EnhancedDisplayBlocks;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Singularity extends MasterAbility {

    private static final String name = "Singularity";

    private Set<TempBlock> sourceTempBlocks = new HashSet<>();

    private HashMap<TempDisplayBlock, Vector> displayBlocks = new HashMap<>();

    private Vector previousDir;


    public Singularity(Player player) {
        super(player, name);

        if (shouldStart()) {
            ChargeSphere chargeSphere = new ChargeSphere(player, name, 1, new ArchetypeVisuals.ChaosVisual());
            getHelpers().put(chargeSphere, (abilityStatus) -> {
                switch (abilityStatus){
                    case CHARGING:
                        Set<Block> sourceBlocks = Blocks.getBlocksAroundPoint(chargeSphere.getLoc(), chargeSphere.getCurrentRadius());
                        for (Block b : sourceBlocks) {
                            if (b != null) {
                                TempDisplayBlock tdb = new TempDisplayBlock(b, b.getType(), 60000, 1);
                                Vector offset = Vectors.getDirectionBetweenLocations(chargeSphere.getLoc(), b.getLocation());
                                displayBlocks.put(tdb, offset);


                                if (TempBlock.isTempBlock(b) && !sourceTempBlocks.contains(TempBlock.getTempBlock(b))) {
                                    TempBlock.getTempBlock(b).revert();
                                }
                                TempBlock tb = new TempBlock(b, Material.AIR, 60000, true);
                                sourceTempBlocks.add(tb);
                            }
                        }

                    case CHARGED:

                    case SHOT:
                }
            });
            previousDir = player.getEyeLocation().getDirection();
            abilityStatus = AbilityStatus.CHARGING;
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        iterateHelpers(abilityStatus);

        if (abilityStatus == AbilityStatus.CHARGING){
            if (!player.isSneaking()){
                if (System.currentTimeMillis() > startTime + chargeTime) {
                    abilityStatus = AbilityStatus.CHARGED;
                } else {
                    this.remove();
                }
            }
        }
        Vector newDir = player.getEyeLocation().getDirection();

        EnhancedDisplayBlocks.orientOrganisedDBs(displayBlocks, previousDir, newDir, player, radius);

        previousDir = newDir;


    }

    public void setHasClicked(){
        if (abilityStatus == AbilityStatus.CHARGED) {
            abilityStatus = AbilityStatus.SHOT;
        }
    }

    @Override
    public void remove() {
        super.remove();

        displayBlocks.forEach((tempDisplayBlock, vector) -> tempDisplayBlock.revert());

        sPlayer.addCooldown(name, cooldown);
    }

    @Override
    public String getName() {
        return name;
    }
}
