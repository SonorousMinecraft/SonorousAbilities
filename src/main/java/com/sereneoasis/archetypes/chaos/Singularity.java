package com.sereneoasis.archetypes.chaos;

import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.abilityuilities.particles.ChargeSphere;
import com.sereneoasis.abilityuilities.particles.SphereBlast;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.enhancedmethods.EnhancedBlocks;
import com.sereneoasis.util.enhancedmethods.EnhancedDisplayBlocks;
import com.sereneoasis.util.methods.*;
import com.sereneoasis.util.temp.TempBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Singularity extends MasterAbility {

    private static final String name = "Singularity";

    private Set<TempBlock> sourceTempBlocks = new HashSet<>();

    private HashMap<TempDisplayBlock, Vector> displayBlocks = new HashMap<>();


    private Vector previousDir;

    private Location loc;

    public Singularity(Player player) {
        super(player, name);

        if (shouldStart()) {
            ChargeSphere chargeSphere = new ChargeSphere(player, name, 1, new ArchetypeVisuals.ChaosVisual());
            getHelpers().put(chargeSphere, (abilityStatus) -> {
                switch (abilityStatus){
                    case CHARGING:

                        if (!player.isSneaking()) {
                            chargeSphere.remove();
                            this.remove();
                        }
                        Set<Block> sourceBlocks = Blocks.getBlocksAroundPoint(chargeSphere.getLoc(), chargeSphere.getCurrentRadius() + 2);
                        for (Block b : sourceBlocks) {
                            if (b != null && !b.isPassable() && b.getType() != Material.CRYING_OBSIDIAN) {

                                    TempDisplayBlock tdb = new TempDisplayBlock(b, b.getType(), 60000, 2);
                                    Vector offset = Vectors.getDirectionBetweenLocations(chargeSphere.getLoc(), b.getLocation());
                                    displayBlocks.put(tdb, offset);

                                if (TempBlock.isTempBlock(b) && !sourceTempBlocks.contains(TempBlock.getTempBlock(b))) {
                                    TempBlock.getTempBlock(b).revert();
                                    TempBlock tb = new TempBlock(b.getLocation().getBlock(), Material.AIR, 60000, true);
                                    sourceTempBlocks.add(tb);
                                } else {
                                    TempBlock tb = new TempBlock(b, Material.AIR, 60000, true);
                                    sourceTempBlocks.add(tb);
                                }
                            }
                        }
                        this.loc = chargeSphere.getLoc();
                        break;
                    case CHARGED:
                        this.loc = chargeSphere.getLoc();

                        break;

                    case SHOT:
                        chargeSphere.remove();
                        break;
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

        if (abilityStatus != AbilityStatus.COMPLETE) {

            Vector newDir = player.getEyeLocation().getDirection();

            EnhancedDisplayBlocks.orientOrganisedDBsGivenCenter(displayBlocks, previousDir, newDir, player, loc);

            previousDir = newDir;
        }

        if (abilityStatus == AbilityStatus.CHARGING){
            if (System.currentTimeMillis() > startTime + chargeTime) {
                abilityStatus = AbilityStatus.CHARGED;

                List<TempDisplayBlock> blocks = null;
                if (displayBlocks.keySet().size() > 100) {
                    blocks = displayBlocks.keySet().stream().collect(Collectors.toList()).subList(0, 100);
                }else {
                    blocks = displayBlocks.keySet().stream().collect(Collectors.toList()).subList(0, displayBlocks.size());

                }

                List<TempDisplayBlock> finalBlocks = blocks;
                displayBlocks.forEach((tempDisplayBlock, vector) -> {
                    if (!finalBlocks.contains(tempDisplayBlock)){
                        tempDisplayBlock.revert();
                    } else {
                        tempDisplayBlock.setSize(1);
                        vector.normalize();
                    }
                });
                List<TempDisplayBlock> absentBlocks = displayBlocks.keySet().stream()
                        .filter(tempDisplayBlock -> !finalBlocks.contains(tempDisplayBlock))
                        .toList();

                absentBlocks.forEach(tempDisplayBlock -> displayBlocks.remove(tempDisplayBlock));;
            }
            else {
                displayBlocks.forEach((tempDisplayBlock, vector) -> {
                    tempDisplayBlock.setScale(0.5);
                    vector.multiply(0.5);
                });
            }
        } else if (abilityStatus == AbilityStatus.CHARGED || abilityStatus == AbilityStatus.SHOT){
            displayBlocks.forEach((tempDisplayBlock, vector) -> {
                vector.normalize().multiply(RandomUtils.getRandomDouble(-radius*2, radius*2));
                vector.rotateAroundX(Math.toRadians(RandomUtils.getRandomDouble(-180, 180)));
                vector.rotateAroundY(Math.toRadians(RandomUtils.getRandomDouble(-180, 180)));
                vector.rotateAroundZ(Math.toRadians(RandomUtils.getRandomDouble(-180, 180)));
                tempDisplayBlock.setSize((float) (RandomUtils.getRandomDouble(0, 1) ));
                tempDisplayBlock.rotate( (float) RandomUtils.getRandomDouble(0, 360), (float) RandomUtils.getRandomDouble(0, 360));
            });
        }

    }

    public void setHasClicked(){
        if (abilityStatus == AbilityStatus.CHARGED) {
            abilityStatus = AbilityStatus.SHOT;

            SphereBlast sphereBlast = new SphereBlast(player, name, true, new ArchetypeVisuals.ChaosVisual(), true);
            sphereBlast.setLoc(loc);
            Particles.spawnParticle(Particle.SONIC_BOOM, sphereBlast.getLoc(), 20, radius * 2, 1);
            getHelpers().put(sphereBlast, (abilityStatus) -> {
                switch (abilityStatus) {
                    case SHOT:
                        this.loc = sphereBlast.getLoc();
                        if (  sphereBlast.getAbilityStatus() == AbilityStatus.COMPLETE) {
                            this.abilityStatus = AbilityStatus.COMPLETE;

                            Particles.spawnParticle(Particle.SONIC_BOOM, sphereBlast.getLoc(), 20, radius * 2, 1);
                            displayBlocks.keySet().forEach((tempDisplayBlock) -> {
                                tempDisplayBlock.setSize(1);
                            });
//                            Scheduler.performTaskLater(100L, () -> {
                                sphereBlast.remove();
                                this.remove();
//                            });

                        } else {
                            Set<Block> sourceBlocks = Blocks.getBlocksAroundPoint(loc, radius);

                            for (Block b : sourceBlocks) {
                                if (b != null && !b.isPassable()) {
                                    if (TempBlock.isTempBlock(b) && !sourceTempBlocks.contains(TempBlock.getTempBlock(b))) {
                                        TempBlock.getTempBlock(b).revert();
                                        TempBlock tb = new TempBlock(b.getLocation().getBlock(), Material.AIR, 60000, true);
                                        sourceTempBlocks.add(tb);
                                    } else {
                                        TempBlock tb = new TempBlock(b, Material.AIR, 60000, true);
                                        sourceTempBlocks.add(tb);
                                    }

                                }
                            }
                        }
                        break;
                    case COMPLETE:
                        displayBlocks.forEach((tempDisplayBlock, vector) -> {
                            tempDisplayBlock.moveTo(tempDisplayBlock.getLoc().clone().add(vector.clone().normalize()));
                            vector.subtract(new Vector(0, Constants.GRAVITY,0));
                        });

                        break;
                }
            });
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
