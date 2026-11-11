/*? if >= 26.2 { */
/*package dev.shwg.betteritemoverlay.feature;

import dev.shwg.betteritemoverlay.BetterItemOverlay;
import dev.shwg.betteritemoverlay.config.BetterItemOverlayConfig;
import dev.shwg.betteritemoverlay.util.RenderingUtil;
import dev.shwg.shwgconfig.render.GenericGraphics;
import dev.shwg.shwgconfig.util.DisplayItemStack;
import net.minecraft.client.gui.Font;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.SulfurCubeContent;

public class SulfurCubeBucketFeature implements OverlayFeature {

    private static ItemStack previewContent;

    @Override
    public void render(Font font, GenericGraphics graphics, ItemStack itemStack, int x, int y) {
        ItemStack content = getContent(itemStack);
        if (content.isEmpty()) return;

        BetterItemOverlayConfig config = BetterItemOverlay.CONFIG;
        if (!RenderingUtil.isVisible(config.sulfurCubeIconHotbarVisibility, config.sulfurCubeIconInventoryVisibility)) return;

        RenderingUtil.renderPositionedIcon(graphics, itemStack, content, config.sulfurCubeIconPosition.getValue(),
                RenderingUtil.resolveScale(config.sulfurCubeIconScale));
    }

    public static ItemStack getContent(ItemStack itemStack) {
        SulfurCubeContent content = itemStack.get(DataComponents.SULFUR_CUBE_CONTENT);
        if (content != null) return content.absorbedBlockItemStack().create();

        if (RenderingUtil.isConfigPreview() && itemStack.is(Items.SULFUR_CUBE_BUCKET)) {
            if (previewContent == null) previewContent = DisplayItemStack.getStackSimple(Items.TNT);
            return previewContent;
        }
        return ItemStack.EMPTY;
    }
}
*//*? } */
