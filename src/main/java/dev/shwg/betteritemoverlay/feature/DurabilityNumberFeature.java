package dev.shwg.betteritemoverlay.feature;

import dev.shwg.betteritemoverlay.BetterItemOverlay;
import dev.shwg.betteritemoverlay.config.BetterItemOverlayConfig;
import dev.shwg.betteritemoverlay.util.RenderingUtil;
import dev.shwg.shwgconfig.render.GenericGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.ItemStack;

public class DurabilityNumberFeature implements OverlayFeature {

    @Override
    public void render(Font font, GenericGraphics graphics, ItemStack itemStack, int ignoredX, int ignoredY) {
        if (!isShown(itemStack)) return;
        BetterItemOverlayConfig config = BetterItemOverlay.CONFIG;

        String text = String.valueOf(itemStack.getMaxDamage() - itemStack.getDamageValue() + (config.durabilityNumberOffset.getValue() ? -1 : 0));
        float scale = getScale();
        float x = RenderingUtil.getScaledStringX(font, config.durabilityBarAlignment.getValue(), text, scale);
        float y = RenderingUtil.DEFAULT_ICON_SIZE - getHeight();
        if (itemStack.isBarVisible()) y -= 3;

        int color = config.useDurabilityBarColor.getValue()
                ? itemStack.getBarColor() | 0xFF000000
                : config.customDurabilityNumberColor.getValue();

        graphics.pushPose();
        graphics.translate(x, y);
        graphics.scale(scale, scale);
        RenderingUtil.drawOverlayString(font, graphics, text, color, true);
        graphics.popPose();
    }

    public static boolean isShown(ItemStack itemStack) {
        if (!itemStack.isDamageableItem()) return false;
        BetterItemOverlayConfig config = BetterItemOverlay.CONFIG;
        boolean modeAllows = switch (config.durabilityNumberDisplayMode.getValue()) {
            case ALWAYS -> true;
            case DAMAGED -> itemStack.isDamaged();
            default -> false;
        };
        return modeAllows && RenderingUtil.isVisible(config.durabilityHotbarVisibility, config.durabilityInventoryVisibility);
    }

    public static float getScale() {
        return RenderingUtil.resolveScale(BetterItemOverlay.CONFIG.durabilityNumberScale);
    }

    public static float getHeight() {
        return RenderingUtil.getTextHeight(getScale());
    }
}
