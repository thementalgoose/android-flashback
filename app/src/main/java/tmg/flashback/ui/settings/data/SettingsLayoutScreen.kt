package tmg.flashback.ui.settings.data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings

@Composable
fun SettingsLayoutScreenVM(
    showBack: Boolean = true,
    actionUpClicked: () -> Unit = { },
    viewModel: SettingsLayoutViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings - Layout")

    val collapsedList = viewModel.outputs.collapsedListEnabled.collectAsState(true)
    val emptyWeeksInSchedule = viewModel.outputs.emptyWeeksInSchedule.collectAsState(false)
    SettingsLayoutScreen(
        showBack = showBack,
        actionUpClicked = actionUpClicked,
        prefClicked = viewModel.inputs::prefClicked,
        collapsedListEnabled = collapsedList.value,
        showEmptyWeeksInSchedule = emptyWeeksInSchedule.value
    )
}

@Composable
fun SettingsLayoutScreen(
    showBack: Boolean,
    actionUpClicked: () -> Unit,
    prefClicked: (Setting) -> Unit,
    collapsedListEnabled: Boolean,
    showEmptyWeeksInSchedule: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_section_home_title),
                    action = if (showBack) HeaderAction.BACK else null,
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = R.string.settings_header_home)
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
            showBack = true,
            actionUpClicked = {},
            prefClicked = {},
            collapsedListEnabled = true,
            showEmptyWeeksInSchedule = false
        )
    }
}