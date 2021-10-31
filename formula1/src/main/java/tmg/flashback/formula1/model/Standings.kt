package tmg.flashback.formula1.model

typealias ConstructorStandingsRound = Map<String, Triple<Constructor, Map<String, Pair<ConstructorDriver, Double>>, Double>> // Constructor, driver id -> points per driver, constructor points

/**
 * Driver Id -> (Driver, double)
 */
typealias DriverStandingsRound = Map<String, Pair<ConstructorDriver, Double>>

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