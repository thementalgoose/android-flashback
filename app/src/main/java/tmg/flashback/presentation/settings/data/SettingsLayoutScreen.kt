package tmg.flashback.presentation.settings.data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Setting
import tmg.flashback.presentation.settings.Settings

@Composable
fun SettingsLayoutScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    viewModel: SettingsLayoutViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings - Layout")

    val collapsedList = viewModel.outputs.collapsedListEnabled.collectAsState()
    val emptyWeeksInSchedule = viewModel.outputs.emptyWeeksInSchedule.collectAsState()
    val showRecentHighlights = viewModel.outputs.recentHighlights.collectAsState()
    SettingsLayoutScreen(
        actionUpClicked = actionUpClicked,
        windowSizeClass = windowSizeClass,
        prefClicked = viewModel.inputs::prefClicked,
        collapsedListEnabled = collapsedList.value,
        showEmptyWeeksInSchedule = emptyWeeksInSchedule.value,
        showRecentHighlights = showRecentHighlights.value
    )
}

@Composable
fun SettingsLayoutScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    prefClicked: (Setting) -> Unit,
    collapsedListEnabled: Boolean,
    showEmptyWeeksInSchedule: Boolean,
    showRecentHighlights: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = string.settings_section_home_title),
                    action = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) HeaderAction.BACK else null,
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = string.settings_header_home)
            Switch(
                model = Settings.Data.showRecentHighlights(showRecentHighlights),
                onClick = prefClicked
            )
            Switch(
                model = Settings.Data.collapseList(collapsedListEnabled),
                onClick = prefClicked
            )
            Switch(
                model = Settings.Data.showEmptyWeeksInSchedule(showEmptyWeeksInSchedule),
                onClick = prefClicked
            )

            Footer()
        }
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        SettingsLayoutScreen(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified),
            actionUpClicked = {},
            prefClicked = {},
            collapsedListEnabled = true,
            showEmptyWeeksInSchedule = false,
            showRecentHighlights = true
        )
    }
}