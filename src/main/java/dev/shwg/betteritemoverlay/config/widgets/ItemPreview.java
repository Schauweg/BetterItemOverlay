package dev.shwg.betteritemoverlay.config.widgets;

import dev.shwg.shwgconfig.gui.layout.RenderableElement;
import dev.shwg.shwgconfig.render.GenericGraphics;
import dev.shwg.shwgconfig.render.textures.GenericSprite;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.ItemStack;

public class ItemPreview implements RenderableElement {

    private static final GenericSprite hotbarSlot = GenericSprite.changedVanillaTexture("textures/gui/widgets.png", 24, 22, 29, 24, 256, 256, "hud/hotbar_offhand_left");

    private int x, y = 0;
    private boolean visible = true;
    private final ItemStack itemStack;

    public ItemPreview(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public void render(GenericGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            Font font = Minecraft.getInstance().font;
            graphics.drawSprite(hotbarSlot, getX(), getY() - 1);
            graphics.renderDecoratedItem(font, itemStack, getX() + 3, getY() + 3);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return 22;
    }

    @Override
    public int getHeight() {
        return 22;
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
