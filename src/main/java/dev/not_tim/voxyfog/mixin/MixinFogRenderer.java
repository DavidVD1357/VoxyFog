package dev.not_tim.voxyfog.mixin;

import dev.not_tim.voxyfog.VoxyCompat;
import dev.not_tim.voxyfog.VoxyFogTuning;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FogRenderer.class, priority = 900)
public abstract class MixinFogRenderer {
    @Inject(method = "setupFog", at = @At("RETURN"))
    private void voxyfog$applyVoxyDistanceFog(
            Camera camera,
            int renderDistanceInChunks,
            DeltaTracker deltaTracker,
            float darkenWorldAmount,
            ClientLevel level,
            CallbackInfoReturnable<FogData> cir
    ) {
        FogData fogData = cir.getReturnValue();
        VoxyCompat.State state = VoxyCompat.getState();
        if (!state.isActive()) {
            return;
        }

        if (fogData.environmentalEnd < 16.0f) {
            return;
        }

        float vanillaDistanceBlocks = renderDistanceInChunks * 16.0f;
        VoxyFogTuning.FogRange fogRange = VoxyFogTuning.compute(state.renderDistanceBlocks(), vanillaDistanceBlocks);
        if (fogRange == null) {
            return;
        }

        fogData.environmentalStart = Math.min(fogData.environmentalStart, fogRange.start());
        fogData.environmentalEnd = Math.min(fogData.environmentalEnd, fogRange.end());
    }
}
