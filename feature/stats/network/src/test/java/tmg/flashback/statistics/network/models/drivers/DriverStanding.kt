package tmg.flashback.statistics.network.models.drivers

fun DriverStanding.Companion.model(
    season: Int = 2020,
    championshipPosition: Int = 1,
    races: Map<String, DriverStandingRace> = mapOf(
        "r1" to DriverStandingRace.model()
    )
): DriverStanding = DriverStanding(
    season = season,
    championshipPosition = championshipPosition,
    races = races
)