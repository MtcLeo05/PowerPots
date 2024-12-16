package com.leo.powerpots.event;

import com.leo.powerpots.PowerPots;
import com.leo.powerpots.init.ModBlockEntities;
import net.darkhax.botanypots.block.BotanyPotRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PowerPots.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBusClientEvent {

    @SubscribeEvent
    public static void registerBERs(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.POWER_POT_BE.get(), BotanyPotRenderer::new);
    }

}
