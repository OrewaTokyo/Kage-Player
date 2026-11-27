package dev.anilbeesetti.nextplayer.feature.player.ui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.anilbeesetti.nextplayer.core.ui.R

private val SleepTimerOptionsMinutes = listOf(15, 30, 45, 60, 90)

/** Panel to pause playback after a chosen number of minutes. */
@Composable
fun BoxScope.SleepTimerSelectorView(
    modifier: Modifier = Modifier,
    show: Boolean,
    selectedMinutes: Int?,
    onTimerSelected: (Int?) -> Unit,
) {
    OverlayView(
        modifier = modifier,
        show = show,
        title = stringResource(R.string.shortcut_sleep_timer),
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp),
        ) {
            RadioButtonRow(
                selected = selectedMinutes == null,
                text = stringResource(R.string.sleep_timer_off),
                onClick = { onTimerSelected(null) },
            )
            SleepTimerOptionsMinutes.forEach { minutes ->
                RadioButtonRow(
                    selected = selectedMinutes == minutes,
                    text = pluralStringResource(R.plurals.sleep_timer_minutes, minutes, minutes),
                    onClick = { onTimerSelected(minutes) },
                )
            }
        }
    }
}
