package dev.shwg.betteritemoverlay.config;

import dev.shwg.betteritemoverlay.config.widgets.*;
import dev.shwg.betteritemoverlay.feature.PotionEffectFeature;
import dev.shwg.betteritemoverlay.util.RenderingUtil;
import dev.shwg.shwgconfig.api.*;
import dev.shwg.shwgconfig.api.entries.EntryOptions;
import dev.shwg.shwgconfig.api.entries.ListOptions;
import dev.shwg.shwgconfig.api.entries.display.EnumLabels;
import dev.shwg.shwgconfig.api.entries.display.NumberDisplay;
import dev.shwg.shwgconfig.api.model.ConfigCategory;
import dev.shwg.shwgconfig.api.model.Tab;
import dev.shwg.shwgconfig.api.values.BooleanValue;
import dev.shwg.shwgconfig.api.values.ColorValue;
import dev.shwg.shwgconfig.api.values.EnumValue;
import dev.shwg.shwgconfig.api.values.numbervalues.IntValue;
import dev.shwg.shwgconfig.api.values.stringvalues.ItemValue;
import dev.shwg.shwgconfig.api.values.stringvalues.KeybindValue;
import dev.shwg.shwgconfig.gui.deferred.GenericTooltip;
import dev.shwg.shwgconfig.gui.layout.Alignment;
import dev.shwg.shwgconfig.util.DisplayItemStack;
import dev.shwg.shwgconfig.util.RegistryHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.function.Supplier;

public class BetterItemOverlayConfig extends ConfigBase {

    public static final int SCALE_USE_GENERAL = 19;

    @Override
    public ConfigManager.ScreenManager buildScreen() {
        Builder builder = Builder.create(Component.translatable("better_item_overlay.config.title"));
        builder.addTab(generalTab());
        builder.addTab(toolsTab());
        builder.addTab(enchantmentsTab());
        builder.addTab(miscTab());
        return builder.build();
    }


    //  Shared entry helpers

    private static void addVisibility(CategoryBuilder category, EnumValue<Options.Visibility> hotbar, EnumValue<Options.Visibility> inventory, Supplier<Boolean> enabled) {
        category.addDropdown(hotbar, Component.translatable("better_item_overlay.config.visibility.hotbar"),
                EnumLabels.translated("better_item_overlay.options"),
                EntryOptions.create().enabledWhen(enabled));
        category.addDropdown(inventory, Component.translatable("better_item_overlay.config.visibility.inventory"),
                EnumLabels.translated("better_item_overlay.options"),
                EntryOptions.create().enabledWhen(enabled));
    }

    private static NumberDisplay<Integer> featureScaleDisplay() {
        return NumberDisplay.custom(value -> value == SCALE_USE_GENERAL
                ? Component.translatable("better_item_overlay.options.use_general")
                : Component.literal(value + "%"));
    }

    //  TAB: General

    private Tab generalTab() {
        TabBuilder tab = TabBuilder.create(Component.translatable("better_item_overlay.config.general"));
        tab.addCategory(generalCategory());
        return tab.build();
    }

    // General - General

    public final BooleanValue modEnabledToggle = BooleanValue.of(true);
    public final EnumValue<Options.GeneralVisibility> hotbarVisibility = EnumValue.of(Options.GeneralVisibility.ALWAYS);
    public final EnumValue<Options.GeneralVisibility> inventoryVisibility = EnumValue.of(Options.GeneralVisibility.ALWAYS);
    public final KeybindValue showOverlayKey = KeybindValue.unbound(
            "key.better_item_overlay.show_overlay", RegistryHelper.build("better_item_overlay", "keybinds"));
    public final IntValue generalScale = IntValue.of(50, 20, 100);

