package tmg.flashback.dashboard.swiping.season

import tmg.flashback.dashboard.swiping.season.DashboardSeasonViewType.*

sealed class DashboardSeasonAdapterItem(
    val viewType: DashboardSeasonViewType
) {
    data class Track(
        val season: Int,
        val round: Int,
        val circuitId: String,
        val trackName: String,
        val trackNationality: String,
        val trackISO: String
    ) : DashboardSeasonAdapterItem(TRACK)

    data class Constructor(
        val season: Int,
        val round: Int,
        val constructorId: String
    ) : DashboardSeasonAdapterItem(CONSTRUCTOR)

    data class Drivers(
        val season: Int,
        val round: Int,
        val driverId: String
    ) : DashboardSeasonAdapterItem(DRIVER)
}