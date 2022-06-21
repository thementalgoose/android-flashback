package tmg.flashback.stats.ui.settings.notifications

import android.os.Bundle
import androidx.activity.compose.setContent
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class SettingsNotificationsActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                SettingsNotificationScreenVM(
                    actionUpClicked = { finish() }
                )
            }
        }
    }
}