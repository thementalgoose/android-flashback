package tmg.flashback.statistics.ui.weekend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class WeekendActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Scaffold(content = {
                    WeekendScreen()
                })
            }
        }
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, WeekendActivity::class.java)
        }
    }
}