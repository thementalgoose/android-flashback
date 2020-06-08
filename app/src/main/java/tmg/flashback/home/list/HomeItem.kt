package tmg.flashback.home.list

import androidx.annotation.LayoutRes
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.R
import tmg.flashback.repo.models.Driver

sealed class HomeItem(
    @LayoutRes val layoutId: Int
) {
    data class Track(
        val season: Int,
        val raceName: String,
        val circuitName: String,
        val raceCountry: String,
        val raceCountryISO: String,
        val date: LocalDate,
        val round: Int
    ): HomeItem(R.layout.view_home_track)

    data class Driver(
        val points: Int,
        val driver: tmg.flashback.repo.models.Driver,
        val driverId: String = driver.id,
        val position: Int,
        val maxPointsInSeason: Int
    ): HomeItem(R.layout.view_home_driver)

    data class Constructor(
        val position: Int,
        val constructor: tmg.flashback.repo.models.Constructor,
        val constructorId: String = constructor.id,
        val driver: List<Pair<tmg.flashback.repo.models.Driver, Int>>,
        val points: Int,
        val maxPointsInSeason: Int
    ): HomeItem(R.layout.view_home_constructor)

    data class Loading(
        val id: Int
    ): HomeItem(R.layout.view_home_loading)

    object NoData: HomeItem(R.layout.view_home_no_data)

    object NoNetwork: HomeItem(R.layout.view_home_no_network)
}