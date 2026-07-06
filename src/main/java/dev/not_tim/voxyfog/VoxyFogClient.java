package dev.not_tim.voxyfog;

import net.fabricmc.api.ClientModInitializer;

public final class VoxyFogClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        VoxyFogConfig.load();
    }
}
