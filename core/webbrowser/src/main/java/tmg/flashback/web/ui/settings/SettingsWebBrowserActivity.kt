package tmg.flashback.web.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class SettingsWebBrowserActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                SettingsWebBrowserScreenVM(
                    actionUpClicked = { finish() }
                )
            }
        }
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, SettingsWebBrowserActivity::class.java)
        }
    }
}