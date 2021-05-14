package tmg.flashback.statistics

import android.content.Context
import android.content.Intent

interface StatisticsNavigationManager {
    fun relaunchAppIntent(context: Context): Intent
    fun aboutAppIntent(context: Context): Intent
}