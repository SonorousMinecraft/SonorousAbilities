package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Tsunami extends CoreAbility {

    private final String name = "Tsunami";

    private int maxHeightFromGround = 5;

    private Block floorBlock;

    private int heightFromGround;
    private HashMap<Integer, TempDisplayBlock> wave;

    private ArmorStand armorStand;

    private long startTime;

    public Tsunami(Player player) {
        super(player);
        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(name))
        {
            return;
        }

        setFloorBlock();
        if (floorBlock == null)
        {
            return;
        }

        wave = new HashMap<>();

        Location loc = player.getLocation();

        armorStand = (ArmorStand) loc.getWorld().spawn(loc, EntityType.ARMOR_STAND.getEntityClass(), ( (entity) ->
        {
            ArmorStand aStand = (ArmorStand) entity;
            aStand.setInvulnerable(true);
            aStand.setVisible(false);
        }));
        Entities.applyPotion(armorStand, PotionEffectType.INVISIBILITY, Math.round(duration));

        armorStand.addPassenger(player);

        this.startTime = System.currentTimeMillis();
        start();
    }

    private void setFloorBlock()
    {
        floorBlock = null;
        for (int i = 0; i <= this.maxHeightFromGround; i++) {
            final Block block = this.player.getEyeLocation().getBlock().getRelative(BlockFace.DOWN, i);
            if (block.getType() == Material.WATER) {
                this.floorBlock = block;
                heightFromGround = maxHeightFromGround;
                return;
            }
        }
    }

    @Override
    public void progress() {

        setFloorBlock();
        if (player.isSneaking() || floorBlock == null || System.currentTimeMillis() > startTime+duration)
        {
            this.remove();
            return;
        }

        Vector dir = player.getEyeLocation().getDirection().setY(0).normalize();
        armorStand.setVelocity(dir.clone().multiply(speed));

        Location waveLoc = floorBlock.getLocation().clone().add(0,1,0);
        Set<Location> waveLocs = new HashSet<>();
        for (double i = 0; i < radius; i +=0.5) {
            waveLocs.addAll(Locations.getPerpArcFromVector(waveLoc.clone().add(0,i,0), dir, i, 135, 225, 10));
        }
        wave = Entities.handleDisplayBlockEntities(wave,
                waveLocs,
                Material.BLUE_STAINED_GLASS, 0.5);
    }

    @Override
    public void remove()
    {
        super.remove();
        sPlayer.addCooldown(name,cooldown);
        for (TempDisplayBlock tb : wave.values())
        {
            tb.revert();
        }
        armorStand.removePassenger(player);
        armorStand.remove();
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
