package tmg.flashback.repo.models.stats

import tmg.flashback.repo.models.stats.Constructor
import tmg.flashback.repo.models.stats.Driver

typealias ConstructorStandings = Map<String, Pair<Constructor, Map<String, Pair<Driver, Int>>>>

typealias DriverStandings = Map<String, Pair<RoundDriver, Int>>

data class RoundConstructorStandings(
    val points: Int,
    val constructor: Constructor
)

data class RoundDriverStandings(
    val points: Int,
    val driver: Driver
)