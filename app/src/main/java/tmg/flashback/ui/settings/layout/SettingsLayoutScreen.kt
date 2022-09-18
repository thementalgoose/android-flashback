package tmg.flashback.ui.settings.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Settings
import tmg.flashback.ui.settings.Setting

@Composable
fun SettingsLayoutScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel = hiltViewModel<SettingsLayoutViewModel>()

    ScreenView(screenName = "Settings - Layout")

    val providedByAtTopEnabled = viewModel.outputs.providedByAtTopEnabled.observeAsState(true)
    SettingsLayoutScreen(
        actionUpClicked = actionUpClicked,
        prefClicked = viewModel.inputs::prefClicked,
        providedByAtTopEnabled = providedByAtTopEnabled.value
    )
}

@Composable
fun SettingsLayoutScreen(
    actionUpClicked: () -> Unit,
    prefClicked: (Setting) -> Unit,
    providedByAtTopEnabled: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_section_home_title),
                    icon = painterResource(id = R.drawable.ic_back),
                    iconContentDescription = stringResource(id = R.string.ab_back),
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = R.string.settings_header_home)
            Switch(
                model = Settings.Layout.providedByAtTop(providedByAtTopEnabled),
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
            actionUpClicked = {},
            prefClicked = {},
            providedByAtTopEnabled = true
        )
    }
}