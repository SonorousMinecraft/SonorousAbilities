package com.sereneoasis.abilityuilities.velocity;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.data.ArchetypeDataManager;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Entities;
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

    private Block floorBlock;


    private boolean any;


    public Skate(Player player, String user, int maxHeightFromGround, boolean anyFloor) {
        super(player, user);

        this.user = user;
        this.maxHeightFromGround = maxHeightFromGround;

        this.any = anyFloor;

        Location loc = player.getLocation();
        setFloorBlock();

        abilityStatus = AbilityStatus.NO_SOURCE;

        if (floorBlock != null) {
            armorStand = (ArmorStand) loc.getWorld().spawn(loc, EntityType.ARMOR_STAND.getEntityClass(), ((entity) ->
            {
                ArmorStand aStand = (ArmorStand) entity;
                aStand.setInvulnerable(true);
                aStand.setVisible(false);
                aStand.setSmall(true);
            }));
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
            if (ArchetypeDataManager.getArchetypeData(sPlayer.getArchetype()).getBlocks().contains(block.getType()) || any) {
                this.floorBlock = block;
                return;
            }
        }
    }

    @Override
    public void progress() {

        if (floorBlock == null || player.isSneaking()) {
            abilityStatus = AbilityStatus.COMPLETE;
            return;
        }

        Vector dir = player.getEyeLocation().getDirection().setY(0).normalize();
        armorStand.setVelocity(dir.clone().multiply(speed));

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
