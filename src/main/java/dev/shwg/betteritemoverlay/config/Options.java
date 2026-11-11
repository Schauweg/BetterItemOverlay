package dev.shwg.betteritemoverlay.config;

import org.jetbrains.annotations.Nullable;

public final class Options {

    public enum GeneralVisibility { ALWAYS, NEVER, ON_KEYBIND }

    public enum Visibility {
        USE_GENERAL, ALWAYS, NEVER, ON_KEYBIND;

        public @Nullable GeneralVisibility asGeneral() {
            return switch (this) {
                case ALWAYS -> GeneralVisibility.ALWAYS;
                case NEVER -> GeneralVisibility.NEVER;
                case ON_KEYBIND -> GeneralVisibility.ON_KEYBIND;
                case USE_GENERAL -> null;
            };
        }
    }

    public enum DamageDisplayMode {
        ALWAYS,
        NEVER,
        DAMAGED
    }

    public enum EnchantmentDisplayMode {
        SINGLE,
        STACKED,
        SIDE_BY_SIDE
    }

}
