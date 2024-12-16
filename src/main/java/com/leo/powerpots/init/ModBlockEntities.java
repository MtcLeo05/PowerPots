package com.leo.powerpots.init;

import com.leo.powerpots.PowerPots;
import com.leo.powerpots.block.entity.PowerPotBE;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PowerPots.MODID);


    public static RegistryObject<BlockEntityType<PowerPotBE>> POWER_POT_BE = BLOCK_ENTITIES.register("power_pot_be",
        () -> BlockEntityType.Builder.of(
            PowerPotBE::new,
            ModBlocks.getBlocks()
        ).build(null)
    );

}
