package tmg.flashback.presentation.settings.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.presentation.settings.Settings
import tmg.flashback.strings.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.list.LazyColumnEdgeToEdge
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Pref
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Setting.Pref

@Composable
fun SettingsWidgetScreenVM(
    actionUpClicked: () -> Unit = { },
    windowSizeClass: WindowSizeClass,
    viewModel: SettingsWidgetViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings - Widgets")

    val showBackground = viewModel.outputs.showBackground.collectAsState()
    val deeplinkToEvent = viewModel.outputs.deeplinkToEvent.collectAsState()

    SettingsWidgetScreen(
        actionUpClicked = actionUpClicked,
        windowSizeClass = windowSizeClass,
        prefClicked = viewModel.inputs::prefClicked,
        showBackground = showBackground.value,
        deeplinkToEvent = deeplinkToEvent.value,
        refreshWidgets = viewModel.inputs::refreshWidgets
    )
}

@Composable
fun SettingsWidgetScreen(
    actionUpClicked: () -> Unit,
    refreshWidgets: () -> Unit,
    windowSizeClass: WindowSizeClass,
    prefClicked: (Setting) -> Unit,
    showBackground: Boolean,
    deeplinkToEvent: Boolean
) {
    LazyColumnEdgeToEdge(
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_header_widgets),
                    action = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) HeaderAction.BACK else null,
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = R.string.settings_section_widgets_title)
            Switch(
                model = Settings.Widgets.showBackground(showBackground),
                onClick = prefClicked
            )
            Switch(
                model = Settings.Widgets.deeplinkToEvent(deeplinkToEvent),
                onClick = prefClicked
            )
            Header(title = R.string.settings_section_refresh_title)
            Pref(
                model = Settings.Widgets.refreshWidgets,
                onClick = prefClicked
            )
            Footer()
        }
    )
    val context = LocalContext.current
    LaunchedEffect(
        key1 = showBackground,
        key2 = deeplinkToEvent
    ) {
        refreshWidgets()
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        SettingsWidgetScreen(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified),
            actionUpClicked = {},
            prefClicked = {},
            showBackground = true,
            deeplinkToEvent = false,
            refreshWidgets = { }
        )
    }
}