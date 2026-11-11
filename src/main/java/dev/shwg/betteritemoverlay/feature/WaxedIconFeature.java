package dev.shwg.betteritemoverlay.feature;

import dev.shwg.betteritemoverlay.BetterItemOverlay;
import dev.shwg.betteritemoverlay.config.BetterItemOverlayConfig;
import dev.shwg.betteritemoverlay.util.RenderingUtil;
import dev.shwg.shwgconfig.render.GenericGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;

public class WaxedIconFeature implements OverlayFeature {

    @Override
    public void render(Font font, GenericGraphics graphics, ItemStack itemStack, int x, int y) {
        if (!isWaxed(itemStack)) return;
        BetterItemOverlayConfig config = BetterItemOverlay.CONFIG;
        if (!RenderingUtil.isVisible(config.waxedIconHotbarVisibility, config.waxedIconInventoryVisibility)) return;

        RenderingUtil.renderPositionedIcon(graphics, itemStack, config.waxedDisplayItem.getItem(),
                config.waxedIconPosition.getValue(), RenderingUtil.resolveScale(config.waxedIconScale));
    }

    public static boolean isWaxed(ItemStack itemStack) {
        return itemStack.getItem() instanceof BlockItem blockItem
                && HoneycombItem.WAX_OFF_BY_BLOCK.get().containsKey(blockItem.getBlock());
    }
}
