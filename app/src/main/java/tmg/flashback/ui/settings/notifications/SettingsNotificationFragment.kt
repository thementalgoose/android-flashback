package tmg.flashback.ui.settings.notifications

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.constants.ViewType
import tmg.flashback.constants.logEvent
import tmg.flashback.core.ui.settings.SettingsFragment
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class SettingsNotificationFragment: SettingsFragment() {

    private val viewModel: SettingsNotificationViewModel by viewModel()

    override val screenAnalytics = ScreenAnalytics(
        screenName = "Settings - Notification"
    )

    override val prefClicked: (prefKey: String) -> Unit = { prefKey ->
        viewModel.inputs.preferenceClicked(prefKey, null)
    }
    override val prefSwitchClicked: (prefKey: String, newState: Boolean) -> Unit = { prefKey, newState ->
        viewModel.inputs.preferenceClicked(prefKey, newState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analyticsController.logEvent(ViewType.SETTINGS_NOTIFICATIONS)

        observe(viewModel.outputs.settings) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.openNotificationsChannel) { channelId ->
            context?.let { context ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val intent = Intent().apply {
                        action = Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
                    }
                    startActivity(intent)
                }
            }
        }

        observeEvent(viewModel.outputs.openNotifications) {
            context?.let { context ->
                val intent = Intent().apply {
                    action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    putExtra("app_package", context.packageName)
                    putExtra("app_uid", context.applicationInfo.uid)
                }
                startActivity(intent)
            }
        }
    }
}