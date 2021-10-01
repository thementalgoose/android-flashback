package tmg.flashback.data.models.stats

typealias ConstructorStandingsRound = Map<String, Triple<Constructor, Map<String, Pair<Driver, Double>>, Double>> // Constructor, driver id -> points per driver, constructor points

typealias DriverStandingsRound = Map<String, Pair<Driver, Double>>

typealias ConstructorStandings = List<SeasonStanding<Constructor>>

typealias DriverStandings = List<SeasonStanding<Driver>>

data class RoundConstructorStandings(
    val points: Double,
    val constructor: Constructor
)

data class RoundDriverStandings(
    val points: Double,
    val driver: ConstructorDriver
)

data class SeasonStanding<T>(
    val item: T,
    val points: Double,
    val position: Int
)