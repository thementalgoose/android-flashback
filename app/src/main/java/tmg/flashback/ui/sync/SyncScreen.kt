package tmg.flashback.ui.sync

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.text.TextBody2

@Composable
fun SyncScreen(
    showLoading: Boolean,
    tryAgainClicked: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(AppTheme.colors.backgroundSplash)
    ) {
        Text(
            style = AppTheme.typography.h1.copy(
                color = White
            ),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.splash_sync_info),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium,
                    top = AppTheme.dimensions.paddingLarge,
                    bottom = AppTheme.dimensions.paddingMedium
                )
        )
        Spacer(modifier = Modifier.weight(1f))

        if (showLoading) {
            LinearProgressIndicator(
                color = White,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            TextBody2(
                textColor = White,

                text = stringResource(id = R.string.splash_sync_required_failed),
                modifier = Modifier.padding(
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium,
                    top = AppTheme.dimensions.paddingMedium,
                    bottom = AppTheme.dimensions.paddingMedium
                )
            )
            ButtonSecondary(
                text = stringResource(id = R.string.splash_sync_try_again),
                onClick = tryAgainClicked,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingMedium))
    }
}

@Preview
@Composable
private fun PreviewLoading() {
    AppThemePreview {
        SyncScreen(
            showLoading = true,
            tryAgainClicked = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewFailed() {
    AppThemePreview {
        SyncScreen(
            showLoading = false,
            tryAgainClicked = { }
        )
    }
}