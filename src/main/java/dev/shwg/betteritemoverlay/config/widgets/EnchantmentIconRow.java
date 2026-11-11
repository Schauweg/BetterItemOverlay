package dev.shwg.betteritemoverlay.config.widgets;

import dev.shwg.betteritemoverlay.config.EnchantmentIconsValue.EnchantmentIcon;
import dev.shwg.shwgconfig.gui.components.controller.list.ListRow;
import dev.shwg.shwgconfig.gui.components.widget.editbox.suggestioneditbox.SuggestionEditBox;
import dev.shwg.shwgconfig.gui.components.widget.editbox.suggestioneditbox.SuggestionProviders;
import dev.shwg.shwgconfig.util.RegistryHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnchantmentIconRow extends ListRow<EnchantmentIcon> {

    private static final int BOX_WIDTH = 100;
    private static final int BOX_HEIGHT = 20;

    private @Nullable Identifier enchantment;
    private @Nullable Identifier item;

    public EnchantmentIconRow(@Nullable EnchantmentIcon initial) {
        super(initial);
        if (initial != null) {
            enchantment = initial.enchantment();
            item = initial.item();
        }

        addWidget(SuggestionEditBox.builder(font(), Component.empty(),
                        SuggestionProviders.excluding(SuggestionProviders.ENCHANTMENTS, this::usedEnchantments))
                .value(idString(enchantment))
                .hint(Component.translatable("better_item_overlay.config.tools.enchantment_icon_editor.enchantment_hint"))
                .applyValue(value -> {
                    enchantment = RegistryHelper.tryBuild(value);
                    update();
                })
                .size(BOX_WIDTH, BOX_HEIGHT)
                .build());

        addWidget(SuggestionEditBox.builder(font(), Component.empty(), SuggestionProviders.ITEMS)
                .value(idString(item))
                .hint(Component.translatable("better_item_overlay.config.tools.enchantment_icon_editor.item_hint"))
                .applyValue(value -> {
                    item = RegistryHelper.tryBuild(value);
                    update();
                })
                .size(BOX_WIDTH, BOX_HEIGHT)
                .build());
    }

    private void update() {
        if (enchantment != null && item != null) {
            commit(new EnchantmentIcon(enchantment, item));
        }
    }

    private List<String> usedEnchantments() {
        return otherValues().stream()
                .map(icon -> icon.enchantment().toString())
                .toList();
    }

    private static @Nullable String idString(@Nullable Identifier id) {
        return id == null ? null : id.toString();
    }
}
