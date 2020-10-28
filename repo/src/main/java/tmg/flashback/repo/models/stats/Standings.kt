package tmg.flashback.repo.models.stats

typealias ConstructorStandingsRound = Map<String, Triple<Constructor, Map<String, Pair<Driver, Int>>, Int>> // Constructor, driver id -> points per driver, constructor points

typealias ConstructorStandings = Map<String, Pair<Constructor, Int>>

typealias DriverStandingsRound = Map<String, Pair<RoundDriver, Int>>

typealias DriverStandings = Map<String, Pair<Driver, Int>>

data class RoundConstructorStandings(
    val points: Int,
    val constructor: Constructor
)

data class RoundDriverStandings(
    val points: Int,
    val driver: RoundDriver
)