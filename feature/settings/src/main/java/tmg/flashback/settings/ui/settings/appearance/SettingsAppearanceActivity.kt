package tmg.flashback.settings.ui.settings.appearance

import android.os.Bundle
import androidx.activity.compose.setContent
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class SettingsAppearanceActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Settings Appearance")

        setContent {
            AppTheme {
                SettingsAppearanceScreenVM(
                    actionUpClicked = { finish() }
                )
            }
        }
    }
}