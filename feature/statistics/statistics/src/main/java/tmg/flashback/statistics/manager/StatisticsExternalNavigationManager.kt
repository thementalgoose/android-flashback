package tmg.flashback.statistics.manager

import android.content.Context
import android.content.Intent

interface StatisticsExternalNavigationManager {
    fun getRSSIntent(context: Context): Intent
}