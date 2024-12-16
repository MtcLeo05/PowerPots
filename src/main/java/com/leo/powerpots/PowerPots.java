package com.leo.powerpots;

import com.leo.powerpots.config.Config;
import com.leo.powerpots.init.ModBlockEntities;
import com.leo.powerpots.init.ModBlocks;
import com.leo.powerpots.init.ModCreativeTabs;
import com.leo.powerpots.init.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PowerPots.MODID)
public class PowerPots {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "powerpots";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();


    public PowerPots() {
        Config.initialize();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modEventBus);

        ModBlocks.register(modEventBus);

        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
    }
}
