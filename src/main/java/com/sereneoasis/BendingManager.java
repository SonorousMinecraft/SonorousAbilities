package com.sereneoasis;

public class BendingManager implements Runnable{

    private static BendingManager instance;

    public BendingManager()
    {
        instance = this;

    }
    @Override
    public void run() {
        CoreAbility.progressAll();
    }
}
