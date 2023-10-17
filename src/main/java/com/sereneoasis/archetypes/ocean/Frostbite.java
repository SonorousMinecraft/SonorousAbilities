package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.ShootBlockToLoc;
import com.sereneoasis.abilityuilities.blocks.ShootBlockToLoc;
import com.sereneoasis.archetypes.data.ArchetypeData;
import com.sereneoasis.archetypes.data.ArchetypeDataManager;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.*;
import com.sereneoasis.util.temp.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Frostbite extends CoreAbility {


    private Block sourceBlock1, sourceBlock2;

    private boolean hasSourced2 = false, hasShot = false, hasSpawnedShots = false;

    private ShootBlockToLoc shootBlockToLoc1, shootBlockToLoc2;
    
    private double currentDistance = 0;
    

    public Frostbite(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()))
        {
            return;
        }

        sourceBlock1 = Blocks.getSourceBlock(player, sPlayer, sourceRange);
        if (sourceBlock1 != null)
        {
            start();
        }
    }

    @Override
    public void progress() {
        
        if (player.isSneaking())
        {
            if (currentDistance < range) {
                currentDistance += (range * 50) / chargetime;
            }
        }
        if (!hasSourced2)
        {
            Particles.spawnColoredParticle(sourceBlock1.getLocation().add(0,1,0),
                    5, 0.5, 1, ArchetypeDataManager.getArchetypeData(sPlayer.getArchetype()).getColor());
        }
        else {
            if (!hasShot) {
                Particles.spawnColoredParticle(sourceBlock1.getLocation().add(0,1,0),
                        5, 0.5, 1, ArchetypeDataManager.getArchetypeData(sPlayer.getArchetype()).getColor());

                Particles.spawnColoredParticle(sourceBlock2.getLocation().add(0, 1, 0),
                        5, 0.5, 1, ArchetypeDataManager.getArchetypeData(sPlayer.getArchetype()).getColor());
            }
            else {
                if (!hasSpawnedShots) {
                    hasSpawnedShots = true;
                    Location endLoc = Locations.getFacingLocation(player.getEyeLocation(), player.getEyeLocation().getDirection().normalize(), currentDistance);
                    shootBlockToLoc1 = new ShootBlockToLoc(player, "Frostbite", sourceBlock1.getLocation()
                            , Material.ICE, endLoc);
                    shootBlockToLoc2 = new ShootBlockToLoc(player, "Frostbite", sourceBlock2.getLocation()
                            , Material.ICE, endLoc);
                }
                else{
                    Location loc1 = shootBlockToLoc1.getLoc();
                    Location loc2 = shootBlockToLoc2.getLoc();
                    if (loc1.distance(loc2) < 2)
                    {
                        Location explode = loc1.add(Vectors.getDirectionBetweenLocations(loc1,loc2).multiply(0.5));
                        for (Block b : Blocks.getBlocksAroundPoint(explode, radius))
                        {
                            TempBlock tb = new TempBlock(b, Material.ICE.createBlockData(), 5000);
                        }
                        DamageHandler.damageEntity(Entities.getAffected(explode, radius, player), player, this, damage);
                        this.remove();
                    }
                }
            }
        }
    }

    public void setSourceBlock2()
    {
        if (!hasSourced2)
        {
            sourceBlock2 = Blocks.getSourceBlock(player, sPlayer, sourceRange);
            if (sourceBlock2 != null)
            {
                hasSourced2 = true;
            }

        }
    }

    public void setHasClicked()
    {
        if (hasSourced2 && !hasShot)
        {
            hasShot = true;
        }
    }

    @Override
    public void remove()
    {
        super.remove();

        if (shootBlockToLoc1 != null && shootBlockToLoc2 != null)
        {
            shootBlockToLoc1.remove();
            shootBlockToLoc2.remove();
        }

    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return "Frostbite";
    }
}