    private ConfigCategory generalCategory() {
        CategoryBuilder category = CategoryBuilder.create(Component.translatable("better_item_overlay.config.general"));

        category.addToggle(modEnabledToggle, Component.translatable("better_item_overlay.config.general.enabled"));

        category.addDropdown(hotbarVisibility, Component.translatable("better_item_overlay.config.visibility.hotbar"),
                EnumLabels.translated("better_item_overlay.options"),
                EntryOptions.create().enabledWhen(modEnabledToggle::getValue)
        );

        category.addDropdown(inventoryVisibility, Component.translatable("better_item_overlay.config.visibility.inventory"),
                EnumLabels.translated("better_item_overlay.options"),
                EntryOptions.create().enabledWhen(modEnabledToggle::getValue)
        );

        category.addKeybind(showOverlayKey, EntryOptions.create()
                .tooltip(Component.translatable("better_item_overlay.config.general.show_overlay_key.tooltip"))
        );

        category.addNumberSlider(generalScale, Component.translatable("better_item_overlay.config.general.scale"),
                NumberDisplay.percentage(),
                EntryOptions.create()
                        .labelTooltip(Component.translatable("better_item_overlay.config.general.scale.tooltip"))
                        .enabledWhen(modEnabledToggle::getValue)
        );

        category.preview(new GeneralPreviewWidget());

        return category.build();
    }

    //  TAB: Tools

    private Tab toolsTab() {
        TabBuilder tab = TabBuilder.create(Component.translatable("better_item_overlay.config.tools"));
        tab.addCategory(durabilityCategory());
        tab.addCategory(arrowCounterCategory());
        return tab.build();
    }

    // - Tools - Durability

    public final EnumValue<Options.Visibility> durabilityHotbarVisibility = EnumValue.of(Options.Visibility.USE_GENERAL);
    public final EnumValue<Options.Visibility> durabilityInventoryVisibility = EnumValue.of(Options.Visibility.USE_GENERAL);
    public final EnumValue<Options.DamageDisplayMode> durabilityBarDisplayMode = EnumValue.of(Options.DamageDisplayMode.DAMAGED);
    public final EnumValue<Options.DamageDisplayMode> durabilityNumberDisplayMode = EnumValue.of(Options.DamageDisplayMode.ALWAYS);
    public final IntValue durabilityNumberScale = IntValue.of(SCALE_USE_GENERAL, SCALE_USE_GENERAL, 100);
    public final BooleanValue durabilityNumberOffset = BooleanValue.of(true);
    public final EnumValue<Alignment.HorizontalIconPosition> durabilityBarAlignment = EnumValue.of(Alignment.HorizontalIconPosition.RIGHT);
    public final BooleanValue useDurabilityBarColor = BooleanValue.of(true);
    public final ColorValue customDurabilityNumberColor = ColorValue.rgb(0xFFFFFF);

    private boolean durabilityNumberShown() {
        return durabilityNumberDisplayMode.getValue() != Options.DamageDisplayMode.NEVER
                && RenderingUtil.isShownAnywhere(durabilityHotbarVisibility, durabilityInventoryVisibility);
    }

    private ConfigCategory durabilityCategory() {
        CategoryBuilder category = CategoryBuilder.create(Component.translatable("better_item_overlay.config.tools.durability"));

        addVisibility(category, durabilityHotbarVisibility, durabilityInventoryVisibility, modEnabledToggle::getValue);

        category.addDropdown(durabilityBarDisplayMode, Component.translatable("better_item_overlay.config.tools.durability.bar_mode"),
                EnumLabels.translated("better_item_overlay.options"),
                EntryOptions.create().labelTooltip(Component.translatable("better_item_overlay.config.tools.durability.bar_mode.tooltip"))
        );

        category.addDropdown(durabilityNumberDisplayMode, Component.translatable("better_item_overlay.config.tools.durability.number_mode"),
                EnumLabels.translated("better_item_overlay.options"),
                EntryOptions.create().labelTooltip(Component.translatable("better_item_overlay.config.tools.durability.number_mode.tooltip"))
        );

        category.addNumberSlider(durabilityNumberScale, Component.translatable("better_item_overlay.config.tools.durability.number_scale"),
                featureScaleDisplay(),
                EntryOptions.create()
                        .labelTooltip(Component.translatable("better_item_overlay.config.tools.durability.number_scale.tooltip"))
                        .enabledWhen(this::durabilityNumberShown)
        );

        category.addToggle(durabilityNumberOffset, Component.translatable("better_item_overlay.config.tools.durability.number_offset"),
                EntryOptions.create().labelTooltip(GenericTooltip.builder()
                                .line(Component.translatable("better_item_overlay.config.tools.durability.number_offset.tooltip"))
                                .bullet(Component.translatable("better_item_overlay.config.tools.durability.number_offset.tooltip.on"))
                                .bullet(Component.translatable("better_item_overlay.config.tools.durability.number_offset.tooltip.off"))
                                .build())
                        .enabledWhen(this::durabilityNumberShown)
        );

        category.addCycleButton(durabilityBarAlignment, Component.translatable("better_item_overlay.config.tools.durability.number_alignment"),
                EnumLabels.translated("shwgconfig.alignment"),
                EntryOptions.create().enabledWhen(this::durabilityNumberShown)
        );

        category.addToggle(useDurabilityBarColor, Component.translatable("better_item_overlay.config.tools.durability.number_color.bar"),
                Component.translatable("better_item_overlay.config.tools.durability.number_color.bar.true"),
                Component.translatable("better_item_overlay.config.tools.durability.number_color.bar.false"),
                EntryOptions.create().enabledWhen(this::durabilityNumberShown)
        );

        category.addColor(customDurabilityNumberColor, Component.translatable("better_item_overlay.config.tools.durability.number_color.custom"),
                EntryOptions.create().enabledWhen(() -> durabilityNumberShown() && !useDurabilityBarColor.getValue())
        );

        category.preview(new DurabilityPreviewWidget());

        return category.build();
    }

