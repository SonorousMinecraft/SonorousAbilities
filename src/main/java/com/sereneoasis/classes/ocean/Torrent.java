package com.sereneoasis.classes.ocean;

import com.sereneoasis.Methods;
import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.abilityuilities.RingAroundPlayer;
import com.sereneoasis.abilityuilities.SourceToPlayer;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.SourceStatus;
import com.sereneoasis.util.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Torrent extends CoreAbility {

    private SourceToPlayer sourceToPlayer;

    private RingAroundPlayer ringAroundPlayer;

    private boolean hasSourced;

    private boolean hasShot;
    private Location loc;
    private Vector dir;
    private TempBlock tb;

    public Torrent(Player player) {
        super(player);

        sourceToPlayer = new SourceToPlayer(player, this, Material.WATER, 4);
        if (! (sourceToPlayer.getSourceStatus() == SourceStatus.NO_SOURCE))
        {
            start();
        }
    }

    @Override
    public void progress() {
        if (!player.isSneaking())
        {
            this.remove();
        }

        if (sourceToPlayer.getSourceStatus() == SourceStatus.SOURCED)
        {
            hasSourced = true;
            sourceToPlayer.remove();
        }

        if (hasSourced) {
            if (hasShot) {
                loc = ringAroundPlayer.getLoc();
                ringAroundPlayer.remove();
                tb = new TempBlock(loc.getBlock(), Material.WATER.createBlockData(), 1000);
                dir = player.getEyeLocation().getDirection().normalize();
                loc.add(dir.clone().multiply(speed));
                DamageHandler.damageEntity(Methods.getAffected(loc, radius, player), player, this, damage);

                if (loc.distance(player.getEyeLocation()) > range) {
                    this.remove();
                }
            }
            else{
                if (ringAroundPlayer == null)
                {
                    ringAroundPlayer = new RingAroundPlayer(player, this, loc, Material.WATER, 3);
                }
            }
        }


    }

    public void setHasClicked()
    {
        if (hasSourced)
        {
            hasShot = true;
        }
    }

    @Override
    public void remove() {
        super.remove();
        ringAroundPlayer.remove();
        sourceToPlayer.remove();
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
