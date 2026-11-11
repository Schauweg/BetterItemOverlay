package dev.shwg.betteritemoverlay;

import dev.shwg.betteritemoverlay.feature.OverlayFeature;
import dev.shwg.shwgconfig.render.GenericGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OverlayRenderer {

    private static final List<OverlayFeature> FEATURES = new ArrayList<>();

    public static void register(OverlayFeature feature) {
        FEATURES.add(feature);
    }

    public static void renderOverlay(Font font, GenericGraphics graphics, ItemStack itemStack, int x, int y, String countText) {
        for (OverlayFeature feature : FEATURES) {
            feature.render(font, graphics, itemStack, x, y);
        }
    }
}
