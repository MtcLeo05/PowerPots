package com.leo.powerpots.world.block.entity;

import com.leo.powerpots.PowerPots;
import com.leo.powerpots.config.Config;
import com.leo.powerpots.energy.ModEnergyStorage;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.botanypots.common.impl.block.entity.BotanyPotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PowerPotBlockEntity extends BotanyPotBlockEntity {

    private Config.PotTier tier;
    private ModEnergyStorage energyStorage;

    public PowerPotBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, Config.PotTier.ZERO);
    }

    public PowerPotBlockEntity(BlockPos pos, BlockState state, Config.PotTier tier) {
        super(getBEType(), pos, state);
        this.tier = tier;
        energyStorage = new ModEnergyStorage(tier.powerStorage(), tier.powerStorage(), 0, 0);
    }

    public static void tickPot(Level level, BlockPos pos, BlockState state, PowerPotBlockEntity pot) {
        pot.sync();
        if(pot.getEnergyStorage().getEnergyStored() < pot.tier.powerEachTick()) return;

        if(!level.isClientSide) pot.getEnergyStorage().removeEnergy(pot.tier.powerEachTick());
        BotanyPotBlockEntity.tickPot(level, pos, state, pot);
    }

    public static CachedSupplier<BlockEntityType<BotanyPotBlockEntity>> getBEType() {
        return CachedSupplier.of(BuiltInRegistries.BLOCK_ENTITY_TYPE, PowerPots.modLoc("power_pot_be")).cast();
    }

    public ModEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag nbt = new CompoundTag();
        this.saveAdditional(nbt, registries);
        return nbt;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        tag.put("energy", energyStorage.serializeNBT(registries));
        tag.putInt("potTier", tier.index());

        super.saveAdditional(tag, registries);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        energyStorage.deserializeNBT(registries, tag.get("energy"));
        int index = tag.getInt("potTier");

        tier = Config.INSTANCE.TIERS.get(Math.max(index - 1, 0));
        energyStorage = new ModEnergyStorage(tier.powerStorage(), tier.powerStorage(), 0, energyStorage.getEnergyStored());

        super.loadAdditional(tag, registries);
    }

    public void sync() {
        if (this.level == null) return;

        setChanged();

        for (Player player : this.level.players()) {
            if (player instanceof ServerPlayer) {
                BlockPos pos = this.getBlockPos();
                if (player.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64 && !this.isRemoved() && level.getBlockEntity(pos) == this) {
                    ((ServerPlayer) player).connection.send(this.getUpdatePacket());
                }
            }
        }
        this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }
}