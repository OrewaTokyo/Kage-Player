package dev.anilbeesetti.nextplayer.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.anilbeesetti.nextplayer.core.ui.R

private data class TermsSection(@StringRes val title: Int, @StringRes val body: Int)

private val termsSections = listOf(
    TermsSection(R.string.terms_videos_title, R.string.terms_videos_body),
    TermsSection(R.string.terms_privacy_title, R.string.terms_privacy_body),
    TermsSection(R.string.terms_license_title, R.string.terms_license_body),
    TermsSection(R.string.terms_warranty_title, R.string.terms_warranty_body),
    TermsSection(R.string.terms_changes_title, R.string.terms_changes_body),
    TermsSection(R.string.terms_choice_title, R.string.terms_choice_body),
)

/** The text of the Terms & Conditions. Shared by the first-launch screen and the About screen. */
@Composable
fun TermsContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.terms_intro),
            style = MaterialTheme.typography.bodyMedium,
        )
        termsSections.forEach { section ->
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(section.title),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = stringResource(section.body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
