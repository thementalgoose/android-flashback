package tmg.flashback.ui.sync

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextHeadline1

private val progressHeight: Dp = 8.dp
private val headerHeight: Dp = 150.dp

@Composable
fun SyncScreen(
    drivers: SyncState,
    circuits: SyncState,
    config: SyncState,
    constructors: SyncState,
    races: SyncState,
    showTryAgain: Boolean,
    tryAgainClicked: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(AppTheme.colors.backgroundSplash)
    ) {
        Box(Modifier.height(headerHeight))

        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = AppTheme.dimens.radiusLarge,
                    topEnd = AppTheme.dimens.radiusLarge
                )
            )
            .background(AppTheme.colors.backgroundPrimary)
            .padding(
                vertical = AppTheme.dimens.medium,
                horizontal = AppTheme.dimens.medium
            )
        ) {
            TextHeadline1(text = stringResource(id = R.string.app_name))
            TextBody2(
                modifier = Modifier.padding(top = AppTheme.dimens.nsmall),
                text = stringResource(id = R.string.splash_sync_info)
            )
            Box(Modifier.weight(1f))
            Breakdown(
                label = R.string.splash_sync_drivers,
                syncState = drivers
            )
            Breakdown(
                label = R.string.splash_sync_constructors,
                syncState = constructors
            )
            Breakdown(
                label = R.string.splash_sync_circuits,
                syncState = circuits
            )
            Breakdown(
                label = R.string.splash_sync_races,
                syncState = races
            )
            Breakdown(
                label = R.string.splash_sync_config,
                syncState = config
            )
            Box(Modifier.height(60.dp)) {
                if (showTryAgain) {
                    ButtonSecondary(
                        text = stringResource(id = R.string.splash_sync_try_again),
                        onClick = tryAgainClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun Breakdown(
    @StringRes
    label: Int,
    syncState: SyncState
) {

    val colour = animateColorAsState(
        targetValue = when (syncState) {
            SyncState.LOADING -> AppTheme.colors.primary
            SyncState.DONE -> AppTheme.colors.f1DeltaNegative
            SyncState.FAILED -> AppTheme.colors.f1DeltaPositive
        },
        animationSpec = tween(durationMillis = 400)
    )
    val progress = animateFloatAsState(
        targetValue = when (syncState) {
            SyncState.LOADING -> 0f
            SyncState.DONE -> 1f
            SyncState.FAILED -> 1f
        },
        animationSpec = tween(durationMillis = 400)
    )

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = AppTheme.dimens.xsmall)
    ) {
        TextBody1(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = label)
        )
        Spacer(Modifier.height(AppTheme.dimens.xsmall))
        Box(Modifier.fillMaxWidth()) {
            LinearProgressIndicator(
                color = colour.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(progressHeight)
                    .clip(RoundedCornerShape(AppTheme.dimens.radiusLarge))
                    .alpha(0.5f)
            )
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(progressHeight)
                    .clip(RoundedCornerShape(AppTheme.dimens.radiusLarge)),
                color = colour.value,
                progress = progress.value
            )
        }
    }
}

@PreviewTheme
@Composable
private fun PreviewLoading() {
    AppThemePreview {
        SyncScreen(
            drivers = SyncState.LOADING,
            circuits = SyncState.LOADING,
            config = SyncState.DONE,
            constructors = SyncState.DONE,
            races = SyncState.LOADING,
            showTryAgain = false,
            tryAgainClicked = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewFailed() {
    AppThemePreview {
        SyncScreen(
            drivers = SyncState.DONE,
            circuits = SyncState.LOADING,
            config = SyncState.FAILED,
            constructors = SyncState.LOADING,
            races = SyncState.LOADING,
            showTryAgain = true,
            tryAgainClicked = { }
        )
    }
}