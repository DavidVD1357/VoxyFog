package dev.not_tim.voxyfog;

public final class VoxyFogTuning {
    private static final float MIN_OVERRIDE_DISTANCE = 48.0f;
    private static final float MIN_FOG_BAND = 64.0f;
    private static final float START_RATIO = 0.82f;
    private static final float END_RATIO = 0.98f;

    private VoxyFogTuning() {
    }

    public static FogRange compute(float voxyDistanceBlocks, float vanillaDistanceBlocks) {
        if (voxyDistanceBlocks <= vanillaDistanceBlocks + MIN_OVERRIDE_DISTANCE) {
            return null;
        }

        float fogEnd = Math.max(voxyDistanceBlocks * END_RATIO, vanillaDistanceBlocks + 32.0f);
        float fogStart = Math.max(voxyDistanceBlocks * START_RATIO, vanillaDistanceBlocks + 16.0f);

        if (fogEnd - fogStart < MIN_FOG_BAND) {
            fogStart = fogEnd - MIN_FOG_BAND;
        }

        if (fogStart < vanillaDistanceBlocks + 16.0f) {
            fogStart = vanillaDistanceBlocks + 16.0f;
        }

        if (fogEnd <= fogStart + 4.0f) {
            fogEnd = fogStart + 4.0f;
        }

        return new FogRange(fogStart, fogEnd);
    }

    public record FogRange(float start, float end) {
    }
}
