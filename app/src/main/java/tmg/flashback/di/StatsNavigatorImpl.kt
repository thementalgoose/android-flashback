package tmg.flashback.di

import android.content.Context
import android.content.Intent
import tmg.flashback.statistics.ui.race.RaceActivity
import tmg.flashback.stats.di.StatsNavigator

@Deprecated("This is temporary!")
class StatsNavigatorImpl: StatsNavigator {
    override fun getRaceIntent(context: Context): Intent {
        return Intent(context, RaceActivity::class.java)
//        return RaceActivity.intent(context)
    }
}