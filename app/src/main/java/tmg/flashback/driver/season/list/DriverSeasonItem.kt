package tmg.flashback.driver.season.list

import androidx.annotation.LayoutRes
import org.threeten.bp.LocalDate
import tmg.flashback.R
import tmg.flashback.repo.enums.RaceStatus
import tmg.flashback.repo.models.stats.Constructor
import tmg.flashback.repo.models.stats.Driver
import tmg.flashback.shared.SyncDataItem

sealed class DriverSeasonItem(
        @LayoutRes val layoutId: Int
) {
    data class Header(
            val driver: Driver,
            val constructors: List<Constructor>
    ): DriverSeasonItem(R.layout.view_driver_header)

    data class Result(
            val season: Int,
            val round: Int,
            val raceName: String,
            val circuitName: String,
            val circuitId: String,
            val raceCountry: String,
            val raceCountryISO: String,
            val date: LocalDate,
            val constructor: Constructor,
            val qualified: Int,
            val gridPos: Int,
            val finished: Int?,
            val raceStatus: RaceStatus,
            val points: Int,
            val maxPoints: Int
    ): DriverSeasonItem(R.layout.view_driver_season)

    object ResultHeader: DriverSeasonItem(R.layout.view_driver_season_header)

    data class ErrorItem(
        val item: SyncDataItem
    ): DriverSeasonItem(item.layoutId)
}

fun MutableList<DriverSeasonItem>.addError(syncDataItem: SyncDataItem) {
    this.add(DriverSeasonItem.ErrorItem(syncDataItem))
}