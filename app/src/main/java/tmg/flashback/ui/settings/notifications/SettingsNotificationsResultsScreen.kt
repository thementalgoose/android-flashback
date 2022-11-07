package tmg.flashback.ui.settings.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import tmg.flashback.ui.components.settings.Pref
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Settings
import tmg.flashback.ui.settings.Setting

@Composable
fun SettingsNotificationsResultsScreenVM(
    showBack: Boolean = true,
    actionUpClicked: () -> Unit = { }
) {
    val viewModel = hiltViewModel<SettingsNotificationsResultsViewModel>()

    ScreenView(screenName = "Settings - Notification Results")

    val permissionEnabled = viewModel.outputs.permissionEnabled.observeAsState(false)
    val qualifyingEnabled = viewModel.outputs.qualifyingEnabled.observeAsState(false)
    val sprintEnabled = viewModel.outputs.sprintEnabled.observeAsState(false)
    val raceEnabled = viewModel.outputs.raceEnabled.observeAsState(false)

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    SettingsNotificationsResultsScreen(
        showBack = showBack,
        actionUpClicked = actionUpClicked,
        prefClicked = viewModel.inputs::prefClicked,
        permissionEnabled = permissionEnabled.value,
        qualifyingEnabled = qualifyingEnabled.value,
        sprintEnabled = sprintEnabled.value,
        raceEnabled = raceEnabled.value
    )
}

@Composable
fun SettingsNotificationsResultsScreen(
    showBack: Boolean,
    actionUpClicked: () -> Unit,
    prefClicked: (Setting) -> Unit,
    permissionEnabled: Boolean,
    qualifyingEnabled: Boolean,
    sprintEnabled: Boolean,
    raceEnabled: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_section_notifications_results_title),
                    icon = when (showBack) {
                        true -> painterResource(id = R.drawable.ic_back)
                        false -> null
                    },
                    iconContentDescription = stringResource(id = R.string.ab_back),
                    actionUpClicked = actionUpClicked
                )
            }

            if (!permissionEnabled) {
                Header(title = R.string.settings_header_permissions)
                Pref(
                    model = Settings.Notifications.notificationPermissionEnable,
                    onClick = prefClicked
                )
            }
            Header(title = R.string.settings_header_notifications)
            Switch(
                model = Settings.Notifications.notificationResultsQualifying(qualifyingEnabled, isEnabled = permissionEnabled),
                onClick = prefClicked
            )
            Switch(
                model = Settings.Notifications.notificationResultsSprint(sprintEnabled, isEnabled = permissionEnabled),
                onClick = prefClicked
            )
            Switch(
                model = Settings.Notifications.notificationResultsRace(raceEnabled, isEnabled = permissionEnabled),
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
        SettingsNotificationsResultsScreen(
            showBack = true,
            actionUpClicked = { },
            prefClicked = { },
            permissionEnabled = false,
            qualifyingEnabled = true,
            sprintEnabled = true,
            raceEnabled = false
        )
    }
}