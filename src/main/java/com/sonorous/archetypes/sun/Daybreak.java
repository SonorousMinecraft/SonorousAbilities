package com.sonorous.archetypes.sun;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.abilityuilities.velocity.Jet;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.enhancedmethods.EnhancedBlocksArchetypeLess;
import com.sonorous.util.methods.AbilityDamage;
import com.sonorous.util.methods.ArchetypeVisuals;
import com.sonorous.util.methods.Locations;
import com.sonorous.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Daybreak extends CoreAbility {

    private final static String name = "Daybreak";


    private final HashMap<Integer, TempDisplayBlock> trail = new HashMap<>();

    private Jet jet;

    private final ArchetypeVisuals.SunVisual sunVisual = new ArchetypeVisuals.SunVisual();

    public Daybreak(Player player) {
        super(player, name);

        if (player.isSneaking() && shouldStart()) {
            jet = new Jet(player, name);
            start();
        }


    }

    @Override
    public void progress() {

        if (!player.isSneaking() | jet.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
        }
        for (Location loc : Locations.getSeveralHelixes(player.getEyeLocation(), player.getEyeLocation().getDirection().multiply(-1), 5, 20, 5, 0, true, 3)) {
            sunVisual.playVisual(loc, size, 0, 1, 1, 1);
        }

        AbilityDamage.damageSeveral(player.getEyeLocation(), this, player, true, player.getVelocity());

//        for (Entity e : Entities.getEntitiesAroundPoint(player.getEyeLocation(), hitbox)) {
//            if (e != null && e instanceof Player targetPlayer && e.getUniqueId() != player.getUniqueId()) {
//                DamageHandler.damageEntity(targetPlayer, player, this, damage);
//                Entities.applyPotion(targetPlayer, PotionEffectType.BLINDNESS, Math.round(startTime - duration));
//            }
//        }

        Location facing = Locations.getFacingLocation(player.getEyeLocation(), player.getEyeLocation().getDirection(), speed);

        if (!EnhancedBlocksArchetypeLess.getFacingSphereBlocks(this, facing).isEmpty()) {
//            new BlockExplodeSphere(player, name, facing, 1);

            SunUtils.blockExplode(player, name, facing, radius * 1.5, 1);
        }

    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
        for (TempDisplayBlock tb : trail.values()) {
            tb.revert();
        }
        jet.remove();

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