package com.leo.powerpots.block;

import com.google.gson.annotations.Expose;

public final class PotTier {
    public static PotTier ZERO = new PotTier(0, 0, 0, 0, 0);

    @Expose
    private final int index;
    @Expose
    private final int powerStorage;
    @Expose
    private final int powerEachTick;
    @Expose
    private final int speedModifier;
    @Expose
    private final int itemAmountMultiplier;

    public PotTier(int index, int powerStorage, int powerEachTick, int speedModifier, int itemAmountMultiplier) {
        this.index = index;
        this.powerStorage = powerStorage;
        this.powerEachTick = powerEachTick;
        this.speedModifier = speedModifier;
        this.itemAmountMultiplier = itemAmountMultiplier;
    }

    public int index() {
        return index;
    }

    public int powerStorage() {
        return powerStorage;
    }

    public int powerEachTick() {
        return powerEachTick;
    }

    public int speedModifier() {
        return speedModifier;
    }

    public int itemAmountMultiplier() {
        return itemAmountMultiplier;
    }

}
