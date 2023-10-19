@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package tmg.flashback.ui.components.layouts

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.annotations.PreviewPhone
import tmg.flashback.ui.annotations.PreviewTablet
import tmg.flashback.ui.components.loading.Fade

@Composable
fun MasterDetailsPane(
    windowSizeClass: WindowSizeClass,
    master: @Composable () -> Unit,
    detailsShow: Boolean,
    details: @Composable () -> Unit,
    detailsActionUpClicked: () -> Unit
) {
    Row(
        Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary)
    ) {
        Box(
            Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            if (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Expanded && detailsShow) {
                details()
                BackHandler(onBack = detailsActionUpClicked)
            } else {
                master()
            }
        }
        if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                if (detailsShow) {
                    Fade(visible = detailsShow) {
                        details()
                    }
                    BackHandler(onBack = detailsActionUpClicked)
                }
            }
        }
    }
}

@PreviewPhone
@Composable
private fun PreviewPhoneNoDetails(
    @PreviewParameter(BooleanParamProvider::class) isLight: Boolean
) {
    AppThemePreview(isLight) {
        MasterDetailsPane(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(500.dp, 600.dp)),
            master = { PreviewMaster() },
            detailsShow = false,
            details = { PreviewDetails() },
            detailsActionUpClicked = { }
        )
    }
}

@PreviewPhone
@Composable
private fun PreviewPhoneMasterDetails(
    @PreviewParameter(BooleanParamProvider::class) isLight: Boolean
) {
    AppThemePreview(isLight) {
        MasterDetailsPane(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(500.dp, 600.dp)),
            master = { PreviewMaster() },
            detailsShow = true,
            details = { PreviewDetails() },
            detailsActionUpClicked = { }
        )
    }
}

@PreviewTablet
@Composable
private fun PreviewTabletNoDetails(
    @PreviewParameter(BooleanParamProvider::class) isLight: Boolean
) {
    AppThemePreview(isLight) {
        MasterDetailsPane(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(1000.dp, 400.dp)),
            master = { PreviewMaster() },
            detailsShow = false,
            details = { PreviewDetails() },
            detailsActionUpClicked = { }
        )
    }
}

@PreviewTablet
@Composable
private fun PreviewTabletMasterDetails(
    @PreviewParameter(BooleanParamProvider::class) isLight: Boolean
) {
    AppThemePreview(isLight) {
        MasterDetailsPane(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(1000.dp, 400.dp)),
            master = { PreviewMaster() },
            detailsShow = true,
            details = { PreviewDetails() },
            detailsActionUpClicked = { }
        )
    }
}

@Composable
private fun PreviewDetails() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Cyan)
    ) {
        TextBody1(text = "Details")
    }
}
@Composable
private fun PreviewMaster() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Green)
    ) {
        TextBody1(text = "Master")
    }
}

private class BooleanParamProvider: PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(true, false)
}