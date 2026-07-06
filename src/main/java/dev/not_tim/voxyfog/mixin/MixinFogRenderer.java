package dev.not_tim.voxyfog.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.not_tim.voxyfog.VoxyCompat;
import dev.not_tim.voxyfog.VoxyFogTuning;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FogRenderer.class, priority = 900)
public abstract class MixinFogRenderer {
    @Inject(
            method = "setupFog",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;getDevice()Lcom/mojang/blaze3d/systems/GpuDevice;",
                    remap = false
            )
    )
    private void voxyfog$applyVoxyDistanceFog(
            Camera camera,
            int renderDistance,
            DeltaTracker deltaTracker,
            float partialTick,
            ClientLevel level,
            CallbackInfoReturnable<Vector4f> cir,
            @Local(type = FogData.class) FogData fogData
    ) {
        VoxyCompat.State state = VoxyCompat.getState();
        if (!state.isActive()) {
            return;
        }

        if (fogData.environmentalEnd < 16.0f) {
            return;
        }

        float vanillaDistanceBlocks = Minecraft.getInstance().gameRenderer.getRenderDistance();
        VoxyFogTuning.FogRange fogRange = VoxyFogTuning.compute(state.renderDistanceBlocks(), vanillaDistanceBlocks);
        if (fogRange == null) {
            return;
        }

        fogData.environmentalStart = Math.min(fogData.environmentalStart, fogRange.start());
        fogData.environmentalEnd = Math.min(fogData.environmentalEnd, fogRange.end());
    }
}
