package tmg.flashback.releasenotes.ui.releasenotes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tmg.flashback.releasenotes.R
import tmg.flashback.releasenotes.constants.ReleaseNotes
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextHeadline2
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.header.Header

@Composable
fun ReleaseScreenVM(
    actionUpClicked: () -> Unit
) {
    ScreenView(screenName = "Release Notes")

    Column(
        modifier = Modifier
            .background(AppTheme.colors.backgroundPrimary)
            .verticalScroll(rememberScrollState())
    ) {
        Header(
            text = stringResource(id = R.string.release_notes_title),
            icon = painterResource(id = R.drawable.ic_back),
            iconContentDescription = stringResource(id = R.string.ab_back),
            actionUpClicked = actionUpClicked
        )
        ReleaseNotes.values()
            .sortedByDescending { it.version }
            .forEach {
                ReleaseNote(
                    title = "${it.versionName} ${stringResource(id = it.title)}",
                    subtitle = stringResource(id = it.release)
                )
            }
    }
}

@Composable
private fun ReleaseNote(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(
            start = AppTheme.dimens.medium,
            end = AppTheme.dimens.medium,
            top = AppTheme.dimens.small,
            bottom = AppTheme.dimens.small
        )
    ) {
        TextHeadline2(text = title)
        Spacer(modifier = Modifier.height(4.dp))
        TextBody1(text = subtitle)
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        ReleaseScreenVM(
            actionUpClicked = {}
        )
    }
}
