package tmg.flashback.ui.overviews.driver.season

import androidx.annotation.*
import org.threeten.bp.LocalDate
import tmg.flashback.R
import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.ui.overviews.driver.summary.PipeType
import tmg.flashback.data.enums.RaceStatus
import tmg.flashback.data.models.stats.SlimConstructor
import tmg.flashback.ui.shared.sync.SyncDataItem

sealed class DriverSeasonItem(
        @LayoutRes val layoutId: Int
) {
    data class Stat(
        @AttrRes
        val tint: Int = R.attr.f1TextSecondary,
        @DrawableRes
        val icon: Int,
        @StringRes
        val label: Int,
        val value: String
    ): DriverSeasonItem(
        R.layout.view_overview_stat
    )

    data class RacedFor(
            val season: Int?, // Null = hide season
            val constructors: SlimConstructor,
            val type: PipeType,
            val isChampionship: Boolean
    ): DriverSeasonItem(
            R.layout.view_driver_summary_history
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
            val showConstructorLabel: Boolean,
            val constructor: SlimConstructor,
            val qualified: Int,
            val finished: Int?,
            val raceStatus: RaceStatus,
            val points: Int,
            val maxPoints: Int,
            val animationSpeed: AnimationSpeed
    ): DriverSeasonItem(R.layout.view_driver_season)

    object ResultHeader: DriverSeasonItem(R.layout.view_driver_season_header)

    data class ErrorItem(
        val item: SyncDataItem
    ): DriverSeasonItem(item.layoutId)
}

fun MutableList<DriverSeasonItem>.addError(syncDataItem: SyncDataItem) {
    this.add(DriverSeasonItem.ErrorItem(syncDataItem))
}