package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.data.ArchetypeDataManager;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Display;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StoneShred extends CoreAbility {

    private final String name = "StoneShred";

    private Set<TempBlock> sourceTempBlocks = new HashSet<>();
    private HashMap<TempDisplayBlock, Vector> displayBlocks = new HashMap<>();

    private Vector previousDir, shotDir;

    private Location origin;


    public StoneShred(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        Set<Block> sourceBlocks = Blocks.getBlocksAroundPoint(Blocks.getFacingBlockLoc(player, sourceRange), radius).stream()
                .filter(block ->  Blocks.getArchetypeBlocks(sPlayer).contains(block.getType()))
                .collect(Collectors.toSet());

        if (!sourceBlocks.isEmpty()){
            previousDir = player.getEyeLocation().getDirection();
            for (Block b : sourceBlocks){
                TempDisplayBlock tdb = new TempDisplayBlock(b.getLocation(), b.getType(), 60000, 1);
                Vector offset = Vectors.getDirectionBetweenLocations(player.getLocation(), b.getLocation());
                displayBlocks.put(tdb, offset);
                TempBlock tb = new TempBlock(b, Material.AIR, 60000, true);
                sourceTempBlocks.add(tb);
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
                for (Map.Entry<TempDisplayBlock, Vector> entry : displayBlocks.entrySet()) {
                    double pitchDiff = Vectors.getPitchDiff(previousDir, newDir, player);
                    double yawDiff = Vectors.getYawDiff(previousDir, newDir, player);
                    entry.getValue().rotateAroundY(-yawDiff);
                    entry.getValue().rotateAroundAxis(Vectors.getRightSideNormalisedVector(player), -pitchDiff);
                    entry.getKey().moveTo(player.getLocation().add(entry.getValue()));
                    entry.getKey().getBlockDisplay().setRotation(0, 0);
                }
                previousDir = newDir;
            } else{
                this.remove();
            }
        }
        else if (abilityStatus == AbilityStatus.SHOT){
            for (Map.Entry<TempDisplayBlock, Vector> entry : displayBlocks.entrySet()) {
                TempDisplayBlock tempDisplayBlock = entry.getKey();
                Location currentLoc = tempDisplayBlock.getBlockDisplay().getLocation();
                //entry.getValue().add(player.getEyeLocation().getDirection().add(Vector.getRandom().multiply(0.1)).multiply(0.1)).normalize();
                Entity target = Entities.getAffected(currentLoc, hitbox, player);
                if (target instanceof LivingEntity livingEntity){
                    DamageHandler.damageEntity(livingEntity, player, this, damage);
                    this.remove();
                }

                entry.getKey().moveTo(currentLoc.add(entry.getValue().clone().multiply(speed)));

                if (origin.distance(currentLoc) > range) {
                    this.remove();
                }
            }
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
        sourceTempBlocks.forEach(tempBlock -> tempBlock.revert());
        displayBlocks.forEach((tempDisplayBlock, vector) -> tempDisplayBlock.revert());
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