    // - Tools - Arrow Counter

    public final EnumValue<Options.Visibility> arrowCounterHotbarVisibility = EnumValue.of(Options.Visibility.USE_GENERAL);
    public final EnumValue<Options.Visibility> arrowCounterInventoryVisibility = EnumValue.of(Options.Visibility.USE_GENERAL);
    public final EnumValue<Alignment.CornerIconPosition> arrowCounterPosition = EnumValue.of(Alignment.CornerIconPosition.TOP_LEFT);
    public final IntValue arrowCounterScale = IntValue.of(SCALE_USE_GENERAL, SCALE_USE_GENERAL, 100);
    public final ColorValue arrowCounterColor = ColorValue.rgb(0xFFFFFF);

    private boolean arrowCounterShown() {
        return RenderingUtil.isShownAnywhere(arrowCounterHotbarVisibility, arrowCounterInventoryVisibility);
    }

    private ConfigCategory arrowCounterCategory() {
        CategoryBuilder category = CategoryBuilder.create(Component.translatable("better_item_overlay.config.tools.arrow_counter"));

        addVisibility(category, arrowCounterHotbarVisibility, arrowCounterInventoryVisibility, modEnabledToggle::getValue);

        category.addDropdown(arrowCounterPosition, Component.translatable("better_item_overlay.config.tools.arrow_counter.position"),
                EnumLabels.translated("shwgconfig.alignment"),
                EntryOptions.create().enabledWhen(this::arrowCounterShown)
        );

        category.addNumberSlider(arrowCounterScale, Component.translatable("better_item_overlay.config.tools.arrow_counter.scale"),
                featureScaleDisplay(),
                EntryOptions.create().enabledWhen(this::arrowCounterShown)
        );

        category.addColor(arrowCounterColor, Component.translatable("better_item_overlay.config.tools.arrow_counter.color"),
                EntryOptions.create().enabledWhen(this::arrowCounterShown)
        );

        category.preview(new ArrowCounterPreviewWidget());

        return category.build();
    }

    // - TAB: Enchantments

    private Tab enchantmentsTab() {
        TabBuilder tab = TabBuilder.create(Component.translatable("better_item_overlay.config.enchantments"));
        tab.addCategory(enchantmentIconCategory());
        tab.addCategory(enchantmentIconEditorCategory());
        return tab.build();
    }

    // - Enchantments - Enchantment Icons

