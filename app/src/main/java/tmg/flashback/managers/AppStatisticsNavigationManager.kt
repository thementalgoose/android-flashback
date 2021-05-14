package tmg.flashback.managers

import android.content.Context
import android.content.Intent
import tmg.aboutthisapp.AboutThisAppActivity
import tmg.common.constants.AboutThisAppConfig
import tmg.flashback.statistics.StatisticsNavigationManager
import tmg.flashback.ui.SplashActivity

class AppStatisticsNavigationManager: StatisticsNavigationManager {
    override fun relaunchAppIntent(context: Context): Intent {
        return Intent(context, SplashActivity::class.java)
    }

    override fun aboutAppIntent(context: Context): Intent {
        return AboutThisAppActivity.intent(context, AboutThisAppConfig.configuration(context, false, "1.0", "appName", "udid"))
    }
}