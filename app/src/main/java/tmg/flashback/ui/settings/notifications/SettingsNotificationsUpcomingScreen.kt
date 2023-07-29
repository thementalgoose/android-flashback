package tmg.flashback.ui.settings.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.results.contract.repository.models.NotificationUpcoming
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Pref
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings

@Composable
fun SettingsNotificationsUpcomingScreenVM(
    showBack: Boolean = true,
    actionUpClicked: () -> Unit = { },
    viewModel: SettingsNotificationsUpcomingViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings - Notification Upcoming")

    val permissionEnabled = viewModel.outputs.permissionEnabled.collectAsState(false)
    val notifications = viewModel.outputs.notifications.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    SettingsNotificationsUpcomingScreen(
        showBack = showBack,
        actionUpClicked = actionUpClicked,
        prefClicked = viewModel.inputs::prefClicked,
        permissionEnabled = permissionEnabled.value,
        notifications = notifications.value,
    )
}

@Composable
fun SettingsNotificationsUpcomingScreen(
    showBack: Boolean,
    actionUpClicked: () -> Unit,
    prefClicked: (Setting) -> Unit,
    permissionEnabled: Boolean,
    notifications: List<Pair<NotificationUpcoming, Boolean>>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_section_notifications_upcoming_title),
                    action = if (showBack) HeaderAction.BACK else null,
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
            notifications.forEach { (upcoming, isChecked) ->
                Switch(
                    model = Settings.Notifications.notificationUpcoming(upcoming, isChecked, isEnabled = permissionEnabled),
                    onClick = prefClicked
                )
            }

            Header(title = R.string.settings_header_notice)
            Pref(
                model = Settings.Notifications.notificationNoticePeriod(isEnabled = permissionEnabled),
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
        SettingsNotificationsUpcomingScreen(
            showBack = true,
            actionUpClicked = { },
            prefClicked = { },
            permissionEnabled = false,
            notifications = NotificationUpcoming.values().map { it to true },
        )
    }
}