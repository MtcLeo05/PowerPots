package com.leo.powerpots.event;

import com.leo.powerpots.PowerPots;
import com.leo.powerpots.init.ModBlockEntities;
import net.minecraft.core.Direction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

@EventBusSubscriber(modid = PowerPots.MODID)
public class ModBusEvents {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.EnergyStorage.BLOCK,
            ModBlockEntities.POWER_POT_BE.get(),
            ((o, direction) -> o.getEnergyStorage())
        );

        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            ModBlockEntities.POWER_POT_BE.get(),
            (be, side) -> side == Direction.DOWN ? new SidedInvWrapper(be, Direction.DOWN) : null
        );
    }

}
