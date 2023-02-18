package tmg.flashback.ui.sync

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
                    start = AppTheme.dimens.medium,
                    end = AppTheme.dimens.medium,
                    top = AppTheme.dimens.large,
                    bottom = AppTheme.dimens.medium
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
                    start = AppTheme.dimens.medium,
                    end = AppTheme.dimens.medium,
                    top = AppTheme.dimens.medium,
                    bottom = AppTheme.dimens.medium
                )
            )
            ButtonSecondary(
                text = stringResource(id = R.string.splash_sync_try_again),
                onClick = tryAgainClicked,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
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