package tmg.flashback.statistics.network.models.races

fun DriverStandings.Companion.model(
    driverId: String = "driverId",
    points: Double = 1.0,
    inProgress: Boolean? = true,
    races: Int = 1,
    position: Int? = 1,
    constructors: Map<String, Double> = mapOf(
        "constructorId" to 1.0
    )
): DriverStandings = DriverStandings(
    driverId = driverId,
    points = points,
    inProgress = inProgress,
    races = races,
    position = position,
    constructors = constructors
)