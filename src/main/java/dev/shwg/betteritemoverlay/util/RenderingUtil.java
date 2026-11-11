package dev.shwg.betteritemoverlay.util;

import dev.shwg.betteritemoverlay.BetterItemOverlay;
import dev.shwg.betteritemoverlay.config.BetterItemOverlayConfig;
import dev.shwg.betteritemoverlay.config.Options;
import dev.shwg.betteritemoverlay.feature.DurabilityNumberFeature;
import dev.shwg.shwgconfig.api.values.EnumValue;
import dev.shwg.shwgconfig.api.values.numbervalues.IntValue;
import dev.shwg.shwgconfig.gui.components.screens.ShwgConfigScreen;
import dev.shwg.shwgconfig.gui.layout.Alignment;
import dev.shwg.shwgconfig.render.GenericGraphics;
import dev.shwg.shwgconfig.util.DisplayItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.IntConsumer;

public final class RenderingUtil {

    public static final int DEFAULT_ICON_SIZE = 16;
    public static final float STACKED_ICON_OFFSET = 0.3f;

    private RenderingUtil() {
    }

    public static boolean isConfigPreview() {
        return !RenderContext.inHotbar && Minecraft.getInstance().screen instanceof ShwgConfigScreen;
    }

    public static boolean isVisible(EnumValue<Options.Visibility> hotbar, EnumValue<Options.Visibility> inventory) {
        if (isConfigPreview()) return true;
        BetterItemOverlayConfig config = BetterItemOverlay.CONFIG;
        if (!config.modEnabledToggle.getValue()) return false;

        Options.GeneralVisibility visibility = RenderContext.inHotbar
                ? resolve(hotbar, config.hotbarVisibility)
                : resolve(inventory, config.inventoryVisibility);
        return switch (visibility) {
            case ALWAYS -> true;
            case NEVER -> false;
            case ON_KEYBIND -> config.showOverlayKey.isKeyDown();
        };
    }

    public static boolean isShownAnywhere(EnumValue<Options.Visibility> hotbar, EnumValue<Options.Visibility> inventory) {
        BetterItemOverlayConfig config = BetterItemOverlay.CONFIG;
        return resolve(hotbar, config.hotbarVisibility) != Options.GeneralVisibility.NEVER
                || resolve(inventory, config.inventoryVisibility) != Options.GeneralVisibility.NEVER;
    }

    private static Options.GeneralVisibility resolve(EnumValue<Options.Visibility> feature, EnumValue<Options.GeneralVisibility> general) {
        Options.GeneralVisibility visibility = feature.getValue().asGeneral();
        return visibility != null ? visibility : general.getValue();
    }

    public static float resolveScale(IntValue featureScale) {
        int value = featureScale.getValue();
        if (value <= BetterItemOverlayConfig.SCALE_USE_GENERAL) {
            value = BetterItemOverlay.CONFIG.generalScale.getValue();
        }
        return value / 100f;
    }

    public static boolean isRight(Alignment.IconPosition position) {
        return position == Alignment.IconPosition.TOP_RIGHT || position == Alignment.IconPosition.BOTTOM_RIGHT;
    }

    public static boolean isRight(Alignment.CornerIconPosition position) {
        return position == Alignment.CornerIconPosition.TOP_RIGHT || position == Alignment.CornerIconPosition.BOTTOM_RIGHT;
    }

    public static boolean isBottom(Alignment.IconPosition position) {
        return position == Alignment.IconPosition.BOTTOM_LEFT || position == Alignment.IconPosition.BOTTOM_RIGHT;
    }

    public static boolean isBottom(Alignment.CornerIconPosition position) {
        return position == Alignment.CornerIconPosition.BOTTOM_LEFT || position == Alignment.CornerIconPosition.BOTTOM_RIGHT;
    }

    public static float getScaledStringX(Font font, Alignment.CornerIconPosition position, String text, float textScale) {
        return isRight(position) ? 15f - font.width(text) * textScale : 2f;
    }

    public static float getScaledStringX(Font font, Alignment.HorizontalIconPosition position, String text, float textScale) {
        return position == Alignment.HorizontalIconPosition.RIGHT ? 15f - font.width(text) * textScale : 2f;
    }

    public static float getBottomReservedHeight(ItemStack itemStack) {
        float reserved = 0;
        if (itemStack.isBarVisible()) reserved += 3;
        if (DurabilityNumberFeature.isShown(itemStack)) reserved += DurabilityNumberFeature.getHeight();
        return reserved;
    }

    public static float getTextHeight(float scale) {
        return Minecraft.getInstance().font.lineHeight * scale;
    }

