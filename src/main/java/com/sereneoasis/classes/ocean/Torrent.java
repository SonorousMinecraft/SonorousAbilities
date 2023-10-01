package com.sereneoasis.classes.ocean;

import com.sereneoasis.Methods;
import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.SourceStatus;
import com.sereneoasis.util.TempBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Torrent extends CoreAbility {

    private SourceStatus sourceStatus;
    private Location loc;
    private Vector dir;

    private TempBlock tb;
    public Torrent(Player player) {
        super(player);

        Bukkit.broadcastMessage("started");
        sourceStatus = SourceStatus.NO_SOURCE;
        Block source = Methods.getFacingBlockOrLiquid(player, range);
        if (source.getType().equals(Material.WATER))
        {
            Bukkit.broadcastMessage("selected source");
          sourceStatus = SourceStatus.SOURCING;
          loc = source.getLocation();
          start();
        }
    }

    @Override
    public void progress() {
        if (!player.isSneaking())
        {
            this.remove();
        }

        tb = new TempBlock(loc.getBlock(), Material.DIRT.createBlockData(), 5000);
        //loc.getBlock().setBlockData(Material.DIRT.createBlockData());
        //loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 5);

        if (sourceStatus == SourceStatus.SOURCING)
        {
            Bukkit.broadcastMessage("sourcing");
            dir = Methods.getDirectionBetweenLocations(loc, player.getEyeLocation());
            loc.add(dir.clone().multiply(speed));
            if (loc.distance(player.getLocation()) <= 3)
            {
                sourceStatus = SourceStatus.SOURCED;
                dir = Methods.getDirectionBetweenLocations(player.getEyeLocation(), loc);
            }
        }
        if (sourceStatus == SourceStatus.SOURCED)
        {
            Bukkit.broadcastMessage("sourcing");
            loc = player.getEyeLocation().add(dir.rotateAroundY(Math.toRadians(15)));
        }
        if (sourceStatus == SourceStatus.SHOT)
        {
            Bukkit.broadcastMessage("shot");
            dir = player.getEyeLocation().getDirection().normalize();
            loc.add(dir.clone().multiply(speed));
            DamageHandler.damageEntity(Methods.getAffected(loc, radius, player), player, this, damage);

            if (loc.distance(player.getEyeLocation()) > range)
            {
                this.remove();
            }
        }
    }

    public void setHasClicked()
    {
        if (sourceStatus == SourceStatus.SOURCED) {
            sourceStatus = SourceStatus.SHOT;
        }
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return "Torrent";
    }
}
