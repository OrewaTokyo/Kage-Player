package dev.anilbeesetti.nextplayer.feature.player.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import dev.anilbeesetti.nextplayer.core.model.PlayerShortcut
import dev.anilbeesetti.nextplayer.core.ui.R
import dev.anilbeesetti.nextplayer.core.ui.components.NextSwitch

/**
 * Panel to switch the whole shortcuts row on or off and to tick the buttons it contains.
 */
@Composable
fun BoxScope.ShortcutsSelectorView(
    modifier: Modifier = Modifier,
    show: Boolean,
    shortcutsVisible: Boolean,
    enabledShortcuts: Set<PlayerShortcut>,
    onShortcutsVisibleChange: (Boolean) -> Unit,
    onShortcutEnabledChange: (PlayerShortcut, Boolean) -> Unit,
) {
    OverlayView(
        modifier = modifier,
        show = show,
        title = stringResource(R.string.shortcuts),
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .toggleable(
                        value = shortcutsVisible,
                        role = Role.Switch,
                        onValueChange = onShortcutsVisibleChange,
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.show_shortcuts),
                    style = MaterialTheme.typography.titleMedium,
                )
                NextSwitch(checked = shortcutsVisible, onCheckedChange = null)
            }
            HorizontalDivider()
            Spacer(modifier = Modifier.padding(top = 8.dp))
            PlayerShortcut.entries.chunked(2).forEach { pair ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    pair.forEach { shortcut ->
                        ShortcutCheckboxRow(
                            modifier = Modifier.weight(1f),
                            text = stringResource(shortcut.labelRes()),
                            checked = shortcut in enabledShortcuts,
                            onCheckedChange = { onShortcutEnabledChange(shortcut, it) },
                        )
                    }
                    if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ShortcutCheckboxRow(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .toggleable(
                value = checked,
                role = Role.Checkbox,
                onValueChange = onCheckedChange,
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Checkbox(checked = checked, onCheckedChange = null)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

private fun PlayerShortcut.labelRes(): Int = when (this) {
    PlayerShortcut.LOCK -> R.string.shortcut_lock
    PlayerShortcut.SCREEN_SCALE -> R.string.shortcut_screen_scale
    PlayerShortcut.PICTURE_IN_PICTURE -> R.string.shortcut_pip
    PlayerShortcut.SCREEN_ROTATION -> R.string.shortcut_screen_rotation
    PlayerShortcut.BACKGROUND_PLAY -> R.string.shortcut_background_play
    PlayerShortcut.PLAYBACK_SPEED -> R.string.shortcut_playback_speed
    PlayerShortcut.LOOP -> R.string.shortcut_loop
    PlayerShortcut.SHUFFLE -> R.string.shortcut_shuffle
    PlayerShortcut.MUTE -> R.string.shortcut_mute
    PlayerShortcut.AUDIO_EFFECT -> R.string.shortcut_audio_effect
    PlayerShortcut.SLEEP_TIMER -> R.string.shortcut_sleep_timer
    PlayerShortcut.A_B_REPEAT -> R.string.shortcut_ab_repeat
    PlayerShortcut.NIGHT_MODE -> R.string.shortcut_night_mode
    PlayerShortcut.MIRROR_MODE -> R.string.shortcut_mirror_mode
    PlayerShortcut.VERTICAL_FLIP -> R.string.shortcut_vertical_flip
}