    /**
     * Lifts what's drawn next in front of (or just behind) the slot's item icons. Only needed before 1.21.6.
     * Call inside a pushPose/popPose.
     * in >= 1.19.4 text translate z: +98 behind icon / +110 in front of icon
     * in <= 1.19.3 text translate z:
     * - in hotbar    +98 behind icon / +110 in front of icon
     * - in inventory +160 behind icon / + 210 in front of icon
     */
    public static void applyOverlayZ(GenericGraphics graphics, boolean behindIcons) {
    /*? if <1.21.6 { */
    /*/^? if >= 1.19.4 { ^/
    graphics.translate(0, 0, behindIcons ? 98 : 110);
    /^? } else { ^/
    /^if (RenderContext.inHotbar) {
        graphics.translate(0, 0, behindIcons ? 98 : 110);
    } else {
        graphics.translate(0, 0, behindIcons ? 160 : 210);
    }
    ^//^? } ^/
    *//*?} */
    }

    public static void drawOverlayString(Font font, GenericGraphics graphics, String string, int color, boolean behindIcons) {
        graphics.pushPose();
        applyOverlayZ(graphics, behindIcons);
        graphics.drawString(font, string, 0, 0, color);
        graphics.popPose();
    }


    public static void renderPositionedIcon(GenericGraphics graphics, ItemStack itemStack, Item icon, Alignment.IconPosition position, float scale) {
        renderPositionedIcon(graphics, itemStack, DisplayItemStack.getStackSimple(icon), position, scale);
    }

    /** Draws {@code icon} in a corner or the center of the slot, always above the durability bar and number. */
    public static void renderPositionedIcon(GenericGraphics graphics, ItemStack itemStack, ItemStack icon,
                                            Alignment.IconPosition position, float scale) {
        renderPositioned(graphics, itemStack, position, scale, () -> graphics.renderItem(icon, 0, 0));
    }

    /**
     * Positions and scales the pose like an icon of {@code scale}, then runs {@code draw},
     * which draws into a 16x16 box at (0, 0).
     */
    public static void renderPositioned(GenericGraphics graphics, ItemStack itemStack,
                                        Alignment.IconPosition position, float scale, Runnable draw) {
        float size = DEFAULT_ICON_SIZE * scale;
        float reserved = getBottomReservedHeight(itemStack);
        float x, y;
        if (position == Alignment.IconPosition.CENTER) {
            // centred in the space above the durability bar and number
            x = (DEFAULT_ICON_SIZE - size) / 2f;
            y = (DEFAULT_ICON_SIZE - reserved - size) / 2f;
        } else {
            x = isRight(position) ? DEFAULT_ICON_SIZE - size : 0f;
            y = isBottom(position) ? DEFAULT_ICON_SIZE - size - reserved : 0f;
        }

        graphics.pushPose();
        graphics.translate(x, y);
        graphics.scale(scale, scale);
        draw.run();
        graphics.popPose();
    }


    public static void renderPositionedIcon(GenericGraphics graphics, ItemStack itemStack, Item icon, Alignment.CornerIconPosition position, float scale) {
        renderPositionedIcon(graphics, itemStack, DisplayItemStack.getStackSimple(icon), position, scale);
    }

    public static void renderPositionedIcon(GenericGraphics graphics, ItemStack itemStack, ItemStack icon, Alignment.CornerIconPosition position, float scale) {
        float size = DEFAULT_ICON_SIZE * scale;
        float x = isRight(position) ? DEFAULT_ICON_SIZE - size : 0f;
        float y = isBottom(position) ? DEFAULT_ICON_SIZE - size - getBottomReservedHeight(itemStack) : 0f;

        graphics.pushPose();
        graphics.translate(x, y, 10); // translate in z because in versions below 1.21.6 icon can clip into block items
        graphics.scale(scale, scale);
        graphics.renderItem(icon, 0, 0);
        graphics.popPose();
    }

    public static void renderIconGroup(GenericGraphics graphics, ItemStack itemStack, int iconCount,
                                       Options.EnchantmentDisplayMode mode, Alignment.CornerIconPosition position,
                                       int maxAmount, float scale, IntConsumer drawIcon) {
        if (iconCount <= 0) return;

        float size = DEFAULT_ICON_SIZE * scale;
        float step = switch (mode) {
            case STACKED -> size * STACKED_ICON_OFFSET;
            case SIDE_BY_SIDE -> size;
            case SINGLE -> 0f;
        };
        int count = step == 0f ? 1 : Math.min(Math.min(maxAmount, iconCount), fittingIcons(size, step));

        boolean left = !isRight(position);
        float cornerX = left ? 0f : DEFAULT_ICON_SIZE - size;
        float iconY = isBottom(position) ? DEFAULT_ICON_SIZE - size - getBottomReservedHeight(itemStack) : 0f;
        float direction = left ? 1f : -1f; // away from the corner, into the slot

        // lowest priority first, so the highest priority ends up on top, at the corner
        for (int i = count - 1; i >= 0; i--) {
            graphics.pushPose();
            graphics.translate(cornerX + direction * i * step, iconY);
            /*? if < 1.21.6 */
            //graphics.translate(0, 0, (count - 1 - i) * size);
            graphics.scale(scale, scale);
            drawIcon.accept(i);
            graphics.popPose();
        }
    }

    private static int fittingIcons(float size, float step) {
        if (size >= DEFAULT_ICON_SIZE) return 1;
        return 1 + (int) Math.floor((DEFAULT_ICON_SIZE - size) / step + 1e-4f);
    }
}
