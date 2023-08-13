package tmg.flashback.ui.settings.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.results.contract.repository.models.NotificationResultsAvailable
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Pref
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings

@Composable
fun SettingsNotificationsResultsScreenVM(
    showBack: Boolean = true,
    actionUpClicked: () -> Unit = { },
    viewModel: SettingsNotificationsResultsViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings - Notification Results")

    val permissionEnabled = viewModel.outputs.permissionEnabled.collectAsState(false)
    val notifications = viewModel.outputs.notifications.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    SettingsNotificationsResultsScreen(
        showBack = showBack,
        actionUpClicked = actionUpClicked,
        prefClicked = viewModel.inputs::prefClicked,
        permissionEnabled = permissionEnabled.value,
        notifications = notifications.value
    )
}

@Composable
fun SettingsNotificationsResultsScreen(
    showBack: Boolean,
    actionUpClicked: () -> Unit,
    prefClicked: (Setting) -> Unit,
    permissionEnabled: Boolean,
    notifications: List<Pair<NotificationResultsAvailable, Boolean>>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_section_notifications_results_title),
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
            notifications.forEach { (results, isChecked) ->
                Switch(
                    model = Settings.Notifications.notificationResultsAvailable(results, isChecked, isEnabled = permissionEnabled),
                    onClick = prefClicked
                )
            }
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
            notifications = NotificationResultsAvailable.values()
                .map { it to true }
        )
    }
}