package com.leo.powerpots.event;

import com.leo.powerpots.PowerPots;
import com.leo.powerpots.init.ModBlockEntities;
import net.darkhax.botanypots.common.impl.block.BotanyPotRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = PowerPots.MODID, value = Dist.CLIENT)
public class ModBusClientEvents {

    @SubscribeEvent
    public static void registerBERs(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.POWER_POT_BE.get(), BotanyPotRenderer::new);
    }

}
