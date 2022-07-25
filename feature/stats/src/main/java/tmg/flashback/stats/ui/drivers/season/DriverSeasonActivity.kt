package tmg.flashback.stats.ui.drivers.season

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import tmg.flashback.stats.analytics.AnalyticsConstants.analyticsDriverId
import tmg.flashback.stats.analytics.AnalyticsConstants.analyticsSeason
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class DriverSeasonActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        logScreenViewed("Driver Season", mapOf(
            analyticsDriverId to (intent.extras?.getString(keyDriverId) ?: ""),
            analyticsSeason to (intent.extras?.getInt(keySeason)?.toString() ?: "")
        ))

        val driverId: String = intent.extras?.getString(keyDriverId)!!
        val driverName: String = intent.extras?.getString(keyDriverName)!!
        val season: Int = intent.extras?.getInt(keySeason)!!

        setContent {
            AppTheme {
                DriverSeasonScreenVM(
                    driverId = driverId,
                    driverName = driverName,
                    season = season,
                    actionUpClicked = { finish() }
                )
            }
        }
    }

    companion object {

        private const val keyDriverId: String = "driverId"
        private const val keyDriverName: String = "driverName"
        private const val keySeason: String = "season"

        fun intent(context: Context, driverId: String, driverName: String, season: Int): Intent {
            return Intent(context, DriverSeasonActivity::class.java).apply {
                putExtra(keyDriverId, driverId)
                putExtra(keyDriverName, driverName)
                putExtra(keySeason, season)
            }
        }
    }
}