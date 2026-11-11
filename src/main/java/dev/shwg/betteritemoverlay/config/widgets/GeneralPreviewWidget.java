package dev.shwg.betteritemoverlay.config.widgets;

import dev.shwg.shwgconfig.gui.layout.GenericLayout;
import dev.shwg.shwgconfig.gui.layout.LayoutElement;
import dev.shwg.shwgconfig.gui.layout.RenderableElement;
import dev.shwg.shwgconfig.render.GenericGraphics;
import dev.shwg.shwgconfig.util.DisplayItemStack;
import dev.shwg.shwgconfig.util.RegistryHelper;

import java.util.List;
import java.util.function.Consumer;

public class GeneralPreviewWidget implements GenericLayout, RenderableElement {

    private static final long CYCLE_MILLIS = 2000;

    private int x, y = 0;
    private final int width;
    private final int height;
    private boolean visible = true;

    private final List<GenericLayout> previews = List.of(
            new DurabilityPreviewWidget(),
            new ArrowCounterPreviewWidget(),
            new EnchantmentIconPreviewWidget(),
            new SimpleItemPreviewWidget(DisplayItemStack.getStackSimple(RegistryHelper.build("minecraft", "waxed_copper_block")))
    );

    public GeneralPreviewWidget() {
        int maxWidth = 0, maxHeight = 0;
        for (GenericLayout preview : previews) {
            maxWidth = Math.max(maxWidth, preview.getWidth());
            maxHeight = Math.max(maxHeight, preview.getHeight());
        }
        width = maxWidth;
        height = maxHeight;
        placePreviews();
    }

    private void placePreviews() {
        for (GenericLayout preview : previews) {
            preview.setX(x + width / 2 - preview.getWidth() / 2);
            preview.setY(y);
        }
    }

    private GenericLayout current() {
        int index = (int) ((System.currentTimeMillis() / CYCLE_MILLIS) % previews.size());
        return previews.get(index);
    }

    @Override
    public void render(GenericGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (visible && current() instanceof RenderableElement renderable) {
            renderable.render(graphics, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
        for (GenericLayout preview : previews) {
            if (preview instanceof RenderableElement renderable) renderable.setVisible(visible);
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setX(int x) {
        this.x = x;
        placePreviews();
    }

    @Override
    public void setY(int y) {
        this.y = y;
        placePreviews();
    }

    @Override
    public void setWidth(int width) {
    }

    @Override
    public void setHeight(int height) {
    }

    @Override
    public void visitChildren(Consumer<LayoutElement> consumer) {
    }

    @Override
    public int getContentHeight() {
        return height;
    }

    @Override
    public int getContentWidth() {
        return width;
    }
}
