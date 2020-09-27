package tmg.flashback.repo.models.stats

typealias ConstructorStandings = Map<String, Triple<Constructor, Map<String, Pair<Driver, Int>>, Int>> // Constructor, driver id -> points per driver, constructor points

typealias DriverStandings = Map<String, Pair<RoundDriver, Int>>

data class RoundConstructorStandings(
    val points: Int,
    val constructor: Constructor
)

data class RoundDriverStandings(
    val points: Int,
    val driver: RoundDriver
)