package com.leo.powerpots.world.block;

import com.leo.powerpots.PowerPots;
import com.leo.powerpots.config.Config;
import com.leo.powerpots.init.ModBlockEntities;
import com.leo.powerpots.world.block.entity.PowerPotBE;
import net.darkhax.botanypots.common.api.context.BotanyPotContext;
import net.darkhax.botanypots.common.api.data.recipes.crop.Crop;
import net.darkhax.botanypots.common.api.data.recipes.soil.Soil;
import net.darkhax.botanypots.common.impl.block.BotanyPotBlock;
import net.darkhax.botanypots.common.impl.block.PotType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PowerPotBlock extends BotanyPotBlock {

    private final Config.PotTier tier;

    public PowerPotBlock(Config.PotTier tier) {
        super(MapColor.COLOR_GRAY, PotType.HOPPER);
        this.tier = tier;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return blockEntity(pPos, pState, null);
    }

    public PowerPotBE blockEntity(BlockPos pos, BlockState state, @Nullable BlockGetter level) {
        if(level == null) return new PowerPotBE(pos, state);
        if(level.getBlockEntity(pos) instanceof PowerPotBE be) return be;

        return new PowerPotBE(pos, state);
    }

    @Override
    public float getGrowthModifier(BotanyPotContext context, Level level, Crop crop, @Nullable Soil soil) {
        return this.tier.speedModifier();
    }

    @Override
    public float getYieldModifier(BotanyPotContext context, Level level, Crop crop, @Nullable Soil soil) {
        return this.tier.yieldModifier();
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> pTooltip, TooltipFlag tooltipFlag) {
        String path = BuiltInRegistries.ITEM.getKey(pStack.getItem()).getPath();
        char c = path.charAt(path.length() - 1);
        int i = Integer.parseInt(String.valueOf(c));
        Config.PotTier tier = Config.INSTANCE.TIERS.get(i - 1);

        pTooltip.add(Component.translatable("tooltip." + PowerPots.MODID + ".energy", tier.powerEachTick()));
        pTooltip.add(Component.translatable("tooltip." + PowerPots.MODID + ".speed", tier.speedModifier()));
        pTooltip.add(Component.translatable("tooltip." + PowerPots.MODID + ".item", tier.yieldModifier()));

        super.appendHoverText(pStack, context, pTooltip, tooltipFlag);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.POWER_POT_BE.get(), PowerPotBE::tickPot);
    }
}
