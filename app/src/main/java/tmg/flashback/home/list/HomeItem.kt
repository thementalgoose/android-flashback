package tmg.flashback.home.list

import androidx.annotation.LayoutRes
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.R

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
        val time: LocalTime?,
        val round: Int
    ): HomeItem(R.layout.view_home_track)

    data class Driver(
        val driverId: String
    ): HomeItem(R.layout.view_home_driver)

    data class Constructor(
        val constructorId: String
    ): HomeItem(R.layout.view_home_constructor)
}