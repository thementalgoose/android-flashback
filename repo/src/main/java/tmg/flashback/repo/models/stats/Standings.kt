package tmg.flashback.repo.models.stats

import tmg.flashback.repo.models.stats.Constructor
import tmg.flashback.repo.models.stats.Driver

data class ConstructorStandings(
    val points: Int,
    val constructor: Constructor
)

data class DriverStandings(
    val points: Int,
    val driver: Driver
)