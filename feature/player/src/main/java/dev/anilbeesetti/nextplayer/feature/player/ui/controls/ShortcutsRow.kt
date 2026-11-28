package dev.anilbeesetti.nextplayer.feature.player.ui.controls

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeOff
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.Equalizer
import androidx.compose.material.icons.rounded.Flip
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import dev.anilbeesetti.nextplayer.core.model.PlayerShortcut
import dev.anilbeesetti.nextplayer.core.model.VideoContentScale
import dev.anilbeesetti.nextplayer.core.ui.R
import dev.anilbeesetti.nextplayer.feature.player.buttons.LoopButton
import dev.anilbeesetti.nextplayer.feature.player.buttons.PlaybackSpeedButton
import dev.anilbeesetti.nextplayer.feature.player.buttons.PlayerButton
import dev.anilbeesetti.nextplayer.feature.player.buttons.RotateButton
import dev.anilbeesetti.nextplayer.feature.player.buttons.ShuffleButton
import dev.anilbeesetti.nextplayer.feature.player.extensions.drawableRes
import dev.anilbeesetti.nextplayer.feature.player.state.PlayerEffectsState

/**
 * The single row of shortcut buttons under the seekbar.
 *
 * Only the shortcuts the user ticked are drawn, in the fixed order of [PlayerShortcut].
 * When [shortcutsVisible] is off, only the customise button remains so the row can be
 * switched back on.
 */
@Composable
fun ShortcutsRow(
    modifier: Modifier = Modifier,
    player: Player?,
    shortcutsVisible: Boolean,
    enabledShortcuts: Set<PlayerShortcut>,
    effects: PlayerEffectsState,
    controlsAlignment: Alignment.Horizontal,
    videoContentScale: VideoContentScale,
    isPipSupported: Boolean,
    isTv: Boolean,
    onLockClick: () -> Unit,
    onVideoContentScaleClick: () -> Unit,
    onVideoContentScaleLongClick: () -> Unit,
    onPictureInPictureClick: () -> Unit,
    onPlayInBackgroundClick: () -> Unit,
    onPlaybackSpeedClick: () -> Unit,
    onAudioEffectClick: () -> Unit,
    onSleepTimerClick: () -> Unit,
    onAbRepeatClick: () -> Unit,
    onCustomiseClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(state = rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = controlsAlignment),
    ) {
        if (shortcutsVisible) {
            for (shortcut in PlayerShortcut.entries.filter { it in enabledShortcuts }) {
                when (shortcut) {
                    PlayerShortcut.LOCK -> PlayerButton(onClick = onLockClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_lock_open),
                            contentDescription = null,
                        )
                    }

                    PlayerShortcut.SCREEN_SCALE -> PlayerButton(
                        onClick = onVideoContentScaleClick,
                        onLongClick = onVideoContentScaleLongClick,
                    ) {
                        Icon(
                            painter = painterResource(videoContentScale.drawableRes()),
                            contentDescription = null,
                        )
                    }

                    PlayerShortcut.PICTURE_IN_PICTURE -> if (isPipSupported) {
                        PlayerButton(onClick = onPictureInPictureClick) {
                            Icon(
                                painter = painterResource(R.drawable.ic_pip),
                                contentDescription = null,
                            )
                        }
                    }

                    PlayerShortcut.SCREEN_ROTATION -> if (!isTv) RotateButton()

                    PlayerShortcut.BACKGROUND_PLAY -> PlayerButton(onClick = onPlayInBackgroundClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_headset),
                            contentDescription = null,
                        )
                    }

                    PlayerShortcut.PLAYBACK_SPEED -> PlaybackSpeedButton(
                        player = player,
                        onClick = onPlaybackSpeedClick,
                    )

                    PlayerShortcut.LOOP -> LoopButton(player = player)

                    PlayerShortcut.SHUFFLE -> ShuffleButton(player = player)

                    PlayerShortcut.MUTE -> PlayerButton(onClick = { effects.isMuted = !effects.isMuted }) {
                        Icon(
                            imageVector = if (effects.isMuted) {
                                Icons.AutoMirrored.Rounded.VolumeOff
                            } else {
                                Icons.AutoMirrored.Rounded.VolumeUp
                            },
                            contentDescription = stringResource(R.string.shortcut_mute),
                        )
                    }

                    PlayerShortcut.AUDIO_EFFECT -> PlayerButton(onClick = onAudioEffectClick) {
                        Icon(
                            imageVector = Icons.Rounded.Equalizer,
                            contentDescription = stringResource(R.string.shortcut_audio_effect),
                        )
                    }

                    PlayerShortcut.SLEEP_TIMER -> PlayerButton(onClick = onSleepTimerClick) {
                        Icon(
                            imageVector = Icons.Rounded.Bedtime,
                            contentDescription = stringResource(R.string.shortcut_sleep_timer),
                            tint = if (effects.sleepTimerMinutes != null) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.White
                            },
                        )
                    }

                    PlayerShortcut.A_B_REPEAT -> PlayerButton(onClick = onAbRepeatClick) {
                        Text(
                            text = when {
                                effects.abRepeatStartMs == null -> "A-B"
                                effects.abRepeatEndMs == null -> "A-"
                                else -> "A-B"
                            },
                            style = MaterialTheme.typography.labelLarge,
                            color = if (effects.isAbRepeatActive) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.White
                            },
                        )
                    }

                    PlayerShortcut.NIGHT_MODE -> PlayerButton(
                        onClick = { effects.isNightMode = !effects.isNightMode },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.NightsStay,
                            contentDescription = stringResource(R.string.shortcut_night_mode),
                            tint = if (effects.isNightMode) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.White
                            },
                        )
                    }

                    PlayerShortcut.MIRROR_MODE -> PlayerButton(
                        onClick = { effects.isMirrored = !effects.isMirrored },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Flip,
                            contentDescription = stringResource(R.string.shortcut_mirror_mode),
                            tint = if (effects.isMirrored) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.White
                            },
                        )
                    }

                    // Shown as its own button over the video, not in this row.
                    PlayerShortcut.SKIP_INTRO -> Unit

                    PlayerShortcut.VERTICAL_FLIP -> PlayerButton(
                        onClick = { effects.isFlipped = !effects.isFlipped },
                    ) {
                        Icon(
                            modifier = Modifier.rotate(90f),
                            imageVector = Icons.Rounded.Flip,
                            contentDescription = stringResource(R.string.shortcut_vertical_flip),
                            tint = if (effects.isFlipped) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.White
                            },
                        )
                    }
                }
            }
        }
        PlayerButton(onClick = onCustomiseClick) {
            Icon(
                imageVector = Icons.Rounded.Tune,
                contentDescription = stringResource(R.string.shortcuts),
            )
        }
    }
}
