package tmg.flashback.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class SettingsAllActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logScreenViewed("Settings")
        setContent {
            AppTheme {
                SettingsAllScreenVM(
                    actionUpClicked = { finish() }
                )
            }
        }
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, SettingsAllActivity::class.java)
        }
    }
}