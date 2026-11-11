package dev.shwg.betteritemoverlay.util;

import dev.shwg.shwgconfig.util.DisplayItemStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.IdentityHashMap;
import java.util.Map;

/*? if >= 1.21 {*/
import net.minecraft.core.registries.Registries;
/*?}*/

public final class AmmoResolver {

    public record AmmoInfo(ItemStack projectile, int count, boolean infinite) {
        public static final AmmoInfo NONE = new AmmoInfo(ItemStack.EMPTY, 0, false);
        private static AmmoInfo preview;

        public static AmmoInfo preview() {
            if (preview == null) {
                preview = new AmmoInfo(DisplayItemStack.getStackSimple(Items.ARROW), 69, false);
            }
            return preview;
        }

        public boolean isEmpty() { return projectile.isEmpty(); }
    }

    private record Resolved(ItemStack projectile, int count) {
        static final Resolved NONE = new Resolved(ItemStack.EMPTY, 0);
    }

    private static final Map<Item, Resolved> CACHE = new IdentityHashMap<>();
    private static long cachedTick = Long.MIN_VALUE;
    private static Player cachedPlayer;

    public static AmmoInfo resolve(Player player, ItemStack weapon) {
        long tick = /*? if >=1.20 {*/ player.level() /*?} else {*/ /*player.level *//*?}*/ .getGameTime();
        if (tick != cachedTick || player != cachedPlayer) {
            CACHE.clear();
            cachedTick = tick;
            cachedPlayer = player;
        }

        Resolved r = CACHE.computeIfAbsent(weapon.getItem(), i -> resolveUncached(player, weapon));
        if (r.projectile().isEmpty()) return AmmoInfo.NONE;

        boolean infinite = player.getAbilities().instabuild
                || (r.projectile().is(Items.ARROW) && hasInfinity(player, weapon));

        return new AmmoInfo(r.projectile(), r.count(), infinite);
    }

    private static Resolved resolveUncached(Player player, ItemStack weapon) {
        ItemStack projectile = player.getProjectile(weapon);
        if (projectile.isEmpty()) return Resolved.NONE;

        int count = 0;
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack s = inv.getItem(i);
            if (!s.isEmpty() && isSameAmmo(s, projectile)) count += s.getCount();
        }
        /*~ if >= 1.20 '.copy()' -> '.copyWithCount(1)'*/
        return new Resolved(projectile.copyWithCount(1), count);
    }


    private static boolean isSameAmmo(ItemStack a, ItemStack b) {
        /*? if >= 1.20.5 {*/
        return ItemStack.isSameItemSameComponents(a, b);
        /*?} else {*/
        //return ItemStack.isSameItemSameTags(a, b);
        /*?}*/
    }

    private static boolean hasInfinity(Player player, ItemStack weapon) {
        /*? if >= 1.21.2 {*/
        var infinity = player.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.INFINITY);
        return EnchantmentHelper.getItemEnchantmentLevel(infinity, weapon) > 0;
        /*?} else if >= 1.21 {*/
        /*var infinity = player.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(Enchantments.INFINITY);
        return EnchantmentHelper.getItemEnchantmentLevel(infinity, weapon) > 0;
        *//*?} else {*/
        //return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY, weapon) > 0;
        /*?}*/
    }
}
