package tmg.flashback.settings.ui.settings.support

import android.os.Bundle
import androidx.activity.compose.setContent
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class SettingsSupportActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Settings Support")

        setContent {
            AppTheme {
                SettingsSupportScreenVM(
                    actionUpClicked = { finish() }
                )
            }
        }
    }
}