package dev.anilbeesetti.nextplayer.core.model

import kotlinx.serialization.Serializable

/**
 * Buttons that can appear in the shortcuts row under the seekbar of the video player.
 * The order of the entries is the order the buttons are drawn in.
 */
@Serializable
enum class PlayerShortcut {
    LOCK,
    SCREEN_SCALE,
    PICTURE_IN_PICTURE,
    SCREEN_ROTATION,
    BACKGROUND_PLAY,
    PLAYBACK_SPEED,
    LOOP,
    SHUFFLE,
    MUTE,
    AUDIO_EFFECT,
    SLEEP_TIMER,
    A_B_REPEAT,
    NIGHT_MODE,
    MIRROR_MODE,
    VERTICAL_FLIP,
    SKIP_INTRO,
    ;

    companion object {
        /** The buttons the player showed before shortcuts became customisable. */
        val defaults: Set<PlayerShortcut> = setOf(
            LOCK,
            SCREEN_SCALE,
            PICTURE_IN_PICTURE,
            SCREEN_ROTATION,
            BACKGROUND_PLAY,
            PLAYBACK_SPEED,
            LOOP,
            SHUFFLE,
            SKIP_INTRO,
        )
    }
}
