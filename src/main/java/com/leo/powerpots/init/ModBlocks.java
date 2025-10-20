package com.leo.powerpots.init;

import com.leo.powerpots.PowerPots;
import com.leo.powerpots.config.Config;
import com.leo.powerpots.world.block.PowerPotBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, PowerPots.MODID);

    public static List<DeferredHolder<Block, Block>> POWER_BLOCKS = new ArrayList<>();

    public static Block[] getBlocks() {
        return POWER_BLOCKS.stream().map(DeferredHolder::get).toArray(Block[]::new);
    }

    public static void register(IEventBus bus) {
        for (Config.PotTier tier : Config.INSTANCE.TIERS) {
            if(tier.index() == 0) continue;
            POWER_BLOCKS.add(
                registerBlock("power_pot_" + tier.index(), () ->
                    new PowerPotBlock(tier)
                )
            );
        }

        BLOCKS.register(bus);
    }

    public static <T extends Block>DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block) {
        DeferredHolder<Block, T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredHolder<Block, T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
