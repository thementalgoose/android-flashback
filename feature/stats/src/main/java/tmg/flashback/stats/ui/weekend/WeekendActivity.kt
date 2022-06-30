package tmg.flashback.stats.ui.weekend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import tmg.flashback.stats.analytics.AnalyticsConstants.analyticsRound
import tmg.flashback.stats.analytics.AnalyticsConstants.analyticsSeason
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class WeekendActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val season = ((intent?.extras ?: savedInstanceState)?.getParcelable<WeekendInfo>(keyWeekendInfo)?.season?.toString() ?: "")
        val round = ((intent?.extras ?: savedInstanceState)?.getParcelable<WeekendInfo>(keyWeekendInfo)?.round?.toString() ?: "")
        logScreenViewed("Race Weekend", mapOf(
            analyticsSeason to season,
            analyticsRound to round
        ))

        val weekendInfo: WeekendInfo = (intent?.extras ?: savedInstanceState)?.getParcelable(keyWeekendInfo)!!
        setContent {
            AppTheme {
                WeekendScreen(
                    weekendInfo = weekendInfo,
                    actionUpClicked = {
                        finish()
                    }
                )
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