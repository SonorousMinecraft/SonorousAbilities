package com.sonorous.archetypes.sky;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.abilityuilities.velocity.Jump;
import com.sonorous.abilityuilities.velocity.Levitate;
import com.sonorous.archetypes.DisplayBlock;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.enhancedmethods.EnhancedBlocksArchetypeLess;
import com.sonorous.util.methods.ArchetypeVisuals;
import com.sonorous.util.temp.TempBlock;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CloudStep extends CoreAbility {

    private static final String name = "CloudStep";
    private Jump jump;
    private Levitate levitate;
    private state CloudStepState = state.LEVITATE;

    public CloudStep(Player player) {
        super(player, name);

        if (shouldStart()) {
            this.levitate = new Levitate(player, name);

            start();
        }


    }

    @Override
    public void progress() {
        if (levitate.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
        }

        if (abilityStatus == AbilityStatus.JUMPING) {
            Location loc = player.getLocation().subtract(0, radius, 0);

            new ArchetypeVisuals.AirVisual().playVisual(loc, size, radius, 10, 5, 1);

            if (jump != null && jump.getAbilityStatus() == AbilityStatus.COMPLETE) {


                abilityStatus = AbilityStatus.FLOATING;
            }
        } else if (abilityStatus == AbilityStatus.MOVING) {

            Vector dir = player.getEyeLocation().getDirection().setY(0).normalize().multiply(radius);
//            if (dir.getY() > 1) {
//                dir.setY(1);
//                player.setVelocity(new Vector(0,1,0));
//            }
            Location floorLoc = player.getLocation().subtract(0, 1, 0).add(dir);

            if (sPlayer.getHeldAbility().equals(name)) {
                EnhancedBlocksArchetypeLess.getCircleAtYBlocks(this, floorLoc, floorLoc.getBlockY())
                        .stream()
                        .forEach(block -> {
                            if (!TempBlock.isTempBlock(block)) {
                                new TempBlock(block, DisplayBlock.AIR, 2000);
                            }
                        });
            }
        }

    }

    public void setHasShifted() {
        if (jump == null || jump.getAbilityStatus() == AbilityStatus.COMPLETE) {
            jump = new Jump(player, name, true);
            abilityStatus = AbilityStatus.JUMPING;
        }
    }

    public void setHasClicked() {
//        Block block = Blocks.getFacingBlock(player, speed * 20);
        if (jump == null || jump.getAbilityStatus() == AbilityStatus.COMPLETE) {

//            if (block != null && DisplayBlock.AIR.getBlocks().contains(block.getType())) {
            if (levitate.isLevitating()) {
                abilityStatus = AbilityStatus.MOVING;

            } else {
                abilityStatus = AbilityStatus.FLOATING;
            }
            levitate.toggle();
        }

        if (player.isSneaking()) {
            this.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();
        if (jump != null) {
            jump.remove();
        }
        if (levitate != null) {
            levitate.remove();
        }
        sPlayer.addCooldown(name, cooldown);
    }


    @Override
    public String getName() {
        return name;
    }

    private enum state {
        LEVITATE,
        STEP
    }
}