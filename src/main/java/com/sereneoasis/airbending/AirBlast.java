package com.sereneoasis.airbending;

import com.sereneoasis.CoreAbility;
import org.bukkit.entity.Player;

public class AirBlast extends CoreAbility {


    public AirBlast(Player player) {
        super(player);
        start();
    }

    @Override
    public void progress() {
        player.sendMessage("Airblast");
    }
}
