package dev.shwg.betteritemoverlay.feature;

import dev.shwg.betteritemoverlay.BetterItemOverlay;
import dev.shwg.betteritemoverlay.config.BetterItemOverlayConfig;
import dev.shwg.betteritemoverlay.util.RenderingUtil;
import dev.shwg.shwgconfig.render.GenericGraphics;
import dev.shwg.shwgconfig.render.textures.GenericTexture;
import dev.shwg.shwgconfig.render.textures.MobEffectTextures;
import dev.shwg.shwgconfig.util.DisplayItemStack;
import net.minecraft.client.gui.Font;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
/*? if >= 1.20.5 { */
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.SuspiciousStewEffects;
/*? } else { */
/*import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.alchemy.PotionUtils;
*//*? } */
/*? if >= 1.20.2 && < 1.20.5 { */
//import dev.shwg.shwgconfig.util.RegistryHelper;
/*? } */

/*? if >= 1.19.3 { */
import net.minecraft.core.registries.BuiltInRegistries;
/*? } else { */
//import net.minecraft.core.Registry;
/*? } */

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PotionEffectFeature implements OverlayFeature {

    private static List<MobEffect> previewEffects;

    @Override
    public void render(Font font, GenericGraphics graphics, ItemStack itemStack, int x, int y) {
        BetterItemOverlayConfig config = BetterItemOverlay.CONFIG;
        if (!RenderingUtil.isVisible(config.potionEffectHotbarVisibility, config.potionEffectInventoryVisibility)) return;

        List<MobEffect> effects = getEffects(itemStack);
        if (effects.isEmpty()) return;

        RenderingUtil.renderIconGroup(graphics, itemStack, effects.size(),
                config.potionEffectMode.getValue(),
                config.potionEffectPosition.getValue(),
                config.potionEffectAmount.getValue(),
                RenderingUtil.resolveScale(config.potionEffectScale),
                i -> {
                    GenericTexture texture = MobEffectTextures.get(effects.get(i));
                    if (texture == null) return;
                    graphics.pushPose();
                    RenderingUtil.applyOverlayZ(graphics, false);
                    graphics.drawTexture(texture, 0, 0, RenderingUtil.DEFAULT_ICON_SIZE, RenderingUtil.DEFAULT_ICON_SIZE);
                    graphics.popPose();
                });
    }

    public static List<MobEffect> getEffects(ItemStack itemStack) {
        BetterItemOverlayConfig config = BetterItemOverlay.CONFIG;
        boolean potionContentsEnabled = itemStack.is(Items.TIPPED_ARROW)
                ? config.potionEffectTippedArrows.getValue()
                : config.potionEffectPotions.getValue();
        boolean stewEnabled = config.potionEffectSuspiciousStews.getValue();
        if (!potionContentsEnabled && !stewEnabled) return List.of();

        Set<MobEffect> effects = new LinkedHashSet<>();

        /*? if >= 1.20.5 { */
        PotionContents potion = potionContentsEnabled ? itemStack.get(DataComponents.POTION_CONTENTS) : null;
        if (potion != null) {
            for (MobEffectInstance instance : potion.getAllEffects()) {
                effects.add(instance.getEffect().value());
            }
        }
        SuspiciousStewEffects stew = stewEnabled ? itemStack.get(DataComponents.SUSPICIOUS_STEW_EFFECTS) : null;
        if (stew != null) {
            for (SuspiciousStewEffects.Entry entry : stew.effects()) {
                effects.add(entry.effect().value());
            }
        }
        /*? } else { */
        /*if (potionContentsEnabled) {
            for (MobEffectInstance instance : PotionUtils.getMobEffects(itemStack)) {
                effects.add(instance.getEffect());
            }
        }
        if (stewEnabled && itemStack.is(Items.SUSPICIOUS_STEW)) addStewEffects(itemStack, effects);
        *//*? } */

        if (effects.isEmpty() && potionContentsEnabled && itemStack.is(Items.POTION) && RenderingUtil.isConfigPreview()) {
            return previewEffects();
        }

        return effects.isEmpty() ? List.of() : new ArrayList<>(effects);
    }

    private static List<MobEffect> previewEffects() {
        if (previewEffects == null) {
            List<MobEffect> found = new ArrayList<>();
            for (String path : List.of("slowness", "resistance")) {
                MobEffect effect = effectByPath(path);
                if (effect != null) found.add(effect);
            }
            previewEffects = List.copyOf(found);
        }
        return previewEffects;
    }

    private static MobEffect effectByPath(String path) {
        /*? if >= 1.19.3 { */
        for (MobEffect effect : BuiltInRegistries.MOB_EFFECT) {
            /*? } else { */
            //for (MobEffect effect : Registry.MOB_EFFECT) {
            /*? } */
            var id = MobEffectTextures.effectId(effect);
            if (id != null && id.getNamespace().equals("minecraft") && id.getPath().equals(path)) return effect;
        }
        return null;
    }

    /*? if < 1.20.5 { */
    /*private static void addStewEffects(ItemStack itemStack, Set<MobEffect> effects) {
        CompoundTag tag = itemStack.getTag();
        if (tag == null) return;
        /^? if >= 1.20.2 { ^/
        ListTag list = tag.getList("effects", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            var id = RegistryHelper.tryBuild(list.getCompound(i).getString("id"));
            if (id == null) continue;
            MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(id);
            if (effect != null) effects.add(effect);
        }
        /^? } else { ^/
        /^ListTag list = tag.getList("Effects", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            MobEffect effect = MobEffect.byId(list.getCompound(i).getInt("EffectId"));
            if (effect != null) effects.add(effect);
        }
        ^//^? } ^/
    }
    *//*? } */

    public static ItemStack previewStack() {
        return DisplayItemStack.getStackSimple(Items.POTION);
    }
}