    public final EnumValue<Options.Visibility> enchantmentIconHotbarVisibility = EnumValue.of(Options.Visibility.USE_GENERAL);
    public final EnumValue<Options.Visibility> enchantmentIconInventoryVisibility = EnumValue.of(Options.Visibility.USE_GENERAL);
    public final EnumValue<Options.EnchantmentDisplayMode> enchantmentIconMode = EnumValue.of(Options.EnchantmentDisplayMode.STACKED);
    public final EnumValue<Alignment.CornerIconPosition> enchantmentIconPosition = EnumValue.of(Alignment.CornerIconPosition.TOP_LEFT);
    public final IntValue enchantmentIconAmount = IntValue.of(3, 1, 5);
    public final IntValue enchantmentIconScale = IntValue.of(SCALE_USE_GENERAL, SCALE_USE_GENERAL, 100);

    private boolean enchantmentIconsShown() {
        return RenderingUtil.isShownAnywhere(enchantmentIconHotbarVisibility, enchantmentIconInventoryVisibility);
    }

    private ConfigCategory enchantmentIconCategory() {
        CategoryBuilder category = CategoryBuilder.create(Component.translatable("better_item_overlay.config.tools.enchantment_icon"));

        addVisibility(category, enchantmentIconHotbarVisibility, enchantmentIconInventoryVisibility, modEnabledToggle::getValue);

        category.addDropdown(enchantmentIconMode, Component.translatable("better_item_overlay.config.tools.enchantment_icon.mode"),
                EnumLabels.translated("better_item_overlay.options"),
                EntryOptions.create()
                        .labelTooltip(GenericTooltip.builder()
                                .line(Component.translatable("better_item_overlay.config.tools.enchantment_icon.mode.tooltip"))
                                .bullet(Component.translatable("better_item_overlay.config.tools.enchantment_icon.mode.tooltip.single"))
                                .bullet(Component.translatable("better_item_overlay.config.tools.enchantment_icon.mode.tooltip.stacked"))
                                .bullet(Component.translatable("better_item_overlay.config.tools.enchantment_icon.mode.tooltip.side_by_side"))
                                .build())
                        .enabledWhen(this::enchantmentIconsShown)
        );

        category.addDropdown(enchantmentIconPosition, Component.translatable("better_item_overlay.config.tools.enchantment_icon.position"),
                EnumLabels.translated("shwgconfig.alignment"),
                EntryOptions.create().enabledWhen(this::enchantmentIconsShown)
        );

        category.addNumberSlider(enchantmentIconAmount, Component.translatable("better_item_overlay.config.tools.enchantment_icon.amount"),
                NumberDisplay.raw(),
                EntryOptions.create()
                        .labelTooltip(Component.translatable("better_item_overlay.config.tools.enchantment_icon.amount.tooltip"))
                        .enabledWhen(() -> {
                            Options.EnchantmentDisplayMode mode = enchantmentIconMode.getValue();
                            return enchantmentIconsShown()
                                    && (mode == Options.EnchantmentDisplayMode.STACKED || mode == Options.EnchantmentDisplayMode.SIDE_BY_SIDE);
                        })
        );

        category.addNumberSlider(enchantmentIconScale, Component.translatable("better_item_overlay.config.tools.enchantment_icon.scale"),
                featureScaleDisplay(),
                EntryOptions.create().enabledWhen(this::enchantmentIconsShown)
        );

        category.preview(new EnchantmentIconPreviewWidget());

        return category.build();
    }

    // - Enchantments - Enchantment Icon Editor

    public final EnchantmentIconsValue enchantmentIcons = EnchantmentIconsValue.of(
            EnchantmentIconsValue.EnchantmentIcon.of(Enchantments.FORTUNE, Items.DIAMOND),
            EnchantmentIconsValue.EnchantmentIcon.of(Enchantments.SILK_TOUCH, Items.DIAMOND_ORE)
    );

    private ConfigCategory enchantmentIconEditorCategory() {
        CategoryBuilder category = CategoryBuilder.create(Component.translatable("better_item_overlay.config.tools.enchantment_icon_editor"));
        EnchantmentIconEditorPreviewWidget preview = new EnchantmentIconEditorPreviewWidget();
        category.addList(enchantmentIcons, EnchantmentIconRow::new, ListOptions.create()
                .enabledWhen(this::enchantmentIconsShown)
                .onSelectionChanged(preview));
        category.preview(preview);
        category.useOwnControllerWidth();
        return category.build();
    }

    // - TAB: Misc

