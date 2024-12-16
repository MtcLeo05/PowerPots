package com.leo.powerpots.init;

import com.leo.powerpots.PowerPots;
import com.leo.powerpots.block.PotTier;
import com.leo.powerpots.block.PowerPotBlock;
import com.leo.powerpots.config.Config;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PowerPots.MODID);

    public static List<RegistryObject<Block>> POWER_BLOCKS = new ArrayList<>();

    public static Block[] getBlocks() {
        return POWER_BLOCKS.stream().map(RegistryObject::get).toArray(Block[]::new);
    }

    public static void register(IEventBus bus) {
        for (PotTier tier : Config.INSTANCE.TIERS) {
            if(tier.index() == 0) continue;
            POWER_BLOCKS.add(
                registerBlock("power_pot_" + tier.index(), () ->
                    new PowerPotBlock(tier)
                )
            );
        }

        BLOCKS.register(bus);
    }

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
