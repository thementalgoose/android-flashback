package tmg.flashback.ads.ui.settings.adverts

import android.os.Bundle
import androidx.activity.compose.setContent
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class SettingsAdvertActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logScreenViewed("Settings Ads")
        setContent {
            AppTheme {
                SettingsAdvertScreenVM(
                    actionUpClicked = { finish() }
                )
            }
        }
    }
}