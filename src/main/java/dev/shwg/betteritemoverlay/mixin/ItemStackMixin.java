package dev.shwg.betteritemoverlay.mixin;

import dev.shwg.betteritemoverlay.BetterItemOverlay;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "isBarVisible", at = @At("RETURN"), cancellable = true)
    private void isBarVisible(CallbackInfoReturnable<Boolean> cir) {
        if(!BetterItemOverlay.CONFIG.modEnabledToggle.getValue()) return;
        ItemStack itemStack = (ItemStack)(Object)this;
        if (itemStack.isDamageableItem()) {
            switch (BetterItemOverlay.CONFIG.durabilityBarDisplayMode.getValue()) {
                case ALWAYS -> cir.setReturnValue(true);
                case NEVER -> cir.setReturnValue(false);
                case DAMAGED -> cir.setReturnValue(itemStack.isDamaged());
            }
        }
    }
}
