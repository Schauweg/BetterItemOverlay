package dev.shwg.betteritemoverlay.mixin;

import dev.shwg.betteritemoverlay.BetterItemOverlay;
import dev.shwg.betteritemoverlay.OverlayRenderer;
import dev.shwg.betteritemoverlay.util.RenderContext;
import dev.shwg.betteritemoverlay.util.RenderingUtil;
import dev.shwg.shwgconfig.gui.layout.Alignment;
import dev.shwg.shwgconfig.render.GenericGraphics;
import dev.shwg.shwgconfig.render.GraphicsAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*? if >= 1.20 { */
import net.minecraft.client.gui.GuiGraphics;
/*? } else { */
/*import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.ItemRenderer;
*//*? } */

/*? if >= 1.20 { */
@Mixin(GuiGraphics.class)
/*? } else { */
//@Mixin(ItemRenderer.class)
/*? } */
public class ItemOverlayMixin {

    /*? if < 1.19.4 { */
    /*@Shadow
    public float blitOffset;
    *//*? } */
    /*? if < 1.21.6 { */
    /*@Unique
    private static final int Z_OFFSET = 60;
    *//*? } */

    /*? if >= 26 { */
    /*@Inject(method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    private void onItemDecorations(Font font, ItemStack itemStack, int x, int y, String countText, CallbackInfo ci) {
        GenericGraphics graphics = ((GraphicsAccessor) this).shwgConfigs$getGraphics();

    *//*? } else if >= 1.20 { */
    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    private void onItemDecorations(Font font, ItemStack itemStack, int x, int y, String countText, CallbackInfo ci) {
        GenericGraphics graphics = ((GraphicsAccessor) this).shwgConfigs$getGraphics();
    /*? } else if >= 1.19.4 { */
    /*@Inject(method = "renderGuiItemDecorations(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    private void onItemDecorations(PoseStack poseStack, Font font, ItemStack itemStack, int x, int y, String countText, CallbackInfo ci){
        GenericGraphics graphics = ((GraphicsAccessor) poseStack).shwgConfigs$getGraphics();
    *//*? } else { */
    /*@Inject(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    private void onItemDecorations(Font font, ItemStack itemStack, int x, int y, String countText, CallbackInfo ci) {
        PoseStack poseStack = new PoseStack();
        GenericGraphics graphics = new GenericGraphics(poseStack);
    *//*? } */

        if (itemStack.is(Items.AIR)) return;

        /*? if < 1.19.4 { */
        /*float zOffset = this.blitOffset;
        if (RenderContext.inHotbar) {
            zOffset += Z_OFFSET;
        }
        *//*? } else if < 1.21.6 { */
        //float zOffset = Z_OFFSET;
        /*? } */

        graphics.pushPose();
        graphics.translate(x, y); // translate to position of ItemStack, x and y of the top left corner of the rendered ItemSack are now 0/0

        //in versions below 1.21.6 the text appears behind the items without this z offset
        /*? if < 1.21.6 */
        //graphics.translate(0, 0, zOffset);

        OverlayRenderer.renderOverlay(font, graphics, itemStack, x, y, countText);

        graphics.popPose();
    }

}
