package tmg.flashback.standings

import tmg.flashback.repo.models.Constructor
import tmg.flashback.repo.models.Driver

sealed class StandingsItem {
    object Header : StandingsItem()
    data class Constructor(
        val constructor: tmg.flashback.repo.models.Constructor,
        val driver: List<DriverPoints>,
        val points: Int
    ): StandingsItem()
    data class Driver(
        val driver: DriverPoints
    ): StandingsItem()
}

data class DriverPoints(
    val driver: Driver,
    val points: Int
)