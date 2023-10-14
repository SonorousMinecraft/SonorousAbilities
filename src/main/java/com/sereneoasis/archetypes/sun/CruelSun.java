package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.particles.ChargeSphere;
import com.sereneoasis.abilityuilities.particles.SphereBlast;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

/**
 * @author Sakrajin
 *
 */
public class CruelSun extends CoreAbility {

    private ChargeSphere chargeSphere;
    private SphereBlast sphereBlast;
    private boolean hasCharged = false, hasShot = false;
    public CruelSun(Player player) {
        super(player);

        chargeSphere = new ChargeSphere(player, "CruelSun", 0, Particle.FLAME);
        start();

    }

    @Override
    public void progress() {
        if (!hasShot)
        {
            if (!player.isSneaking())
            {
                this.remove();
            }
            if (chargeSphere.getAbilityStatus() == AbilityStatus.CHARGED)
            {
                hasCharged = true;
            }
            if (hasCharged)
            {
                sphereBlast = new SphereBlast(player, "CruelSun", false, Particle.FLAME);
                hasShot = true;
            }
        }
        if (hasShot)
        {
            if (sphereBlast.getAbilityStatus() == AbilityStatus.COMPLETE)
            {
                this.remove();
            }
        }


    }

    @Override
    public void remove() {
        super.remove();
        if (chargeSphere != null)
        {
            chargeSphere.remove();
        }
        if (sphereBlast != null)
        {
            sphereBlast.remove();
        }
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return "CruelSun";
    }
}
