package tmg.flashback.statistics.network.models.drivers

fun DriverConstructorStandings.Companion.model(
    season: Int = 2020,
    championshipPosition: Int = 1,
    races: Map<String, DriverStandingRace> = mapOf(
        "r1" to DriverStandingRace.model()
    ),
    constructors: Map<String, Double> = mapOf(
        "constructorId" to 1.0
    ),
): DriverConstructorStandings = DriverConstructorStandings(
    season = season,
    championshipPosition = championshipPosition,
    races = races,
    constructors = constructors,
)