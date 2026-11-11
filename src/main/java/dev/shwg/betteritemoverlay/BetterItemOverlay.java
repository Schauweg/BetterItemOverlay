package dev.shwg.betteritemoverlay;

import dev.shwg.betteritemoverlay.feature.*;
import dev.shwg.shwgconfig.api.ConfigManager;
import dev.shwg.betteritemoverlay.config.BetterItemOverlayConfig;

/*? if fabric {*/
import net.fabricmc.api.ModInitializer;
/*?}*/

/*? if forge {*/
/*import net.minecraftforge.fml.common.Mod;
/^? if 1.20.6 || >= 1.21.6  { ^/
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
/^? } ^/
*//*?}*/

/*? if neoforge {*/
/*import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.bus.api.IEventBus;
*//*?}*/

/*? if forgeLike{ */
/*import dev.shwg.shwgconfig.ShwgConfig;
@Mod(BetterItemOverlay.MOD_ID)
*//*?}*/
/*~ if fabric '{' -> 'implements ModInitializer {' */
public class BetterItemOverlay implements ModInitializer {

    public static final String MOD_ID = "better_item_overlay";
    public static final BetterItemOverlayConfig CONFIG = new BetterItemOverlayConfig();
    public static ConfigManager configManager;

    /*? if forge {*/
    /*/^~ if 1.20.6 || >= 1.21.6 '()' -> '(FMLJavaModLoadingContext context)' ^/
    public BetterItemOverlay(FMLJavaModLoadingContext context) {
        init();
        /^~ if 1.20.6 || >= 1.21.6 'null' -> 'context' ^/
        ShwgConfig.forgeInit(configManager, context);
    }
    *//*?}*/

    /*? if neoforge {*/
    /*public BetterItemOverlay(IEventBus modEventBus, ModContainer modContainer) {
        init();
        ShwgConfig.neoForgeInit(configManager, modEventBus, modContainer);
    }
    *//*?}*/

    /*? if fabric {*/
    @Override
    public void onInitialize() {
        init();
    }
    /*?}*/

    private void init(){
        configManager = ConfigManager.of(CONFIG, "better_item_overlay.json");
        configManager.load();

        OverlayRenderer.register(new PotionEffectFeature());
        OverlayRenderer.register(new EnchantmentIconFeature());
        /*? if >= 26.2 */
        //OverlayRenderer.register(new SulfurCubeBucketFeature());
        OverlayRenderer.register(new WaxedIconFeature());
        OverlayRenderer.register(new ArrowCounterFeature());
        OverlayRenderer.register(new DurabilityNumberFeature());
    }

}
