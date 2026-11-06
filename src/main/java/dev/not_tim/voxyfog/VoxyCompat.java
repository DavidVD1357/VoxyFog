package dev.not_tim.voxyfog;

import java.lang.reflect.Field;

public final class VoxyCompat {
    private static final String CONFIG_CLASS_NAME = "me.cortex.voxy.client.config.VoxyConfig";

    private static boolean lookedUp;
    private static boolean available;
    private static Field configField;
    private static Field enabledField;
    private static Field renderingField;
    private static Field sectionRenderDistanceField;

    private VoxyCompat() {
    }

    public static State getState() {
        if (!ensureLookup()) {
            return State.unavailable();
        }

        try {
            Object config = configField.get(null);
            if (config == null) {
                return State.unavailable();
            }

            boolean enabled = enabledField.getBoolean(config);
            boolean rendering = renderingField.getBoolean(config);
            float sectionRenderDistance = sectionRenderDistanceField.getFloat(config);
            return new State(true, enabled, rendering, sectionRenderDistance);
        } catch (ReflectiveOperationException ignored) {
            available = false;
            return State.unavailable();
        }
    }

    private static boolean ensureLookup() {
        if (lookedUp) {
            return available;
        }
        lookedUp = true;

        try {
            Class<?> configClass = Class.forName(CONFIG_CLASS_NAME);
            configField = configClass.getField("CONFIG");
            enabledField = configClass.getField("enabled");
            renderingField = configClass.getField("enableRendering");
            sectionRenderDistanceField = configClass.getField("sectionRenderDistance");
            available = true;
        } catch (ReflectiveOperationException ignored) {
            available = false;
        }

        return available;
    }

    public static final class State {
        final boolean present;
        final boolean enabled;
        final boolean renderingEnabled;
        final float sectionRenderDistance;

        State(boolean present, boolean enabled, boolean renderingEnabled, float sectionRenderDistance) {
            this.present = present;
            this.enabled = enabled;
            this.renderingEnabled = renderingEnabled;
            this.sectionRenderDistance = sectionRenderDistance;
        }

        static State unavailable() {
            return new State(false, false, false, 0.0f);
        }

        public boolean isActive() {
            return VoxyFogConfig.isEnabled() && this.present && this.enabled && this.renderingEnabled;
        }

        public float renderDistanceBlocks() {
            return this.sectionRenderDistance * 32.0f * 16.0f;
        }
    }
}
