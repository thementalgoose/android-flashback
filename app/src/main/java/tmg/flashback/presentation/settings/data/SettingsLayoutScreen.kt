package tmg.flashback.presentation.settings.data

import androidx.compose.foundation.layout.PaddingValues
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
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.presentation.settings.Settings
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Setting

@Composable
fun SettingsLayoutScreenVM(
    actionUpClicked: () -> Unit,
    paddingValues: PaddingValues,
    windowSizeClass: WindowSizeClass,
    viewModel: SettingsLayoutViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings - Layout")

    val collapsedList = viewModel.outputs.collapsedListEnabled.collectAsState()
    val emptyWeeksInSchedule = viewModel.outputs.emptyWeeksInSchedule.collectAsState()
    val showRecentHighlights = viewModel.outputs.recentHighlights.collectAsState()
    val rememberSeasonChange = viewModel.outputs.rememberSeasonChange.collectAsState()
    SettingsLayoutScreen(
        actionUpClicked = actionUpClicked,
        windowSizeClass = windowSizeClass,
        paddingValues = paddingValues,
        prefClicked = viewModel.inputs::prefClicked,
        collapsedListEnabled = collapsedList.value,
        showEmptyWeeksInSchedule = emptyWeeksInSchedule.value,
        showRecentHighlights = showRecentHighlights.value,
        rememberSeasonChange = rememberSeasonChange.value
    )
}

@Composable
fun SettingsLayoutScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    paddingValues: PaddingValues,
    prefClicked: (Setting) -> Unit,
    collapsedListEnabled: Boolean,
    showEmptyWeeksInSchedule: Boolean,
    showRecentHighlights: Boolean,
    rememberSeasonChange: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = paddingValues,
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
            Switch(
                model = Settings.Data.rememberSeasonChange(rememberSeasonChange),
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
            paddingValues = PaddingValues.Absolute(),
            actionUpClicked = {},
            prefClicked = {},
            collapsedListEnabled = true,
            showEmptyWeeksInSchedule = false,
            showRecentHighlights = true,
            rememberSeasonChange = true
        )
    }
}