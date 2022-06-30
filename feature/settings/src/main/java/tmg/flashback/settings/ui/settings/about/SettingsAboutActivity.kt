package tmg.flashback.settings.ui.settings.about

import android.os.Bundle
import androidx.activity.compose.setContent
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class SettingsAboutActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                SettingsAboutScreenVM(
                    actionUpClicked = { finish() }
                )
            }
        }
    }
}