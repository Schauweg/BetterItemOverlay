package dev.shwg.betteritemoverlay.config.widgets;

import dev.shwg.betteritemoverlay.BetterItemOverlay;
import dev.shwg.betteritemoverlay.config.EnchantmentIconsValue.EnchantmentIcon;
import dev.shwg.shwgconfig.gui.layout.GenericLayout;
import dev.shwg.shwgconfig.gui.layout.LayoutElement;
import dev.shwg.shwgconfig.gui.layout.RenderableElement;
import dev.shwg.shwgconfig.render.GenericGraphics;
import dev.shwg.shwgconfig.util.DisplayItemStack;
import net.minecraft.world.item.Items;
/*? if >= 1.21 { */
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
/*? } else { */
/*import dev.shwg.shwgconfig.util.RegistryHelper;
import net.minecraft.world.item.enchantment.Enchantment;
*//*? } */

import java.util.List;
import java.util.function.Consumer;

public class EnchantmentIconPreviewWidget implements GenericLayout, RenderableElement {

    private int x, y = 0;
    private final int width = 24;
    private final int height = 24;
    private boolean visible = true;

    private List<EnchantmentIcon> builtFor;
    private ItemPreview preview;

    public EnchantmentIconPreviewWidget() {
        rebuildIfChanged();
    }

    private void rebuildIfChanged() {
        List<EnchantmentIcon> icons = BetterItemOverlay.CONFIG.enchantmentIcons.getValue();
        if (icons == builtFor) return;
        builtFor = icons;

        DisplayItemStack stack = DisplayItemStack.builder(Items.DIAMOND_PICKAXE).withDamage(200, 1561);
        for (EnchantmentIcon icon : icons) {
            /*? if >= 1.21 { */
            stack.withEnchantment(ResourceKey.create(Registries.ENCHANTMENT, icon.enchantment()));
            /*? } else { */
            /*Enchantment enchantment = RegistryHelper.getEnchantment(icon.enchantment());
            if (enchantment != null) stack.withEnchantment(enchantment); // unknown ones can't be enchanted for real
            *//*? } */
        }
        preview = new ItemPreview(stack.build());
        preview.setVisible(visible);
        placePreview();
    }

    private void placePreview() {
        preview.setX(x + width / 2 - preview.getWidth() / 2);
        preview.setY(y);
    }

    @Override
    public void render(GenericGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            rebuildIfChanged();
            preview.render(graphics, mouseX, mouseY, partialTicks);
        }
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
