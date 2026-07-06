package dev.not_tim.voxyfog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class VoxyFogConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("voxy-renderdistance-fog.json");

    private static State state = new State();

    private VoxyFogConfig() {
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            State loaded = GSON.fromJson(reader, State.class);
            if (loaded != null) {
                state = loaded;
            }
        } catch (IOException ignored) {
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(state, writer);
            }
        } catch (IOException ignored) {
        }
    }

    public static boolean isEnabled() {
        return state.enabled;
    }

    public static void setEnabled(boolean enabled) {
        state.enabled = enabled;
        save();
    }

    private static final class State {
        private boolean enabled = true;
    }
}
