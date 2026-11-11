package dev.shwg.betteritemoverlay.mixin;

import dev.shwg.betteritemoverlay.util.RenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*~ if >= 26.2 'Gui' -> 'Hud' { */
import net.minecraft.client.gui.Gui;

@Mixin(Gui.class)
/*~ } */
public class HotbarMixin {

    //something very strange, in fabric since version 1.20.5 it is called renderHotbarAndDecorations but in forge and neoforge it is still only called renderHotbar
    //even till deobfuscated versions, so I switched to item hotbar (which tbh makes more sense anyway)

    /*~ if >= 1.20.5 'renderHotbar' -> 'renderItemHotbar' { */
    /*~ if >= 26 'renderItemHotbar' -> 'extractItemHotbar' { */
    @Inject(method = "renderItemHotbar", at = @At("HEAD"))
    private void enterHotbar(CallbackInfo ci) { RenderContext.inHotbar = true; }

    @Inject(method = "renderItemHotbar", at = @At("RETURN"))
    private void exitHotbar(CallbackInfo ci) { RenderContext.inHotbar = false; }
    /*~ } */
    /*~ } */
}
