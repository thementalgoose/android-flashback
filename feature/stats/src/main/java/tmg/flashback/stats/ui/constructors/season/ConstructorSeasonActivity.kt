package tmg.flashback.stats.ui.constructors.season

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import tmg.flashback.stats.analytics.AnalyticsConstants
import tmg.flashback.stats.analytics.AnalyticsConstants.analyticsConstructorId
import tmg.flashback.stats.analytics.AnalyticsConstants.analyticsSeason
import tmg.flashback.stats.ui.constructors.overview.ConstructorOverviewActivity
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class ConstructorSeasonActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Constructor Season", mapOf(
            analyticsConstructorId to (intent.extras?.getString(keyConstructorId) ?: ""),
            analyticsSeason to (intent.extras?.getInt(keySeason)?.toString() ?: "")
        ))

        val constructorId: String = intent.extras?.getString(keyConstructorId)!!
        val constructorName: String = intent.extras?.getString(keyConstructorName)!!
        val season: Int = intent.extras?.getInt(keySeason)!!

        setContent {
            AppTheme {
                ConstructorSeasonScreenVM(
                    constructorId = constructorId,
                    constructorName = constructorName,
                    season = season
                )
            }
        }
    }

    companion object {

        private const val keyConstructorId: String = "constructorId"
        private const val keyConstructorName: String = "constructorName"
        private const val keySeason: String = "season"

        fun intent(context: Context, constructorId: String, constructorName: String, season: Int): Intent {
            return Intent(context, ConstructorSeasonActivity::class.java).apply {
                putExtra(keyConstructorId, constructorId)
                putExtra(keyConstructorName, constructorName)
                putExtra(keySeason, season)
            }
        }
    }
}