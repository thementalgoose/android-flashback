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
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.results.repository.models.NotificationReminder
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Option
import tmg.flashback.ui.components.settings.Pref
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings

@Composable
fun SettingsNotificationUpcomingNoticeScreenVM(
    showBack: Boolean = true,
    actionUpClicked: () -> Unit = { },
    viewModel: SettingsNotificationsUpcomingNoticeViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings - Notification Notice")

    val result = viewModel.outputs.currentlySelected.collectAsState(NotificationReminder.MINUTES_30)
    val permissions = viewModel.outputs.permissions.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    SettingsNotificationUpcomingNoticeScreen(
        showBack = showBack,
        actionUpClicked = actionUpClicked,
        prefClicked = viewModel.inputs::prefClicked,
        result = result.value,
        permissions = permissions.value
    )
}

@Composable
fun SettingsNotificationUpcomingNoticeScreen(
    showBack: Boolean,
    actionUpClicked: () -> Unit,
    prefClicked: (Setting) -> Unit,
    result: NotificationReminder,
    permissions: UpcomingNoticePermissionState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.notification_onboarding_title),
                    action = if (showBack) HeaderAction.BACK else null,
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = R.string.notification_onboarding_title)

            if (!permissions.runtimePermission) {
                Pref(
                    model = Settings.Notifications.notificationPermissionEnable,
                    onClick = prefClicked
                )
            } else if (!permissions.exactAlarmPermission) {
                Pref(
                    model = Settings.Notifications.notificationExactAlarmEnable,
                    onClick = prefClicked
                )
            }
            NotificationReminder.values()
                .sortedBy { it.seconds }
                .forEach {
                    Option(
                        model = Settings.Notifications.notificationNoticePeriod(
                            reminder = it,
                            isChecked = it == result && permissions.exactAlarmPermission && permissions.runtimePermission,
                            isEnabled = permissions.exactAlarmPermission && permissions.runtimePermission
                        ),
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
        SettingsNotificationUpcomingNoticeScreen(
            showBack = true,
            actionUpClicked = { },
            prefClicked = { },
            result = NotificationReminder.MINUTES_30,
            permissions = UpcomingNoticePermissionState(false, false)
        )
    }
}