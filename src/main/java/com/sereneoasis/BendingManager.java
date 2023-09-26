package com.sereneoasis;

import java.util.Map;
import java.util.UUID;

public class BendingManager implements Runnable{

    private static BendingManager instance;

    public BendingManager()
    {
        instance = this;

    }
    @Override
    public void run() {
        CoreAbility.progressAll();
        this.handleCooldowns();
    }

    public void handleCooldowns() {
        for (Map.Entry<UUID, SerenityPlayer> entry : SerenityPlayer.getSerenityPlayerMap().entrySet()) {
            SerenityPlayer sPlayer = entry.getValue();

            sPlayer.removeOldCooldowns();
        }
    }
}
