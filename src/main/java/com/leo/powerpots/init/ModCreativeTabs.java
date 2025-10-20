package com.leo.powerpots.init;

import com.leo.powerpots.PowerPots;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PowerPots.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEMS = CREATIVE_MODE_TABS.register("items", () ->
        CreativeModeTab.builder()
            .title(Component.translatable(PowerPots.MODID + ".itemGroup.items"))
            .icon(Items.STICK::getDefaultInstance)
            .displayItems((idp, output) -> {
                List<? extends Item> items = ModItems.ITEMS.getEntries().stream().map(DeferredHolder::get).toList();

                items.forEach(output::accept);

                List<? extends Block> blocks = ModBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).toList();

                blocks.forEach(output::accept);
            })
            .build()
    );
}
