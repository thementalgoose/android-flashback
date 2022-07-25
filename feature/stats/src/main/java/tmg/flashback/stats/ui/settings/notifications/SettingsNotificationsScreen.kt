package tmg.flashback.stats.ui.settings.notifications

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.androidx.compose.viewModel
import tmg.flashback.stats.R
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.repository.models.NotificationReminder
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.settings.SettingsScreen
import tmg.utilities.lifecycle.Event
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

@Composable
fun SettingsNotificationScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<SettingsNotificationViewModel>()
    
    ScreenView(screenName = "Settings Notifications")

    SettingsScreen(
        title = stringResource(id = R.string.settings_notifications_title),
        actionUpClicked = actionUpClicked,
        viewModel = viewModel
    )
}