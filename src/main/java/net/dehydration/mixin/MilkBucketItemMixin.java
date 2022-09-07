package net.dehydration.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.dehydration.access.ThirstManagerAccess;
import net.dehydration.init.ConfigInit;
import net.dehydration.init.EffectInit;
import net.dehydration.misc.ThirstTooltipData;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.world.World;

@Mixin(MilkBucketItem.class)
public abstract class MilkBucketItemMixin extends Item {

    public MilkBucketItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "finishUsing", at = @At(value = "HEAD"))
    public void finishUsingMixin(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info) {
        if (user instanceof PlayerEntity player) {
            ((ThirstManagerAccess) player).getThirstManager().add(ConfigInit.CONFIG.milk_thirst_quench);
            if (!world.isClient && world.random.nextFloat() >= ConfigInit.CONFIG.milk_thirst_chance)
                player.addStatusEffect(new StatusEffectInstance(EffectInit.THIRST, ConfigInit.CONFIG.potion_bad_thirst_duration / 2, 0, false, false, true));
        }
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return Optional.of(new ThirstTooltipData(1, ConfigInit.CONFIG.milk_thirst_quench));
    }

}
