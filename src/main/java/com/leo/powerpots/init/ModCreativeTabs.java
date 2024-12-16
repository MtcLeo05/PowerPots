package com.leo.powerpots.init;

import com.leo.powerpots.PowerPots;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PowerPots.MODID);

    public static final RegistryObject<CreativeModeTab> ITEMS = CREATIVE_MODE_TABS.register("items", () ->
        CreativeModeTab.builder()
            .title(Component.translatable(PowerPots.MODID + ".itemGroup.items"))
            .icon(Items.STICK::getDefaultInstance)
            .displayItems((idp, output) -> {
                Iterable<Item> items = ModItems.ITEMS.getEntries().stream().map(RegistryObject::get)::iterator;

                items.forEach(output::accept);

                Iterable<Block> blocks = ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;

                blocks.forEach(output::accept);
            })
            .build()
    );
}
