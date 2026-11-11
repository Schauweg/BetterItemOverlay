package dev.shwg.betteritemoverlay.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.shwg.shwgconfig.api.values.ListValue;
import dev.shwg.shwgconfig.util.RegistryHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*? if >= 1.21 { */
import net.minecraft.resources.ResourceKey;
/*? } */

/**
 * The enchantment icon list. The order matters: the first entry is drawn on top.
 *
 * <p>Saved as a JSON object in list order, enchantment ID → item ID:</p>
 * <pre>{@code
 * "enchantmentIcons": {
 *   "minecraft:fortune": "minecraft:diamond",
 *   "minecraft:silk_touch": "minecraft:glass"
 * }
 * }</pre>
 *
 * <p>Each enchantment can appear only once - a list with a duplicate enchantment is rejected.</p>
 */
public class EnchantmentIconsValue extends ListValue<EnchantmentIconsValue.EnchantmentIcon> {

    public record EnchantmentIcon(Identifier enchantment, Identifier item) {

        /*? if >= 1.21 { */
        public static EnchantmentIcon of(ResourceKey<Enchantment> enchantment, Item item) {
            return new EnchantmentIcon(RegistryHelper.getID(enchantment), RegistryHelper.getID(item));
        }
        /*? } else { */
        /*public static EnchantmentIcon of(Enchantment enchantment, Item item) {
            return new EnchantmentIcon(RegistryHelper.getID(enchantment), RegistryHelper.getID(item));
        }
        *//*? } */
    }

    public static EnchantmentIconsValue of(EnchantmentIcon... defaults) {
        return new EnchantmentIconsValue(List.of(defaults));
    }

    public EnchantmentIconsValue(List<EnchantmentIcon> defaults) {
        super(defaults);
    }

    @Override
    protected boolean isValid(List<EnchantmentIcon> icons) {
        if (!super.isValid(icons)) return false;
        Set<Identifier> enchantments = new HashSet<>();
        for (EnchantmentIcon icon : icons) {
            if (!enchantments.add(icon.enchantment())) return false; // same enchantment twice
        }
        return true;
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject json = new JsonObject();
        for (EnchantmentIcon icon : value) {
            json.addProperty(icon.enchantment().toString(), icon.item().toString());
        }
        return json;
    }

    @Override
    public void loadFromJson(JsonElement element) {
        if (!element.isJsonObject()) return;

        List<EnchantmentIcon> icons = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
            if (!entry.getValue().isJsonPrimitive()) continue;
            Identifier enchantment = RegistryHelper.tryBuild(renamed(entry.getKey()));
            Identifier item = RegistryHelper.tryBuild(entry.getValue().getAsString());
            if (enchantment != null && item != null) {
                icons.add(new EnchantmentIcon(enchantment, item));
            }
        }
        setValue(icons);
    }

    private static String renamed(String enchantment) {
        /*? if >= 1.20.5 { */
        if (enchantment.equals("minecraft:sweeping")) return "minecraft:sweeping_edge";
        /*? } */
        return enchantment;
    }

}
