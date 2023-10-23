@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package tmg.flashback.ui.components.layouts

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.annotations.PreviewPhone
import tmg.flashback.ui.annotations.PreviewTablet
import tmg.flashback.ui.components.loading.Fade

private val detailsMinWidth = 320.dp
private const val DETAILS_RATIO = 0.58f

@Composable
fun MasterDetailsPane(
    windowSizeClass: WindowSizeClass,
    master: @Composable () -> Unit,
    detailsShow: Boolean,
    details: @Composable () -> Unit,
    detailsActionUpClicked: () -> Unit
) {
    BoxWithConstraints {
        val detailsWidth = when {
            maxWidth <= (detailsMinWidth * 2f) -> maxWidth * 0.5f
            maxWidth >= ((maxWidth * DETAILS_RATIO) + detailsMinWidth) -> (maxWidth * DETAILS_RATIO).coerceAtLeast(detailsMinWidth)
            else -> maxWidth - detailsMinWidth
        }
        val detailsOffset = animateDpAsState(targetValue = when (detailsShow) {
            true -> maxWidth - detailsWidth
            false -> maxWidth
        }, label = "mdp_offset")

        Box(
            Modifier
                .width(if (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Expanded) maxWidth else detailsOffset.value)
                .fillMaxHeight()
        ) {
            master()
            if (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Expanded) {
                Fade(visible = detailsShow) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(AppTheme.colors.backgroundPrimary)
                    )
                    if (detailsShow) {
                        details()
                        BackHandler(onBack = detailsActionUpClicked)
                    }
                }
            }
        }
        if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
            Box(
                modifier = Modifier
                    .offset(detailsOffset.value)
                    .width(detailsWidth)
                    .fillMaxHeight()
                    .background(AppTheme.colors.backgroundPrimary),
                contentAlignment = Alignment.Center
            ) {
                Fade(visible = detailsShow) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(AppTheme.colors.backgroundPrimary)
                    )
                    if (detailsShow) {
                        details()
                        BackHandler(onBack = detailsActionUpClicked)
                    }
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