package tmg.flashback.formula1.model

@Deprecated("Should no longer be used", ReplaceWith("SeasonConstructorStandings"))
typealias ConstructorStandingsRound = Map<String, Triple<Constructor, Map<String, Pair<ConstructorDriver, Double>>, Double>> // Constructor, driver id -> points per driver, constructor points

/**
 * Driver Id -> (Driver, double)
 */
@Deprecated("Should no longer be used", ReplaceWith("SeasonDriverStandings"))
typealias DriverStandingsRound = Map<String, Pair<ConstructorDriver, Double>>

@Deprecated("Should no longer be used", ReplaceWith("SeasonConstructorStandings"))
typealias ConstructorStandings = List<SeasonStanding<Constructor>>

@Deprecated("Should no longer be used", ReplaceWith("SeasonDriverStandings"))
typealias DriverStandings = List<SeasonStanding<DriverWithEmbeddedConstructor>>

@Deprecated("Should no longer be used", ReplaceWith("SeasonDriverStandings"))
data class RoundDriverStandings(
    val points: Double,
    val driver: ConstructorDriver
)

@Deprecated("Should no longer be used", ReplaceWith("SeasonDriverStandings"))
data class SeasonStanding<T>(
    val item: T,
    val points: Double,
    val position: Int
)