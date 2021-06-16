package tmg.flashback.data.models.stats

typealias ConstructorStandingsRound = Map<String, Triple<Constructor, Map<String, Pair<Driver, Int>>, Int>> // Constructor, driver id -> points per driver, constructor points

typealias DriverStandingsRound = Map<String, Pair<RoundDriver, Int>>

typealias ConstructorStandings = List<SeasonStanding<Constructor>>

typealias DriverStandings = List<SeasonStanding<Driver>>

data class RoundConstructorStandings(
    val points: Int,
    val constructor: Constructor
)

data class RoundDriverStandings(
    val points: Int,
    val driver: RoundDriver
)

data class SeasonStanding<T>(
    val item: T,
    val points: Int,
    val position: Int
)