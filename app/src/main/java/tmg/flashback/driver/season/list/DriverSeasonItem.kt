package tmg.flashback.driver.season.list

import androidx.annotation.LayoutRes
import org.threeten.bp.LocalDate
import tmg.flashback.R
import tmg.flashback.repo.enums.RaceStatus
import tmg.flashback.repo.models.stats.Constructor
import tmg.flashback.shared.viewholders.DataUnavailable

sealed class DriverSeasonItem(
        @LayoutRes val layoutId: Int
) {

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
            val finished: Int?,
            val raceStatus: RaceStatus,
            val points: Int,
            val maxPoints: Int
    ): DriverSeasonItem(R.layout.view_driver_season)

    object ResultHeader: DriverSeasonItem(R.layout.view_driver_season_header)

    object NoNetwork: DriverSeasonItem(R.layout.view_shared_no_network)

    object InternalError: DriverSeasonItem(R.layout.view_shared_internal_error)

    data class Message(
            val msg: String
    ): DriverSeasonItem(R.layout.view_shared_message)

    data class Unavailable(
            val type: DataUnavailable
    ): DriverSeasonItem(R.layout.view_shared_data_unavailable)
}