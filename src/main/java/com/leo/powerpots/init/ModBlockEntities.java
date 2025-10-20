package com.leo.powerpots.init;

import com.leo.powerpots.PowerPots;
import com.leo.powerpots.world.block.entity.PowerPotBE;
import net.darkhax.botanypots.common.impl.block.entity.BotanyPotBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, PowerPots.MODID);


    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<PowerPotBE>> POWER_POT_BE = BLOCK_ENTITIES.register("power_pot_be",
        () -> BlockEntityType.Builder.of(
            PowerPotBE::new,
            ModBlocks.getBlocks()
        ).build(null)
    );

}
