package com.sereneoasis.archetypes.chaos;

import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.abilityuilities.velocity.Teleport;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ShadowStep extends MasterAbility {

    private static final String name = "ShadowStep";

    private int shots = 5;
    public ShadowStep(Player player) {
        super(player, name);

        if (shouldStart()){
            abilityStatus = AbilityStatus.TELEPORTING;
            setHasClicked();
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {

        Bukkit.broadcastMessage("currently at " + shots + " shots");


        iterateHelpers(abilityStatus);

        if (shots == 0 && getHelpers().keySet().stream().noneMatch(coreAbility -> coreAbility.getAbilityStatus() == AbilityStatus.TELEPORTING)){
            Bukkit.broadcastMessage("removed");
            this.remove();
        }
        else {
            if (player.isSneaking()) {
                abilityStatus = AbilityStatus.REVERTING;
            } else {
                abilityStatus = AbilityStatus.TELEPORTING;
            }
        }
    }

    public void setHasClicked(){
        if (shots > 0) {
            Teleport teleport = new Teleport(player, name, range);
            getHelpers().put(teleport, (abilityStatus) -> {
                switch (abilityStatus) {
                    case REVERTING -> {
                        if (teleport.getAbilityStatus() == AbilityStatus.COMPLETE) {
                            player.teleport(teleport.getOrigin());
                            teleport.setAbilityStatus(AbilityStatus.REVERTING);
                        }
                    }
                }
            });
            shots--;
        }

    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
    }

    @Override
    public String getName() {
        return name;
    }
}
