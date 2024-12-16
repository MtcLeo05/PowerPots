package com.leo.powerpots.block;

import com.leo.powerpots.PowerPots;
import com.leo.powerpots.block.entity.PowerPotBE;
import com.leo.powerpots.config.Config;
import com.leo.powerpots.init.ModBlockEntities;
import net.darkhax.botanypots.block.BlockBotanyPot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PowerPotBlock extends BlockBotanyPot {

    PotTier tier;

    public PowerPotBlock(PotTier tier) {
        super(true);
        this.tier = tier;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return blockEntity(pPos, pState, null, tier);
    }

    public PowerPotBE blockEntity(BlockPos pos, BlockState state, @Nullable BlockGetter level, PotTier tier) {
        if(level == null) return new PowerPotBE(pos, state, tier);
        if(level.getBlockEntity(pos) instanceof PowerPotBE be) return be;

        return new PowerPotBE(pos, state, tier);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        String path = ForgeRegistries.ITEMS.getKey(pStack.getItem()).getPath();
        char c = path.charAt(path.length() - 1);
        int i = Integer.parseInt(String.valueOf(c));
        PotTier tier = Config.INSTANCE.TIERS.get(i - 1);

        pTooltip.add(Component.translatable("tooltip." + PowerPots.MODID + ".energy", tier.powerEachTick()));
        pTooltip.add(Component.translatable("tooltip." + PowerPots.MODID + ".speed", tier.speedModifier()));
        pTooltip.add(Component.translatable("tooltip." + PowerPots.MODID + ".item", tier.itemAmountMultiplier()));
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.POWER_POT_BE.get(), PowerPotBE::tickPot);
    }
}
