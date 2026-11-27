package dev.anilbeesetti.nextplayer.feature.player.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.Player
import androidx.media3.extractor.metadata.Chapter
import dev.anilbeesetti.nextplayer.core.model.PlayerShortcut
import dev.anilbeesetti.nextplayer.core.model.VideoContentScale
import dev.anilbeesetti.nextplayer.feature.player.extensions.noRippleClickable
import dev.anilbeesetti.nextplayer.feature.player.state.SubtitleOptionsEvent
import io.github.anilbeesetti.nextlib.media3ext.ffdecoder.DecoderMode

@Composable
fun BoxScope.OverlayShowView(
    player: Player,
    overlayView: OverlayView?,
    videoDecoderMode: DecoderMode?,
    audioDecoderMode: DecoderMode?,
    videoContentScale: VideoContentScale,
    chapters: List<Chapter>,
    currentChapterIndex: Int,
    onChapterSelected: (Chapter) -> Unit,
    onDismiss: () -> Unit = {},
    onVideoDecoderModeSelected: (DecoderMode) -> Unit = {},
    onAudioDecoderModeSelected: (DecoderMode) -> Unit = {},
    onSelectSubtitleClick: () -> Unit = {},
    onSelectAudioClick: () -> Unit = {},
    onSubtitleOptionEvent: (SubtitleOptionsEvent) -> Unit = {},
    onVideoContentScaleChanged: (VideoContentScale) -> Unit = {},
    shortcutsVisible: Boolean = true,
    enabledShortcuts: Set<PlayerShortcut> = PlayerShortcut.defaults,
    onShortcutsVisibleChange: (Boolean) -> Unit = {},
    onShortcutEnabledChange: (PlayerShortcut, Boolean) -> Unit = { _, _ -> },
    sleepTimerMinutes: Int? = null,
    onSleepTimerSelected: (Int?) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .matchParentSize()
            .then(
                other = if (overlayView != null) {
                    Modifier.noRippleClickable(onClick = onDismiss)
                } else {
                    Modifier
                },
            ),
    )

    AudioTrackSelectorView(
        show = overlayView == OverlayView.AUDIO_SELECTOR,
        player = player,
        onSelectAudioClick = onSelectAudioClick,
        onDismiss = onDismiss,
    )

    DecoderSelectorView(
        show = overlayView == OverlayView.DECODER_SELECTOR,
        videoMode = videoDecoderMode,
        audioMode = audioDecoderMode,
        onVideoModeSelected = onVideoDecoderModeSelected,
        onAudioModeSelected = onAudioDecoderModeSelected,
    )

    SubtitleSelectorView(
        show = overlayView == OverlayView.SUBTITLE_SELECTOR,
        player = player,
        onSelectSubtitleClick = onSelectSubtitleClick,
        onEvent = onSubtitleOptionEvent,
        onDismiss = onDismiss,
    )

    PlaybackSpeedSelectorView(
        show = overlayView == OverlayView.PLAYBACK_SPEED,
        player = player,
    )

    VideoContentScaleSelectorView(
        show = overlayView == OverlayView.VIDEO_CONTENT_SCALE,
        videoContentScale = videoContentScale,
        onVideoContentScaleChanged = onVideoContentScaleChanged,
        onDismiss = onDismiss,
    )

    PlaylistView(
        show = overlayView == OverlayView.PLAYLIST,
        player = player,
    )

    ChaptersView(
        show = overlayView == OverlayView.CHAPTERS,
        chapters = chapters,
        currentChapterIndex = currentChapterIndex,
        onChapterSelected = onChapterSelected,
    )

    ShortcutsSelectorView(
        show = overlayView == OverlayView.SHORTCUTS,
        shortcutsVisible = shortcutsVisible,
        enabledShortcuts = enabledShortcuts,
        onShortcutsVisibleChange = onShortcutsVisibleChange,
        onShortcutEnabledChange = onShortcutEnabledChange,
    )

    SleepTimerSelectorView(
        show = overlayView == OverlayView.SLEEP_TIMER,
        selectedMinutes = sleepTimerMinutes,
        onTimerSelected = {
            onSleepTimerSelected(it)
            onDismiss()
        },
    )
}

val Configuration.isPortrait: Boolean
    get() = orientation == Configuration.ORIENTATION_PORTRAIT

enum class OverlayView {
    DECODER_SELECTOR,
    AUDIO_SELECTOR,
    SUBTITLE_SELECTOR,
    PLAYBACK_SPEED,
    VIDEO_CONTENT_SCALE,
    PLAYLIST,
    CHAPTERS,
    SHORTCUTS,
    SLEEP_TIMER,
}
