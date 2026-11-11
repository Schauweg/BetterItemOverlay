package dev.shwg.betteritemoverlay.feature;

import dev.shwg.shwgconfig.render.GenericGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.ItemStack;

public interface OverlayFeature {
    void render(Font font, GenericGraphics graphics, ItemStack itemStack, int x, int y);
}
