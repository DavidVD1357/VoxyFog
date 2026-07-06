package dev.not_tim.voxyfog.mixin;

import dev.not_tim.voxyfog.VoxyFogConfig;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoSettingsScreen.class)
public abstract class MixinVideoSettingsScreen extends OptionsSubScreen {
    protected MixinVideoSettingsScreen(Screen lastScreen, Options options, Component title) {
        super(lastScreen, options, title);
    }

    @Inject(method = "addOptions", at = @At("TAIL"))
    private void voxyfog$addToggle(CallbackInfo ci) {
        this.addRenderableWidget(
                CycleButton.onOffBuilder(VoxyFogConfig.isEnabled())
                        .create(
                                this.width / 2 - 155,
                                this.height - 74,
                                150,
                                20,
                                Component.translatable("voxy_renderdistance_fog.option.enabled"),
                                (button, value) -> VoxyFogConfig.setEnabled(value)
                        )
        );
    }
}
