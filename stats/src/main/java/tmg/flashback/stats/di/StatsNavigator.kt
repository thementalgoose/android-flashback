package tmg.flashback.stats.di

import android.content.Context
import android.content.Intent

@Deprecated("This is temporary!")
interface StatsNavigator {
    fun getRaceIntent(context: Context): Intent
}