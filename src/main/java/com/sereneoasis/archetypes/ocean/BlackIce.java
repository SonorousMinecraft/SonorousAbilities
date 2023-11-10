package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.temp.TempBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BlackIce extends CoreAbility {

    private final String name = "BlackIce";

    private GlacierBreath glacierBreath;

    private long starttime;

    public BlackIce(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        if (CoreAbility.hasAbility(player, GlacierBreath.class))
        {
            glacierBreath = CoreAbility.getAbility(player,GlacierBreath.class);
            if (glacierBreath.getAbilityStatus() == AbilityStatus.CHARGED)
            {
                if (Blocks.getFacingBlock(player, sourceRange) != null && DisplayBlock.ICE.getBlocks().contains(Blocks.getFacingBlock(player, sourceRange).getType())) {
                    for (Block b : Blocks.getBlocksAroundPoint(player.getLocation(), radius, Material.WATER ))
                    {
                        if (b.getLocation().clone().add(0,1,0).getBlock().getType() == Material.AIR)
                        {
                            new TempBlock(b, DisplayBlock.ICE, duration, true);
                        }
                    }
                    glacierBreath.setBlackIce();
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    starttime = System.currentTimeMillis();
                    start();
                }
            }
        }




    }

    @Override
    public void progress() {
        if (System.currentTimeMillis() > starttime + duration)
        {
            this.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();
        glacierBreath.remove();
        player.setFlying(false);
        player.setAllowFlight(false);
        sPlayer.addCooldown(name, cooldown);
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
