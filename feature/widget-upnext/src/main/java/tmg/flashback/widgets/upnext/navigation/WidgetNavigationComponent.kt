package tmg.flashback.widgets.upnext.navigation

import android.content.Context
import android.content.Intent
import java.time.LocalDate

interface WidgetNavigationComponent {
    fun getLaunchAppIntent(context: Context): Intent
    fun getLaunchAppWithSeasonRoundIntent(
        context: Context,
        season: Int,
        round: Int,
        raceName: String,
        circuitId: String,
        circuitName: String,
        country: String,
        countryISO: String,
        date: LocalDate
    ): Intent
}