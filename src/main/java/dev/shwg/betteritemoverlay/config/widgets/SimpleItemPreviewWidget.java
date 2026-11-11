package dev.shwg.betteritemoverlay.config.widgets;

import dev.shwg.shwgconfig.gui.layout.GenericLayout;
import dev.shwg.shwgconfig.gui.layout.LayoutElement;
import dev.shwg.shwgconfig.gui.layout.RenderableElement;
import dev.shwg.shwgconfig.render.GenericGraphics;
import dev.shwg.shwgconfig.util.DisplayItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class SimpleItemPreviewWidget implements GenericLayout, RenderableElement {

    private int x, y = 0;
    private final int width = 24;
    private final int height = 24;
    private boolean visible = true;

    private final ItemPreview preview;

    public SimpleItemPreviewWidget(ItemStack stack) {
        preview = new ItemPreview(stack);
        placePreview();
    }

    public SimpleItemPreviewWidget(Item preview) {
        this(DisplayItemStack.getStackSimple(preview));
    }

    private void placePreview() {
        preview.setX(x + width / 2 - preview.getWidth() / 2);
        preview.setY(y);
    }

    @Override
    public void render(GenericGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (visible) preview.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
        preview.setVisible(visible);
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
        placePreview();
    }

    @Override
    public void setY(int y) {
        this.y = y;
        placePreview();
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
