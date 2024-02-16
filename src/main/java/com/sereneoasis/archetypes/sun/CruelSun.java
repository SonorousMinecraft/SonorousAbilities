package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.BlockSmash;
import com.sereneoasis.abilityuilities.particles.ChargeSphere;
import com.sereneoasis.abilityuilities.particles.SphereBlast;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import com.sereneoasis.util.methods.Locations;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

/**
 * @author Sakrajin
 */
public class CruelSun extends CoreAbility {

    private ChargeSphere chargeSphere;
    private BlockSmash blockSmash;

    private final String name = "CruelSun";

    public CruelSun(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        chargeSphere = new ChargeSphere(player, name, 0, new ArchetypeVisuals.SunVisual());
        abilityStatus = AbilityStatus.CHARGING;
        start();

    }

    @Override
    public void progress() {
        if (abilityStatus == AbilityStatus.CHARGING) {
            if (!player.isSneaking()) {
                this.remove();
            }
            if (chargeSphere.getAbilityStatus() == AbilityStatus.CHARGED) {
                abilityStatus = AbilityStatus.CHARGED;
                chargeSphere.remove();
            }
        }
        else if (abilityStatus == AbilityStatus.CHARGED) {
            blockSmash = new BlockSmash(player, name,
                    DisplayBlock.SUN,
                    Locations.getFacingLocation(player.getEyeLocation(), player.getEyeLocation().getDirection(), radius/2));
            abilityStatus = AbilityStatus.NOT_SHOT;
        }
        else if (abilityStatus == AbilityStatus.SHOT){
            if (blockSmash.getAbilityStatus() == AbilityStatus.COMPLETE) {
                this.remove();
            }
        }


    }

    public void setHasClicked() {
        blockSmash.setHasClicked();
        abilityStatus = AbilityStatus.SHOT;
    }

    @Override
    public void remove() {
        super.remove();
        if (chargeSphere != null) {
            chargeSphere.remove();
        }
        if (blockSmash != null) {
            blockSmash.remove();
        }
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
