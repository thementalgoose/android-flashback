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
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings

@Composable
fun SettingsLayoutScreenVM(
    showBack: Boolean = true,
    actionUpClicked: () -> Unit = { },
    viewModel: SettingsLayoutViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings - Layout")

    val providedByAtTopEnabled = viewModel.outputs.collapsedListEnabled.observeAsState(true)
    SettingsLayoutScreen(
        showBack = showBack,
        actionUpClicked = actionUpClicked,
        prefClicked = viewModel.inputs::prefClicked,
        collapsedListEnabled = providedByAtTopEnabled.value
    )
}

@Composable
fun SettingsLayoutScreen(
    showBack: Boolean,
    actionUpClicked: () -> Unit,
    prefClicked: (Setting) -> Unit,
    collapsedListEnabled: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_section_home_title),
                    icon = when (showBack) {
                        true -> painterResource(id = R.drawable.ic_back)
                        false -> null
                    },
                    iconContentDescription = stringResource(id = R.string.ab_back),
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = R.string.settings_header_home)
            Switch(
                model = Settings.Layout.collapseList(collapsedListEnabled),
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
            collapsedListEnabled = true
        )
    }
}