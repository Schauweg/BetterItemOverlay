package dev.shwg.betteritemoverlay.config.widgets;

import dev.shwg.shwgconfig.gui.components.widget.slider.NumberSlider;
import dev.shwg.shwgconfig.gui.layout.GenericLayout;
import dev.shwg.shwgconfig.gui.layout.LayoutElement;
import dev.shwg.shwgconfig.gui.layout.RenderableElement;
import dev.shwg.shwgconfig.render.GenericGraphics;
import dev.shwg.shwgconfig.util.DisplayItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class DurabilityPreviewWidget implements GenericLayout, RenderableElement {

    private int x, y = 0;
    private final int width = 50;
    private final int height = 44;
    private boolean visible = true;

    private final ItemStack itemStack = DisplayItemStack.builder(Items.DIAMOND_PICKAXE).withDamage(0, DisplayItemStack.DIAMOND_TOOL_MAX_DAMAGE).build();
    private final ItemPreview preview;
    private final NumberSlider<Integer> damageSlider;

    public DurabilityPreviewWidget() {

        preview = new ItemPreview(itemStack);

        this.damageSlider = NumberSlider.intSlider(Component.empty(), 1, itemStack.getMaxDamage(), itemStack.getMaxDamage())
                .applyValue(integer -> itemStack.setDamageValue(itemStack.getMaxDamage() - integer))
                .size(width, 20)
                .build();

        this.damageSlider.setVisible(true);
    }

    @Override
    public void render(GenericGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            preview.render(graphics, mouseX, mouseY, partialTicks);
            damageSlider.render(graphics, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
        preview.setVisible(visible);
        damageSlider.setVisible(visible);
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
        this.preview.setX(x + width / 2 - preview.getWidth() / 2);
        this.damageSlider.setX(x);
    }

    @Override
    public void setY(int y) {
        this.y = y;
        this.preview.setY(y);
        this.damageSlider.setY(getBottom() - damageSlider.getHeight());
    }

    @Override
    public void setWidth(int width) {
    }

    @Override
    public void setHeight(int height) {
    }

    @Override
    public void visitChildren(Consumer<LayoutElement> consumer) {
        consumer.accept(damageSlider);
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
