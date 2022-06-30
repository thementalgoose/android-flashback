package tmg.flashback.stats.ui.settings.home

import android.os.Bundle
import androidx.activity.compose.setContent
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class SettingsHomeActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Settings Home")

        setContent {
            AppTheme {
                SettingsHomeScreenVM(
                    actionUpClicked = { finish() }
                )
            }
        }
    }
}