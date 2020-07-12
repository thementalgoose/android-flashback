package tmg.flashback.home.list

import androidx.annotation.LayoutRes
import org.threeten.bp.LocalDate
import tmg.flashback.R
import tmg.flashback.repo.models.stats.CircuitSummary
import tmg.flashback.shared.viewholders.DataUnavailable

sealed class HomeItem(
    @LayoutRes val layoutId: Int
) {
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
    ): HomeItem(R.layout.view_home_track)

    data class Driver(
        val season: Int,
        val points: Int,
        val driver: tmg.flashback.repo.models.stats.Driver,
        val driverId: String = driver.id,
        val position: Int,
        val bestQualifying: Pair<Int, List<CircuitSummary>>?,
        val bestFinish: Pair<Int, List<CircuitSummary>>?,
        val maxPointsInSeason: Int
    ): HomeItem(R.layout.view_home_driver)

    data class Constructor(
        val season: Int,
        val position: Int,
        val constructor: tmg.flashback.repo.models.stats.Constructor,
        val constructorId: String = constructor.id,
        val driver: List<Pair<tmg.flashback.repo.models.stats.Driver, Int>>,
        val points: Int,
        val maxPointsInSeason: Int
    ): HomeItem(R.layout.view_home_constructor)

    object NoNetwork: HomeItem(R.layout.view_shared_no_network)

    object InternalError: HomeItem(R.layout.view_shared_internal_error)

    data class Message(
        val msg: String
    ): HomeItem(R.layout.view_shared_message)

    data class Unavailable(
        val type: DataUnavailable
    ): HomeItem(R.layout.view_shared_data_unavailable)
}