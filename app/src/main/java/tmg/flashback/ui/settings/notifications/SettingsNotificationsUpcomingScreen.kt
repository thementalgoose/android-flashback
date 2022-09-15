package tmg.flashback.ui.settings.notifications

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
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Pref
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Settings
import tmg.flashback.ui.settings.Setting

@Composable
fun SettingsNotificationsUpcomingScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel = hiltViewModel<SettingsNotificationsUpcomingViewModel>()

    val permissionEnabled = viewModel.outputs.permissionEnabled.observeAsState(false)
    val freePracticeEnabled = viewModel.outputs.freePracticeEnabled.observeAsState(false)
    val qualifyingEnabled = viewModel.outputs.qualifyingEnabled.observeAsState(false)
    val otherEnabled = viewModel.outputs.otherEnabled.observeAsState(false)
    val raceEnabled = viewModel.outputs.raceEnabled.observeAsState(false)

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    SettingsNotificationsUpcomingScreen(
        actionUpClicked = actionUpClicked,
        prefClicked = viewModel.inputs::prefClicked,
        permissionEnabled = permissionEnabled.value,
        freePracticeEnabled = freePracticeEnabled.value,
        qualifyingEnabled = qualifyingEnabled.value,
        otherEnabled = otherEnabled.value,
        raceEnabled = raceEnabled.value
    )
}

@Composable
fun SettingsNotificationsUpcomingScreen(
    actionUpClicked: () -> Unit,
    prefClicked: (Setting) -> Unit,
    permissionEnabled: Boolean,
    freePracticeEnabled: Boolean,
    qualifyingEnabled: Boolean,
    otherEnabled: Boolean,
    raceEnabled: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_section_notifications_results_title),
                    icon = painterResource(id = R.drawable.ic_back),
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
                model = Settings.Notifications.notificationUpcomingFreePractice(freePracticeEnabled, isEnabled = permissionEnabled),
                onClick = prefClicked
            )
            Switch(
                model = Settings.Notifications.notificationUpcomingQualifying(qualifyingEnabled, isEnabled = permissionEnabled),
                onClick = prefClicked
            )
            Switch(
                model = Settings.Notifications.notificationUpcomingOther(otherEnabled, isEnabled = permissionEnabled),
                onClick = prefClicked
            )
            Switch(
                model = Settings.Notifications.notificationUpcomingRace(raceEnabled, isEnabled = permissionEnabled),
                onClick = prefClicked
            )

            Header(title = R.string.settings_header_notice)
            Pref(
                model = Settings.Notifications.notificationNoticePeriod(isEnabled = permissionEnabled),
                onClick = prefClicked
            )
        }
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        SettingsNotificationsUpcomingScreen(
            actionUpClicked = { },
            prefClicked = { },
            permissionEnabled = false,
            qualifyingEnabled = true,
            freePracticeEnabled = true,
            otherEnabled = true,
            raceEnabled = false
        )
    }
}