package com.sereneoasis.archetypes.war;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.methods.Display;
import com.sereneoasis.util.methods.Locations;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

public class Crossbow extends CoreAbility {

    private final String name = "Crossbow";

    private ItemDisplay crossbow;

    public Crossbow(Player player) {
        super(player);

        crossbow = Display.createItemDisplayNoTransform(Locations.getMainHandLocation(player), Material.CROSSBOW, size,
                ItemDisplay.ItemDisplayTransform.FIRSTPERSON_RIGHTHAND, org.bukkit.entity.Display.Billboard.CENTER);
        start();
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (System.currentTimeMillis() > startTime + duration){
            this.remove();
        }

        crossbow.teleport(Locations.getMainHandLocation(player));
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
        crossbow.remove();
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
