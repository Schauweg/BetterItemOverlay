package dev.shwg.betteritemoverlay.config.widgets;

import dev.shwg.betteritemoverlay.config.EnchantmentIconsValue.EnchantmentIcon;
import dev.shwg.betteritemoverlay.feature.EnchantmentIconFeature;
import dev.shwg.shwgconfig.gui.components.controller.list.ListSelectionListener;
import dev.shwg.shwgconfig.gui.layout.LayoutElement;
import dev.shwg.shwgconfig.gui.layout.RenderableElement;
import dev.shwg.shwgconfig.render.GenericGraphics;
import dev.shwg.shwgconfig.util.DisplayItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnchantmentIconEditorPreviewWidget implements RenderableElement, ListSelectionListener<EnchantmentIcon> {

    private static final int ITEM_OFFSET = 3;

    private final ItemStack pickaxe = DisplayItemStack.getStackSimple(Items.DIAMOND_PICKAXE);
    private final ItemPreview slot = new ItemPreview(pickaxe);


    private @Nullable LayoutElement row;
    private ItemStack icon = ItemStack.EMPTY;

    private int x, y;
    private boolean visible = true;

    public EnchantmentIconEditorPreviewWidget() {
    }

    @Override
    public void onSelectionChanged(@Nullable EnchantmentIcon value, @Nullable LayoutElement row) {
        this.row = row;
        this.icon = value == null ? ItemStack.EMPTY : DisplayItemStack.getStackSimple(value.item());
    }

    @Override
    public void render(GenericGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (!visible) return;

        int slotY = row == null ? y : row.getY() + (row.getHeight() - slot.getHeight()) / 2;
        slot.setX(x);
        slot.setY(slotY);
        slot.render(graphics, mouseX, mouseY, partialTicks);

        if (!icon.isEmpty()) {
            graphics.pushPose();
            graphics.translate(x + ITEM_OFFSET, slotY + ITEM_OFFSET);
            EnchantmentIconFeature.renderIcons(graphics, pickaxe, List.of(icon));
            graphics.popPose();
        }
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
        slot.setVisible(visible);
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
        return slot.getWidth();
    }

    @Override
    public int getHeight() {
        return slot.getHeight();
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void setWidth(int width) {
    }

    @Override
    public void setHeight(int height) {
    }
}
