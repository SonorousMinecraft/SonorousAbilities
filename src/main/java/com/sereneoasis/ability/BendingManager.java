package com.sereneoasis.ability;

import com.sereneoasis.SerenityPlayer;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.TempBlock;

import java.util.Map;
import java.util.UUID;

public class BendingManager implements Runnable{

    private static BendingManager instance;

    private long time;

    public BendingManager()
    {
        instance = this;

    }
    @Override
    public void run() {

        CoreAbility.progressAll();
        this.handleCooldowns();

        while (!TempBlock.getRevertQueue().isEmpty())
        {
            final TempBlock tempBlock = TempBlock.getRevertQueue().peek(); //Check if the top TempBlock is ready for reverting
            if (tempBlock.getRevertTime() < System.currentTimeMillis()) {
                tempBlock.revertBlock();
            }
            else{
                break;
            }
        }
    }

    public void handleCooldowns() {
        for (Map.Entry<UUID, SerenityPlayer> entry : SerenityPlayer.getSerenityPlayerMap().entrySet()) {
            SerenityPlayer sPlayer = entry.getValue();

            sPlayer.removeOldCooldowns();
        }
    }
}
