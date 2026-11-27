package dev.anilbeesetti.nextplayer.feature.player.state

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.media3.common.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

private const val AB_REPEAT_POLL_MS = 150L
private const val AB_REPEAT_MIN_GAP_MS = 500L

/** Per-viewing toggles of the player shortcuts. They reset when the player is closed. */
@Stable
class PlayerEffectsState {
    var isMirrored by mutableStateOf(false)
    var isFlipped by mutableStateOf(false)
    var isNightMode by mutableStateOf(false)
    var isMuted by mutableStateOf(false)

    var abRepeatStartMs by mutableStateOf<Long?>(null)
        private set
    var abRepeatEndMs by mutableStateOf<Long?>(null)
        private set

    /** Chosen sleep timer length in minutes, or null when no timer is running. */
    var sleepTimerMinutes by mutableStateOf<Int?>(null)
        private set
    var sleepTimerEndsAtMs by mutableStateOf<Long?>(null)
        private set

    val isAbRepeatActive: Boolean
        get() = abRepeatStartMs != null

    /** Tap 1 sets point A, tap 2 sets point B and starts looping, tap 3 clears it. */
    fun cycleAbRepeat(positionMs: Long) {
        val start = abRepeatStartMs
        val end = abRepeatEndMs
        when {
            start == null -> abRepeatStartMs = positionMs

            end == null -> if (positionMs - start >= AB_REPEAT_MIN_GAP_MS) {
                abRepeatEndMs = positionMs
            } else {
                abRepeatStartMs = positionMs
            }

            else -> clearAbRepeat()
        }
    }

    fun clearAbRepeat() {
        abRepeatStartMs = null
        abRepeatEndMs = null
    }

    fun startSleepTimer(minutes: Int) {
        sleepTimerMinutes = minutes
        sleepTimerEndsAtMs = SystemClock.elapsedRealtime() + minutes * 60_000L
    }

    fun cancelSleepTimer() {
        sleepTimerMinutes = null
        sleepTimerEndsAtMs = null
    }
}

@Composable
fun rememberPlayerEffectsState(): PlayerEffectsState = remember { PlayerEffectsState() }

/** Applies the toggles of [state] to [player]. Call it once next to the player UI. */
@Composable
fun PlayerEffectsHandler(player: Player, state: PlayerEffectsState, mediaId: String?) {
    // A-B points belong to one video.
    LaunchedEffect(mediaId) { state.clearAbRepeat() }

    LaunchedEffect(player, state.abRepeatStartMs, state.abRepeatEndMs) {
        val start = state.abRepeatStartMs ?: return@LaunchedEffect
        val end = state.abRepeatEndMs ?: return@LaunchedEffect
        while (isActive) {
            if (player.currentPosition >= end) player.seekTo(start)
            delay(AB_REPEAT_POLL_MS)
        }
    }

    LaunchedEffect(player, state.sleepTimerEndsAtMs) {
        val endsAt = state.sleepTimerEndsAtMs ?: return@LaunchedEffect
        val remaining = endsAt - SystemClock.elapsedRealtime()
        if (remaining > 0) delay(remaining)
        player.pause()
        state.cancelSleepTimer()
    }

    LaunchedEffect(player, state.isMuted) {
        if (state.isMuted) {
            player.volume = 0f
        } else if (player.volume == 0f) {
            player.volume = 1f
        }
    }
}