    private Tab miscTab() {
        TabBuilder tab = TabBuilder.create(Component.translatable("better_item_overlay.config.misc"));
        tab.addCategory(waxedIconCategory());
        /*? if >=26.2 */
        //tab.addCategory(sulfurCubeCategory());
        tab.addCategory(potionEffectCategory());
        return tab.build();
    }

    // - Misc - Waxed Icon

    public final EnumValue<Options.Visibility> waxedIconHotbarVisibility = EnumValue.of(Options.Visibility.USE_GENERAL);
    public final EnumValue<Options.Visibility> waxedIconInventoryVisibility = EnumValue.of(Options.Visibility.USE_GENERAL);
    public final EnumValue<Alignment.IconPosition> waxedIconPosition = EnumValue.of(Alignment.IconPosition.TOP_RIGHT);
    public final ItemValue waxedDisplayItem = ItemValue.of(Items.HONEYCOMB);
    public final IntValue waxedIconScale = IntValue.of(SCALE_USE_GENERAL, SCALE_USE_GENERAL, 100);

    private boolean waxedIconShown() {
        return RenderingUtil.isShownAnywhere(waxedIconHotbarVisibility, waxedIconInventoryVisibility);
    }

    private ConfigCategory waxedIconCategory() {
        CategoryBuilder category = CategoryBuilder.create(Component.translatable("better_item_overlay.config.tools.waxed_icon"));

        addVisibility(category, waxedIconHotbarVisibility, waxedIconInventoryVisibility, modEnabledToggle::getValue);

        category.addDropdown(waxedIconPosition, Component.translatable("better_item_overlay.config.tools.waxed_icon.position"),
                EnumLabels.translated("shwgconfig.alignment"),
                EntryOptions.create().enabledWhen(this::waxedIconShown)
        );

        category.addItemSuggestionInput(waxedDisplayItem, Component.translatable("better_item_overlay.config.tools.waxed_icon.display_item"),
                EntryOptions.create().enabledWhen(this::waxedIconShown)
        );

        category.addNumberSlider(waxedIconScale, Component.translatable("better_item_overlay.config.tools.waxed_icon.scale"),
                featureScaleDisplay(),
                EntryOptions.create().enabledWhen(this::waxedIconShown)
        );

        category.preview(new SimpleItemPreviewWidget(DisplayItemStack.getStackSimple(RegistryHelper.build("minecraft", "waxed_copper_block"))));

        return category.build();
    }

    // - Misc - Sulfur Cube Bucket

    /*? if >=26.2 { */
    /*public final EnumValue<Options.Visibility> sulfurCubeIconHotbarVisibility = EnumValue.of(Options.Visibility.USE_GENERAL);
    public final EnumValue<Options.Visibility> sulfurCubeIconInventoryVisibility = EnumValue.of(Options.Visibility.USE_GENERAL);
    public final EnumValue<Alignment.IconPosition> sulfurCubeIconPosition = EnumValue.of(Alignment.IconPosition.CENTER);
    public final IntValue sulfurCubeIconScale = IntValue.of(SCALE_USE_GENERAL, SCALE_USE_GENERAL, 100);

    private boolean sulfurCubeIconShown() {
        return RenderingUtil.isShownAnywhere(sulfurCubeIconHotbarVisibility, sulfurCubeIconInventoryVisibility);
    }

    private ConfigCategory sulfurCubeCategory() {
        CategoryBuilder category = CategoryBuilder.create(Component.translatable("better_item_overlay.config.tools.sulfur_cube_icon"));
        addVisibility(category, sulfurCubeIconHotbarVisibility, sulfurCubeIconInventoryVisibility, modEnabledToggle::getValue);

        category.addDropdown(sulfurCubeIconPosition, Component.translatable("better_item_overlay.config.tools.sulfur_cube_icon.position"),
                EnumLabels.translated("shwgconfig.alignment"),
                EntryOptions.create().enabledWhen(this::sulfurCubeIconShown)
        );

        category.addNumberSlider(sulfurCubeIconScale, Component.translatable("better_item_overlay.config.tools.sulfur_cube_icon.scale"),
                featureScaleDisplay(),
                EntryOptions.create().enabledWhen(this::sulfurCubeIconShown)
        );

        category.preview(new SimpleItemPreviewWidget(Items.SULFUR_CUBE_BUCKET));

        return category.build();
    }
    *//*?} */


