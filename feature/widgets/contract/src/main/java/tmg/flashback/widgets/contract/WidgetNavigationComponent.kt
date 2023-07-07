package tmg.flashback.widgets.contract

import android.content.Context
import android.content.Intent
import org.threeten.bp.LocalDate

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