package tmg.flashback.stats.ui.weekend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class WeekendActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val weekendInfo: WeekendInfo = (intent?.extras ?: savedInstanceState)?.getParcelable(keyWeekendInfo)!!
        setContent {
            AppTheme {
                WeekendScreen(weekendInfo)
            }
        }
    }

    companion object {

        private const val keyWeekendInfo = "weekendInfo"

        fun intent(context: Context, weekendInfo: WeekendInfo): Intent {
            val intent = Intent(context, WeekendActivity::class.java)
            intent.putExtra(keyWeekendInfo, weekendInfo)
            return intent
        }
    }
}