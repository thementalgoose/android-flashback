package tmg.flashback.driver.season

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import org.threeten.bp.LocalDate
import tmg.flashback.R
import tmg.flashback.driver.overview.DriverOverviewItem
import tmg.flashback.repo.enums.RaceStatus
import tmg.flashback.repo.models.stats.Constructor
import tmg.flashback.repo.models.stats.Driver
import tmg.flashback.repo.models.stats.SlimConstructor
import tmg.flashback.shared.SyncDataItem

sealed class DriverSeasonItem(
        @LayoutRes val layoutId: Int
) {
    data class Stat(
        @DrawableRes
        val icon: Int,
        @StringRes
        val label: Int,
        val value: String
    ): DriverSeasonItem(
        R.layout.view_driver_overview_stat
    )

    data class Result(
            val season: Int,
            val round: Int,
            val raceName: String,
            val circuitName: String,
            val circuitId: String,
            val raceCountry: String,
            val raceCountryISO: String,
            val date: LocalDate,
            val constructor: SlimConstructor,
            val qualified: Int,
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