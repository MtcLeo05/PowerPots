package com.leo.powerpots;

import com.leo.powerpots.config.Config;
import com.leo.powerpots.init.ModBlockEntities;
import com.leo.powerpots.init.ModBlocks;
import com.leo.powerpots.init.ModCreativeTabs;
import com.leo.powerpots.init.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(PowerPots.MODID)
public class PowerPots {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "powerpots";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public PowerPots(IEventBus modEventBus, ModContainer modContainer) {
        Config.initialize();

        ModItems.ITEMS.register(modEventBus);

        ModBlocks.register(modEventBus);

        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
    }

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
