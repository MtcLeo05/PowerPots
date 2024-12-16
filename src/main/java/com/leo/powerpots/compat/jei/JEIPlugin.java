package com.leo.powerpots.compat.jei;

import com.leo.powerpots.PowerPots;
import com.leo.powerpots.init.ModBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(PowerPots.MODID, "jei_compat");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (Block block : ModBlocks.getBlocks()) {
            registration.addRecipeCatalyst(
                new ItemStack(block),
                net.darkhax.botanypots.addons.jei.JEIPlugin.CROP
            );
        }
    }
}
