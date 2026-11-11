package dev.shwg.betteritemoverlay.feature;

import dev.shwg.betteritemoverlay.BetterItemOverlay;
import dev.shwg.betteritemoverlay.config.BetterItemOverlayConfig;
import dev.shwg.betteritemoverlay.config.EnchantmentIconsValue;
import dev.shwg.betteritemoverlay.util.RenderingUtil;
import dev.shwg.shwgconfig.render.GenericGraphics;
import dev.shwg.shwgconfig.util.DisplayItemStack;
import dev.shwg.shwgconfig.util.RegistryHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*? if >= 1.20.5 */
import net.minecraft.core.Holder;

import java.util.*;

public class EnchantmentIconFeature implements OverlayFeature {

    private static final Map<Identifier, ItemStack> ICON_STACKS = new HashMap<>();

    @Override
    public void render(Font font, GenericGraphics graphics, ItemStack itemStack, int x, int y) {
        BetterItemOverlayConfig config = BetterItemOverlay.CONFIG;
        if (!RenderingUtil.isVisible(config.enchantmentIconHotbarVisibility, config.enchantmentIconInventoryVisibility)) return;

        List<ItemStack> icons = getEnchantmentIcons(itemStack);
        if (icons.isEmpty()) return;

        renderIcons(graphics, itemStack, icons);
    }

    public static void renderIcons(GenericGraphics graphics, ItemStack itemStack, List<ItemStack> icons) {
        BetterItemOverlayConfig config = BetterItemOverlay.CONFIG;
        RenderingUtil.renderIconGroup(graphics, itemStack, icons.size(),
                config.enchantmentIconMode.getValue(),
                config.enchantmentIconPosition.getValue(),
                config.enchantmentIconAmount.getValue(),
                RenderingUtil.resolveScale(config.enchantmentIconScale),
                i -> graphics.renderItem(icons.get(i), 0, 0));
    }

    public static List<ItemStack> getEnchantmentIcons(ItemStack itemStack) {
        Set<Identifier> enchantments = getEnchantmentIds(itemStack);
        if (enchantments.isEmpty()) return List.of();

        List<ItemStack> icons = new ArrayList<>();
        for (EnchantmentIconsValue.EnchantmentIcon entry : BetterItemOverlay.CONFIG.enchantmentIcons.getValue()) {
            if (!enchantments.contains(entry.enchantment())) continue;
            ItemStack icon = ICON_STACKS.computeIfAbsent(entry.item(), DisplayItemStack::getStackSimple);

            if (!icon.isEmpty() && !icons.contains(icon)) icons.add(icon);
        }
        return icons;
    }

    public static Set<Identifier> getEnchantmentIds(ItemStack itemStack) {
        Set<Identifier> ids = new HashSet<>();
        /*? if >= 1.20.5 { */
        for (Holder<Enchantment> enchantment : EnchantmentHelper.getEnchantmentsForCrafting(itemStack).keySet()) {
            enchantment.unwrapKey().ifPresent(key -> ids.add(RegistryHelper.getID(key)));
        }
        /*? } else { */
        /*for (Enchantment enchantment : EnchantmentHelper.getEnchantments(itemStack).keySet()) {
            ids.add(RegistryHelper.getID(enchantment));
        }
        *//*? } */
        ids.addAll(DisplayItemStack.getFakeEnchantments(itemStack)); // config previews on 1.21+
        return ids;
    }
}
