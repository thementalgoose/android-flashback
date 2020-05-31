package tmg.flashback.standings

import androidx.annotation.LayoutRes
import tmg.flashback.R
import tmg.flashback.repo.models.Constructor
import tmg.flashback.repo.models.Driver

sealed class StandingsItem {
    data class Header(
        val season: Int
    ) : StandingsItem()
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

enum class StandingsItemType(
    @LayoutRes val layoutId: Int
) {
    HEADER(R.layout.view_standings_header),
    DRIVER(R.layout.view_standings_driver),
    CONSTRUCTOR(R.layout.view_standings_constructor),
}