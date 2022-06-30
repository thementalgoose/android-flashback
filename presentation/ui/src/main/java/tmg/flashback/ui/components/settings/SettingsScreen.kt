package tmg.flashback.ui.components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.R
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel

@Composable
fun SettingsScreen(
    title: String?,
    actionUpClicked: () -> Unit,
    viewModel: SettingsViewModel
) {
    viewModel.loadSettings()
    val list = viewModel.settings.observeAsState(emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            title?.let { title ->
                item("header") {
                    Header(
                        text = title,
                        icon = painterResource(id = R.drawable.ic_back),
                        iconContentDescription = stringResource(id = R.string.ab_back),
                        actionUpClicked = actionUpClicked
                    )
                }
            }
            item("settings") {
                SettingsScreen(
                    models = list.value,
                    prefClicked = viewModel::clickPreference,
                    prefSwitchClicked = viewModel::clickSwitchPreference
                )
            }
        }
    )
}

@Composable
fun SettingsScreen(
    models: List<SettingsModel>,
    prefClicked: (SettingsModel.Pref) -> Unit,
    prefSwitchClicked: (SettingsModel.SwitchPref, toState: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        for (x in models) {
            when (x) {
                is SettingsModel.Header -> {
                    Category(
                        text = stringResource(id = x.title)
                    )
                }
                is SettingsModel.Pref -> {
                    Preference(
                        title = stringResource(id = x.title),
                        subtitle = stringResource(id = x.description),
                        preferenceClicked = { prefClicked(x) }
                    )
                }
                is SettingsModel.SwitchPref -> {
                    SwitchPreference(
                        title = stringResource(id = x.title),
                        subtitle = stringResource(id = x.description),
                        isChecked = x.getState(),
                        preferenceClicked = { prefSwitchClicked(x, !x.getState()) }
                    )
                }
            }
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview(isLight = true) {
        SettingsScreen(
            models = fakeSettings,
            prefClicked = { },
            prefSwitchClicked = { _, _ -> }
        )
    }
}

private val fakeSettings = listOf(
    SettingsModel.Header(
        title = R.string.settings_theme_title,
        beta = true
    ),
    SettingsModel.Pref(
        title = R.string.settings_theme_title,
        description = R.string.settings_theme_theme_description,
        onClick = { },
        beta = false
    ),
    SettingsModel.SwitchPref(
        title = R.string.settings_theme_title,
        description = R.string.settings_theme_theme_description,
        getState = { true },
        saveState = { },
        saveStateNotification = { },
        beta = false
    )
)