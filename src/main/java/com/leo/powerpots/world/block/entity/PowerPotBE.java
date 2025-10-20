package com.leo.powerpots.world.block.entity;

import com.leo.powerpots.PowerPots;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.botanypots.common.impl.block.entity.BotanyPotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PowerPotBE extends BotanyPotBlockEntity {
    public PowerPotBE(BlockPos pos, BlockState state) {
        super(getBEType(), pos, state);
    }

    public static void tickPot(Level level, BlockPos pos, BlockState state, PowerPotBE pot) {
        BotanyPotBlockEntity.tickPot(level, pos, state, pot);
    }

    public static CachedSupplier<BlockEntityType<BotanyPotBlockEntity>> getBEType() {
        return CachedSupplier.of(BuiltInRegistries.BLOCK_ENTITY_TYPE, PowerPots.modLoc("power_pot_be")).cast();
    }
}