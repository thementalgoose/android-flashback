package tmg.flashback.statistics.room.models.standings

fun DriverStanding.Companion.model(
    driverId: String = "driverId",
    season: Int = 2020,
    points: Double = 1.0,
    position: Int? = 1,
    inProgress: Boolean = true,
    races: Int = 1,
): DriverStanding = DriverStanding(
    driverId = driverId,
    season = season,
    points = points,
    position = position,
    inProgress = inProgress,
    races = races
)