package tmg.flashback.di.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsRound
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsSeason
import tmg.flashback.googleanalytics.manager.FirebaseAnalyticsManager
import tmg.flashback.presentation.HomeActivity
import tmg.flashback.weekend.navigation.ScreenWeekendData
import tmg.flashback.widgets.upnext.contract.WidgetNavigationComponent
import java.time.LocalDate
import javax.inject.Inject

class AppWidgetNavigationComponent @Inject constructor(
    private val firebaseAnalyticsManager: FirebaseAnalyticsManager
): WidgetNavigationComponent {

    override fun getLaunchAppIntent(context: Context): Intent {
        firebaseAnalyticsManager.logEvent("relaunch_app")
        return Intent(context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    override fun getLaunchAppWithSeasonRoundIntent(
        context: Context,
        season: Int,
        round: Int,
        raceName: String,
        circuitId: String,
        circuitName: String,
        country: String,
        countryISO: String,
        date: LocalDate
    ): Intent {
        firebaseAnalyticsManager.logEvent("relaunch_app", params = mapOf(
            analyticsSeason to season.toString(),
            analyticsRound to round.toString()
        ))
        val screenWeekendData = ScreenWeekendData(
            season = season,
            round = round,
            raceName = raceName,
            circuitId = circuitId,
            circuitName = circuitName,
            country = country,
            countryISO = countryISO,
            date = date
        )
        // TODO: Fix this
//        val deeplink = Screen.Weekend.with(screenWeekendData).route
        return Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            putExtra(SCREEN_PARAM, deeplink)
        }
    }
}