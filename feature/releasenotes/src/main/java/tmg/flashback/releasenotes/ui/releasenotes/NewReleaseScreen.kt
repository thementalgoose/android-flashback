package tmg.flashback.releasenotes.ui.releasenotes

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tmg.flashback.releasenotes.R
import tmg.flashback.releasenotes.constants.ReleaseNotes
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.layouts.BottomSheet

@Composable
fun NewReleaseScreen(
    releaseNotes: List<ReleaseNotes>
) {
    ScreenView(screenName = "Release notes popup")

    BottomSheet(
        title = stringResource(id = R.string.release_notes_title),
        subtitle = stringResource(id = R.string.release_notes_description),
        content = {
            releaseNotes.forEach { releaseNote ->
                val title = stringResource(id = releaseNote.title)
                val version = stringResource(id = R.string.release_notes_version, releaseNote.versionName)
                val header = when (title.isBlank()) {
                    true -> version
                    false -> "$version - $title"
                }
                TextBody1(
                    modifier = Modifier.padding(
                        start = AppTheme.dimens.medium,
                        end = AppTheme.dimens.medium,
                        top = AppTheme.dimens.small,
                        bottom = AppTheme.dimens.xxsmall
                    ),
                    text = header
                )
                TextBody2(
                    modifier = Modifier.padding(
                        start = AppTheme.dimens.medium,
                        end = AppTheme.dimens.medium,
                        bottom = AppTheme.dimens.small,
                        top = AppTheme.dimens.xxsmall
                    ),
                    text = stringResource(id = releaseNote.release)
                )
            }
        }
    )
}