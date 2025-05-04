package com.sonorous.archetypes.earth;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.abilityuilities.blocks.RaiseBlockCircle;
import com.sonorous.abilityuilities.velocity.Jump;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.enhancedmethods.EnhancedBlocks;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Catapult extends CoreAbility {

    private final String name = "Catapult";

    private Jump jump;


    private List<RaiseBlockCircle> quakes = new ArrayList<>();

    private double currentRadius = 0.5;

    private boolean hasCooldownApplied = false;

    private Location loc;

    public Catapult(Player player) {
        super(player, "Catapult");

        if (shouldStart() && EnhancedBlocks.isStandingOnSource(this)) {
            jump = new Jump(player, name, true);
            start();
        }


    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (jump.getAbilityStatus() == AbilityStatus.COMPLETE) {

            if (!hasCooldownApplied) {
                sPlayer.addCooldown(name, cooldown);
                this.loc = player.getLocation();
                hasCooldownApplied = true;
            }

            if (currentRadius > radius) {
                abilityStatus = AbilityStatus.COMPLETE;
                this.remove();
            }
            currentRadius += size;
            RaiseBlockCircle shockwaveRing = new RaiseBlockCircle(player, name, loc, 1, currentRadius, true);
            quakes.add(shockwaveRing);

        }


    }


    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
        jump.remove();
        for (RaiseBlockCircle shockwaveRing : quakes) {
            if (shockwaveRing.getAbilityStatus() == AbilityStatus.COMPLETE) {
                shockwaveRing.remove();
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}