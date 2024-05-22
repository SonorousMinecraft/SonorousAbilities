package com.sereneoasis.archetypes.sky;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.particles.DirectionalStream;
import com.sereneoasis.abilityuilities.velocity.Jet;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import com.sereneoasis.util.methods.Vectors;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Nimbus extends CoreAbility {

    private static final String name = "Nimbus";

    private Jet jet;

    private DirectionalStream directionalStream;

    private Vector streamDir;

    public Nimbus(Player player) {
        super(player, name);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        jet = new Jet(player, name);
        streamDir = player.getEyeLocation().getDirection().multiply(-1);
        directionalStream = new DirectionalStream(player, name, Particle.ELECTRIC_SPARK, streamDir);
        start();
    }

    @Override
    public void progress() {
        streamDir = player.getVelocity().multiply(-1);
        directionalStream.setDir(streamDir);
//        directionalStream.getLocs().forEach(location -> {
//            Vector diff = Vectors.getDirectionBetweenLocations(player.getEyeLocation(), location);
//            location.subtract(diff);
//            diff.rotateAroundAxis(Vectors.getRightSideNormalisedVector(player), Math.toRadians(90));
//            location.add(diff);
//            location.add(0,3,0);
//        });
        if (jet.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();
        jet.remove();
        directionalStream.remove();
        sPlayer.addCooldown(name, cooldown);
    }

    public void setHasClicked(){
        if (player.isSneaking()) {
            this.remove();
        }
    }

    @Override
    public String getName() {
        return name;
    }
}