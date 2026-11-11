package dev.shwg.betteritemoverlay.feature;

import dev.shwg.betteritemoverlay.BetterItemOverlay;
import dev.shwg.betteritemoverlay.config.BetterItemOverlayConfig;
import dev.shwg.betteritemoverlay.util.AmmoResolver;
import dev.shwg.betteritemoverlay.util.RenderingUtil;
import dev.shwg.shwgconfig.gui.layout.Alignment;
import dev.shwg.shwgconfig.render.GenericGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;

public class ArrowCounterFeature implements OverlayFeature {

    private static final float GAP = 0.5f;

    @Override
    public void render(Font font, GenericGraphics graphics, ItemStack itemStack, int x, int y) {
        if (!(itemStack.getItem() instanceof ProjectileWeaponItem)) return;
        BetterItemOverlayConfig config = BetterItemOverlay.CONFIG;
        if (!RenderingUtil.isVisible(config.arrowCounterHotbarVisibility, config.arrowCounterInventoryVisibility)) return;

        Player player = Minecraft.getInstance().player;
        AmmoResolver.AmmoInfo ammo = player != null
                ? AmmoResolver.resolve(player, itemStack)
                : AmmoResolver.AmmoInfo.preview();

        String text;
        if (ammo.isEmpty()) {
            text = "0";
        } else {
            text = ammo.infinite() ? "∞" : String.valueOf(ammo.count());
        }

        Alignment.CornerIconPosition position = config.arrowCounterPosition.getValue();
        float textScale = RenderingUtil.resolveScale(config.arrowCounterScale);
        float textWidth = font.width(text) * textScale;
        float groupHeight = font.lineHeight * textScale;

        float iconSize = font.lineHeight * textScale;
        float iconScale = iconSize / RenderingUtil.DEFAULT_ICON_SIZE;
        float groupWidth = iconSize + GAP + textWidth;

        float groupX = RenderingUtil.isRight(position) ? RenderingUtil.DEFAULT_ICON_SIZE - groupWidth : 0f;
        float groupY = RenderingUtil.isBottom(position)
                ? RenderingUtil.DEFAULT_ICON_SIZE - groupHeight - RenderingUtil.getBottomReservedHeight(itemStack)
                : 0f;

        graphics.pushPose();
        graphics.translate(0, 1);
        graphics.pushPose();
        graphics.translate(groupX, groupY + (groupHeight - iconSize) / 2f - textScale);
        graphics.scale(iconScale, iconScale);
        graphics.renderItem(ammo.projectile(), 0, 0);
        graphics.popPose();

        // count
        graphics.pushPose();
        graphics.translate(groupX + iconSize + GAP, groupY);
        graphics.scale(textScale, textScale);
        RenderingUtil.drawOverlayString(font, graphics, text, config.arrowCounterColor.getValue(), true);
        graphics.popPose();
        graphics.popPose();
    }
}
