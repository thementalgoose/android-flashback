package tmg.flashback.presentation.sync

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import tmg.flashback.R
import tmg.flashback.strings.R.string
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextHeadline1
import tmg.flashback.ui.components.layouts.AppScaffold

private val progressHeight: Dp = 8.dp

@Composable
fun SyncScreen(
    windowSizeClass: WindowSizeClass,
    drivers: SyncState,
    circuits: SyncState,
    config: SyncState,
    constructors: SyncState,
    races: SyncState,
    showTryAgain: Boolean,
    tryAgainClicked: () -> Unit
) {
    ScreenView(screenName = "Sync")

    val headerHeight = when (windowSizeClass.heightSizeClass) {
        WindowHeightSizeClass.Compact -> 30.dp
        WindowHeightSizeClass.Medium -> 60.dp
        WindowHeightSizeClass.Expanded -> 120.dp
        else -> 30.dp
    }

    AppScaffold(
        content = {
            val layoutDirection = LocalLayoutDirection.current
            Box(modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.backgroundSplash)
                .padding(
                    start = it.calculateStartPadding(layoutDirection),
                    end = it.calculateEndPadding(layoutDirection)
                )
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = headerHeight + it.calculateTopPadding())
                    .clip(
                        RoundedCornerShape(
                            topStart = AppTheme.dimens.radiusLarge,
                            topEnd = AppTheme.dimens.radiusLarge
                        )
                    )
                    .background(AppTheme.colors.backgroundPrimary)
                    .padding(
                        top = AppTheme.dimens.medium,
                        bottom = AppTheme.dimens.medium + it.calculateBottomPadding(),
                        start = AppTheme.dimens.medium,
                        end = AppTheme.dimens.medium
                    )
                ) {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        SyncScreenCompact(
                            drivers = drivers,
                            circuits = circuits,
                            config = config,
                            constructors = constructors,
                            races = races,
                            showTryAgain = showTryAgain,
                            tryAgainClicked = tryAgainClicked
                        )
                    } else {
                        SyncScreenExpanded(
                            drivers = drivers,
                            circuits = circuits,
                            config = config,
                            constructors = constructors,
                            races = races,
                            showTryAgain = showTryAgain,
                            tryAgainClicked = tryAgainClicked
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun ColumnScope.SyncScreenCompact(
    drivers: SyncState,
    circuits: SyncState,
    config: SyncState,
    constructors: SyncState,
    races: SyncState,
    showTryAgain: Boolean,
    tryAgainClicked: () -> Unit
) {
    TextHeadline1(text = stringResource(id = R.string.app_name))
    TextBody2(
        modifier = Modifier.padding(top = AppTheme.dimens.nsmall),
        text = stringResource(id = string.splash_sync_info)
    )
    if (showTryAgain) {
        TextBody2(
            modifier = Modifier.padding(top = AppTheme.dimens.nsmall),
            text = stringResource(id = string.splash_sync_required_failed)
        )
    }
    Box(Modifier.weight(1f))
    Breakdown(
        label = string.splash_sync_drivers,
        syncState = drivers
    )
    Breakdown(
        label = string.splash_sync_constructors,
        syncState = constructors
    )
    Breakdown(
        label = string.splash_sync_circuits,
        syncState = circuits
    )
    Breakdown(
        label = string.splash_sync_races,
        syncState = races
    )
    Breakdown(
        label = string.splash_sync_config,
        syncState = config
    )
    Box(Modifier.height(70.dp)) {
        if (showTryAgain) {
            ButtonSecondary(
                modifier = Modifier.align(Alignment.Center).fillMaxWidth(),
                text = stringResource(id = string.splash_sync_try_again),
                onClick = tryAgainClicked
            )
        }
    }
}

@Composable
private fun ColumnScope.SyncScreenExpanded(
    drivers: SyncState,
    circuits: SyncState,
    config: SyncState,
    constructors: SyncState,
    races: SyncState,
    showTryAgain: Boolean,
    tryAgainClicked: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.medium)
    ) {
        Column(Modifier.weight(1f).fillMaxHeight()) {
            TextHeadline1(text = stringResource(id = R.string.app_name))
            TextBody2(
                modifier = Modifier.padding(top = AppTheme.dimens.nsmall),
                text = stringResource(id = string.splash_sync_info)
            )
            if (showTryAgain) {
                TextBody2(
                    modifier = Modifier.padding(top = AppTheme.dimens.nsmall),
                    text = stringResource(id = string.splash_sync_required_failed)
                )
                ButtonSecondary(
                    text = stringResource(id = string.splash_sync_try_again),
                    onClick = tryAgainClicked
                )
            }
        }
        Column(Modifier.weight(1f).fillMaxHeight()) {
            Breakdown(
                label = string.splash_sync_drivers,
                syncState = drivers
            )
            Breakdown(
                label = string.splash_sync_constructors,
                syncState = constructors
            )
            Breakdown(
                label = string.splash_sync_circuits,
                syncState = circuits
            )
            Breakdown(
                label = string.splash_sync_races,
                syncState = races
            )
            Breakdown(
                label = string.splash_sync_config,
                syncState = config
            )
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
            tryAgainClicked = { },
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified)
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
            tryAgainClicked = { },
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified)
        )
    }
}