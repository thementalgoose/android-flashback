package tmg.flashback.statistics.room.models.drivers

fun DriverSeasonWithRaces.Companion.model(
    driverSeason: DriverSeason = DriverSeason.model(),
    races: List<DriverSeasonRaceWithConstructor> = listOf(
        DriverSeasonRaceWithConstructor.model()
    )
): DriverSeasonWithRaces = DriverSeasonWithRaces(
    driverSeason = driverSeason,
    races = races
)