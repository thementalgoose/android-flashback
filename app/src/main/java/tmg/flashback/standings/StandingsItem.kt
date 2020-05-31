package tmg.flashback.standings

import androidx.annotation.LayoutRes
import tmg.flashback.R
import tmg.flashback.repo.models.Constructor
import tmg.flashback.repo.models.Driver

sealed class StandingsItem {
    data class Header(
        val season: Int,
        val raceScheduled: Int,
        val raceCompleted: Int
    ) : StandingsItem()
    object Skeleton: StandingsItem()
    data class Constructor(
        val constructor: tmg.flashback.repo.models.Constructor,
        val driver: List<Pair<tmg.flashback.repo.models.Driver, Int>>,
        val points: Int
    ): StandingsItem()
    data class Driver(
        val driver: tmg.flashback.repo.models.Driver,
        val points: Int
    ): StandingsItem()
}

enum class StandingsItemType(
    @LayoutRes val layoutId: Int
) {
    HEADER(R.layout.view_standings_header),
    SKELETON(R.layout.view_standings_skeleton),
    DRIVER(R.layout.view_standings_driver),
    CONSTRUCTOR(R.layout.view_standings_constructor),
}