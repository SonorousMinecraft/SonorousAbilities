package com.sereneoasis.abilityuilities.velocity;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.data.ArchetypeDataManager;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Display;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Vectors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Skate extends CoreAbility {

    private String user;
    private ArmorStand armorStand;

    private int maxHeightFromGround;

    private int preferredHeightFromGround;

    private Block floorBlock;

    private boolean floorRegisteredAsNull = false;

    private long sinceLastNullFloor;

    private boolean any;


    public Skate(Player player, String user, int maxHeightFromGround, int preferredHeightFromGround, boolean anyFloor) {
        super(player, user);

        this.user = user;
        this.preferredHeightFromGround = preferredHeightFromGround;
        this.maxHeightFromGround = maxHeightFromGround;

        this.any = anyFloor;

        Location loc = player.getLocation();
        setFloorBlock();

        abilityStatus = AbilityStatus.NO_SOURCE;

        if (floorBlock != null) {
            armorStand = Display.createArmorStand(loc);
          /*  armorStand = (ArmorStand) loc.getWorld().spawn(loc, EntityType.ARMOR_STAND.getEntityClass(), ((entity) ->
            {
                ArmorStand aStand = (ArmorStand) entity;
                aStand.setInvulnerable(true);
                aStand.setVisible(false);
                aStand.setSmall(true);
            }));*/
            Entities.applyPotion(armorStand, PotionEffectType.INVISIBILITY, Math.round(duration));

            armorStand.addPassenger(player);

            abilityStatus = AbilityStatus.MOVING;
            start();
        }
    }

    private void setFloorBlock() {
        floorBlock = null;
        for (int i = 0; i <= this.maxHeightFromGround; i++) {
            final Block block = this.player.getEyeLocation().getBlock().getRelative(BlockFace.DOWN, i);
            if (ArchetypeDataManager.getArchetypeData(sPlayer.getArchetype()).getBlocks().contains(block.getType()) || any && block.getType() != Material.AIR) {
                this.floorBlock = block;
                return;
            }
        }
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    @Override
    public void progress() {

        if (floorBlock == null) {
            if (!floorRegisteredAsNull) {
                sinceLastNullFloor = System.currentTimeMillis();
                floorRegisteredAsNull = true;
            } else {
                if (System.currentTimeMillis() - sinceLastNullFloor > 1000) {
                    abilityStatus = AbilityStatus.COMPLETE;
                }
            }
        } else {
            floorRegisteredAsNull = false;
        }

        if (player.isSneaking()) {
            abilityStatus = AbilityStatus.COMPLETE;
            return;
        }


//        Vector dir = player.getEyeLocation().getDirection().setY(Vectors.getDirectionBetweenLocations(armorStand.getLocation(), floorBlock.getLocation().add(0,preferredHeightFromGround,0)).getY()/10).normalize();
//        armorStand.setVelocity(dir.clone().multiply(speed ));
//        armorStand.setVelocity(armorStand.getVelocity().setY(Vectors.getDirectionBetweenLocations(armorStand.getLocation(), floorBlock.getLocation().add(0,preferredHeightFromGround,0)).getY()/10));

        Vector dir = player.getEyeLocation().getDirection().setY(0).normalize();

        if (!floorRegisteredAsNull) {
            double heightDifference = Vectors.getDirectionBetweenLocations(armorStand.getLocation(), floorBlock.getLocation().add(0, preferredHeightFromGround, 0)).getY();
            double yVelocity = Math.log(Math.abs(heightDifference) + 1);
            if (heightDifference < 0) {
                armorStand.setVelocity(dir.clone().multiply(speed).add(new Vector(0, -yVelocity, 0)));
            } else {
                if (Blocks.isSolid(armorStand.getLocation().add(dir.clone().multiply(speed).add(new Vector(0, yVelocity + 1, 0))))) {
                    abilityStatus = AbilityStatus.COMPLETE;
                }

                armorStand.setVelocity(dir.clone().multiply(speed).add(new Vector(0, yVelocity, 0)));
            }
        } else {
            armorStand.setVelocity(dir.clone().multiply(speed));
            if (Blocks.isSolid(armorStand.getLocation().add(dir.clone().multiply(speed).add(new Vector(0, 1, 0))))) {
                abilityStatus = AbilityStatus.COMPLETE;
            }
        }

        setFloorBlock();
    }

    public Block getFloorBlock() {
        return this.floorBlock;
    }

    @Override
    public void remove() {
        super.remove();
        armorStand.removePassenger(player);
        armorStand.remove();
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return user;
    }
}