    // - Misc - Potion Effects

    public final EnumValue<Options.Visibility> potionEffectHotbarVisibility = EnumValue.of(Options.Visibility.USE_GENERAL);
    public final EnumValue<Options.Visibility> potionEffectInventoryVisibility = EnumValue.of(Options.Visibility.USE_GENERAL);
    public final EnumValue<Options.EnchantmentDisplayMode> potionEffectMode = EnumValue.of(Options.EnchantmentDisplayMode.STACKED);
    public final EnumValue<Alignment.CornerIconPosition> potionEffectPosition = EnumValue.of(Alignment.CornerIconPosition.TOP_LEFT);
    public final IntValue potionEffectAmount = IntValue.of(3, 1, 5);
    public final IntValue potionEffectScale = IntValue.of(SCALE_USE_GENERAL, SCALE_USE_GENERAL, 100);
    public final BooleanValue potionEffectPotions = BooleanValue.of(true);
    public final BooleanValue potionEffectTippedArrows = BooleanValue.of(true);
    public final BooleanValue potionEffectSuspiciousStews = BooleanValue.of(true);

    private boolean potionEffectsVisible() {
        return RenderingUtil.isShownAnywhere(potionEffectHotbarVisibility, potionEffectInventoryVisibility);
    }

    private boolean potionEffectsShown() {
        return potionEffectsVisible() && (potionEffectPotions.getValue() || potionEffectTippedArrows.getValue() || potionEffectSuspiciousStews.getValue());
    }


    private ConfigCategory potionEffectCategory() {
        CategoryBuilder category = CategoryBuilder.create(Component.translatable("better_item_overlay.config.misc.potion_effect"));

        addVisibility(category, potionEffectHotbarVisibility, potionEffectInventoryVisibility, modEnabledToggle::getValue);

        category.addDropdown(potionEffectMode, Component.translatable("better_item_overlay.config.misc.potion_effect.mode"),
                EnumLabels.translated("better_item_overlay.options"),
                EntryOptions.create()
                        .labelTooltip(Component.translatable("better_item_overlay.config.misc.potion_effect.mode.tooltip"))
                        .enabledWhen(this::potionEffectsShown)
        );

        category.addDropdown(potionEffectPosition, Component.translatable("better_item_overlay.config.misc.potion_effect.position"),
                EnumLabels.translated("shwgconfig.alignment"),
                EntryOptions.create().enabledWhen(this::potionEffectsShown)
        );

        category.addNumberSlider(potionEffectAmount, Component.translatable("better_item_overlay.config.misc.potion_effect.amount"),
                NumberDisplay.raw(),
                EntryOptions.create()
                        .labelTooltip(Component.translatable("better_item_overlay.config.misc.potion_effect.amount.tooltip"))
                        .enabledWhen(() -> potionEffectsShown()
                                && potionEffectMode.getValue() != Options.EnchantmentDisplayMode.SINGLE)
        );

        category.addNumberSlider(potionEffectScale, Component.translatable("better_item_overlay.config.misc.potion_effect.scale"),
                featureScaleDisplay(),
                EntryOptions.create().enabledWhen(this::potionEffectsShown)
        );

        category.addToggle(potionEffectPotions, Component.translatable("better_item_overlay.config.misc.potion_effect.potions"),
                EntryOptions.create()
                        .labelTooltip(Component.translatable("better_item_overlay.config.misc.potion_effect.potions.tooltip"))
                        .enabledWhen(this::potionEffectsVisible)
        );

        category.addToggle(potionEffectTippedArrows, Component.translatable("better_item_overlay.config.misc.potion_effect.tipped_arrows"),
                EntryOptions.create().enabledWhen(this::potionEffectsVisible)
        );

        category.addToggle(potionEffectSuspiciousStews, Component.translatable("better_item_overlay.config.misc.potion_effect.suspicious_stews"),
                EntryOptions.create().enabledWhen(this::potionEffectsVisible)
        );

        category.preview(new SimpleItemPreviewWidget(PotionEffectFeature.previewStack()));

        return category.build();
    }


}
