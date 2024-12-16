package com.leo.powerpots.block.entity;

import com.leo.powerpots.block.PotTier;
import com.leo.powerpots.config.Config;
import com.leo.powerpots.energy.ModEnergyStorage;
import com.leo.powerpots.init.ModBlockEntities;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.inventory.ContainerInventoryAccess;
import net.darkhax.bookshelf.api.inventory.IInventoryAccess;
import net.darkhax.botanypots.BotanyPotHelper;
import net.darkhax.botanypots.Constants;
import net.darkhax.botanypots.block.BlockEntityBotanyPot;
import net.darkhax.botanypots.block.inv.BotanyPotContainer;
import net.darkhax.botanypots.data.recipes.crop.Crop;
import net.darkhax.botanypots.data.recipes.soil.Soil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;


public class PowerPotBE extends BlockEntityBotanyPot {
    final Random rng = new Random();
    private long rngSeed;

    public ModEnergyStorage energyHandler;
    private LazyOptional<ModEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    private PotTier tier;

    public PowerPotBE(BlockPos pPos, BlockState pBlockState, PotTier tier) {
        super(ModBlockEntities.POWER_POT_BE.get(), pPos, pBlockState);
        energyHandler = new ModEnergyStorage(tier.powerStorage(), tier.powerStorage(), 0, 0);
        customRefreshRandom();
        this.tier = tier;
    }

    public PowerPotBE(BlockPos pPos, BlockState pState) {
        this(pPos, pState, PotTier.ZERO);
    }

    public static void tickPot(Level level, BlockPos pos, BlockState state, PowerPotBE pot) {
        if (pot.isRemoved() || pot.getLevel() == null) {
            return;
        }

        pot.getInventory().update();

        final Soil soil = pot.getSoil();
        final Crop crop = pot.getCrop();

        if (soil != null) {
            soil.onTick(level, pos, pot);
        }

        if (crop != null) {
            crop.onTick(level, pos, pot);
        }

        if (pot.isHopper()) {
            if (pot.exportDelay > 0) {
                pot.exportDelay--;
            }

            if (pot.harvestDelay > 0) {
                pot.harvestDelay--;
            }

            if (crop != null && pot.harvestDelay < 1 && pot.isCropHarvestable()) {
                if (pot.attemptAutoHarvest()) {
                    pot.resetGrowth();
                }

                pot.harvestDelay = 50;
            }

            if (pot.exportDelay < 1) {
                pot.attemptExport();

                pot.exportDelay = 10;
            }
        }

        // Growth Logic
        if (soil != null && crop != null && pot.areGrowthConditionsMet()) {
            if (!pot.doneGrowing) {
                pot.growthTime += pot.tier.speedModifier();
                pot.energyHandler.removeEnergy(pot.tier.powerEachTick());

                soil.onGrowthTick(level, pos, pot, crop);
                crop.onGrowthTick(level, pos, pot, soil);

                pot.prevComparatorLevel = pot.comparatorLevel;
                pot.comparatorLevel = Mth.floor(15f * ((float) pot.growthTime / pot.getInventory().getRequiredGrowthTime()));

                final boolean finishedGrowing = pot.growthTime >= pot.getInventory().getRequiredGrowthTime();

                if (pot.doneGrowing != finishedGrowing) {
                    pot.doneGrowing = finishedGrowing;
                    pot.markDirty();
                }
            }
        }

        else if (pot.growthTime != -1 || pot.doneGrowing || pot.comparatorLevel != 0) {
            pot.resetGrowth();
        }

        if (pot.comparatorLevel != pot.prevComparatorLevel) {
            pot.prevComparatorLevel = pot.comparatorLevel;
            pot.level.updateNeighbourForOutputSignal(pot.worldPosition, pot.getBlockState().getBlock());
        }
    }

    public void customRefreshRandom() {
        this.rngSeed = Constants.RANDOM.nextLong();
        this.rng.setSeed(rngSeed);
    }

    @Override
    public boolean attemptAutoHarvest() {
        if (this.getLevel() != null && !this.getLevel().isClientSide && this.getCrop() != null) {
            final ContainerInventoryAccess<BotanyPotContainer> inventory = new ContainerInventoryAccess<>(this.getInventory());

            this.rng.setSeed(this.rngSeed);
            final List<ItemStack> drops = BotanyPotHelper.generateDrop(rng, this.level, this.getBlockPos(), this, this.getCrop());

            if (drops.isEmpty()) {
                return true;
            }

            boolean didCollect = false;

            for (ItemStack drop : drops) {
                if (!drop.isEmpty()) {
                    drop.setCount(drop.getCount() * tier.itemAmountMultiplier());

                    final int originalSize = drop.getCount();

                    for (int slot : BotanyPotContainer.STORAGE_SLOT) {
                        if (drop.isEmpty()) {
                            break;
                        }

                        drop = inventory.insert(slot, drop, Direction.UP, true, true);
                    }

                    if (drop.getCount() != originalSize) {
                        didCollect = true;
                    }
                }
            }

            return didCollect;
        }

        return false;
    }

    private void attemptExport() {
        if (this.getLevel() == null || this.getLevel().isClientSide) {
            return;
        }

        final IInventoryAccess exportTo = Services.INVENTORY_HELPER.getInventory(this.getLevel(), this.getBlockPos().below(), Direction.UP);

        if (exportTo == null) {
            return;
        }

        for (int potSlotId : BotanyPotContainer.STORAGE_SLOT) {
            final ItemStack potStack = this.getInventory().getItem(potSlotId);

            if (!potStack.isEmpty()) {
                for (int exportSlotId : exportTo.getAvailableSlots()) {
                    if (exportTo.insert(exportSlotId, potStack, Direction.UP, false).getCount() != potStack.getCount()) {
                        this.getInventory().setItem(potSlotId, exportTo.insert(exportSlotId, potStack, Direction.UP, true));
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyEnergyHandler = LazyOptional.of(() -> energyHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyHandler.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public boolean areGrowthConditionsMet() {
        return super.areGrowthConditionsMet() && energyHandler.getEnergyStored() >= tier.powerEachTick();
    }

    @Override
    public void saveAdditional(CompoundTag pTag) {
        pTag.put("energy", energyHandler.serializeNBT());
        pTag.putInt("potTier", tier.index());

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        energyHandler.deserializeNBT(pTag.get("energy"));
        int index = pTag.getInt("potTier");

        tier = Config.INSTANCE.TIERS.get(index - 1);
        energyHandler = new ModEnergyStorage(tier.powerStorage(), tier.powerStorage(), 0, 0);
        super.load(pTag);
    }
}
