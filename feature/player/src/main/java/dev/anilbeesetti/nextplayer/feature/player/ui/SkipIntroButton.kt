package dev.anilbeesetti.nextplayer.feature.player.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.C
import androidx.media3.common.Player
import dev.anilbeesetti.nextplayer.core.ui.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

private const val SKIP_INTRO_MS = 85_000L
private const val WINDOW_START_MS = 15_000L
private const val WINDOW_END_MS = 300_000L
private const val MIN_DURATION_MS = 600_000L
private const val POLL_MS = 500L

/**
 * A "Skip intro" button for anime episodes. It appears during the first minutes of a long video
 * and jumps ahead by 85 seconds, the usual length of an opening.
 */
@Composable
fun SkipIntroButton(
    player: Player,
    modifier: Modifier = Modifier,
) {
    var position by remember { mutableLongStateOf(0L) }
    LaunchedEffect(player) {
        while (isActive) {
            position = player.currentPosition
            delay(POLL_MS)
        }
    }

    val duration = player.duration
    val isLongEnough = duration != C.TIME_UNSET && duration >= MIN_DURATION_MS
    if (isLongEnough && position in WINDOW_START_MS..WINDOW_END_MS) {
        FilledTonalButton(
            modifier = modifier,
            onClick = { player.seekTo((player.currentPosition + SKIP_INTRO_MS).coerceAtMost(duration)) },
        ) {
            Icon(
                imageVector = Icons.Rounded.FastForward,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Text(text = stringResource(R.string.skip_intro), modifier = Modifier.padding(start = 6.dp))
        }
    }
}
