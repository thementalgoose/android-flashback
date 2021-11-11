package tmg.flashback.statistics.ui.dashboard.season

import androidx.annotation.LayoutRes
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import tmg.flashback.statistics.R
import tmg.flashback.ui.model.AnimationSpeed
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem

sealed class SeasonItem(
        @LayoutRes val layoutId: Int
) {
    object CalendarHeader: SeasonItem(R.layout.view_dashboard_season_calendar_header)

    data class CalendarMonth(
        val month: Month,
        val year: Int
    ): SeasonItem(R.layout.view_dashboard_season_calendar_month)

    data class CalendarWeek(
        val forMonth: Month,
        val startingDay: LocalDate,
        val race: OverviewRace?
    ): SeasonItem(R.layout.view_dashboard_season_calendar_week)

    data class Track(
        val season: Int,
        val raceName: String,
        val circuitName: String,
        val circuitId: String,
        val raceCountry: String,
        val raceCountryISO: String,
        val date: LocalDate,
        val round: Int,
        val hasQualifying: Boolean,
        val hasResults: Boolean
    ) : SeasonItem(R.layout.view_dashboard_season_track)

    data class Driver(
        val season: Int,
        val points: Double,
        val driver: tmg.flashback.formula1.model.Driver,
        val constructors: List<tmg.flashback.formula1.model.Constructor>,
        val driverId: String = driver.id,
        val position: Int,
        val maxPointsInSeason: Double,
        val animationSpeed: AnimationSpeed
    ) : SeasonItem(R.layout.view_dashboard_season_driver)

    data class Constructor(
        val season: Int,
        val position: Int,
        val constructor: tmg.flashback.formula1.model.Constructor,
        val constructorId: String = constructor.id,
        val driver: List<Pair<tmg.flashback.formula1.model.Driver, Double>>,
        val points: Double,
        val maxPointsInSeason: Double,
        val barAnimation: AnimationSpeed
    ) : SeasonItem(R.layout.view_dashboard_season_constructor)

    data class ErrorItem(
            val item: SyncDataItem
    ) : SeasonItem(item.layoutId)

    companion object
}

fun MutableList<SeasonItem>.addError(item: SyncDataItem) {
    this.add(SeasonItem.ErrorItem(item))
}