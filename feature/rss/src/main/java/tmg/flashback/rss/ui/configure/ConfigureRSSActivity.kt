package tmg.flashback.rss.ui.configure

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class ConfigureRSSActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                SettingsRSSConfigureScreenVM(
                    actionUpClicked = { finish() }
                )
            }
        }
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, ConfigureRSSActivity::class.java)
        }
    }
}