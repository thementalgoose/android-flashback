package tmg.flashback.flashbackapi.api.models.drivers

fun DriverHistoryStandingRaceSprint.Companion.model(
    qualifying: Boolean = true,
    race: Boolean = false
): DriverHistoryStandingRaceSprint = DriverHistoryStandingRaceSprint(
    qualifying = qualifying,
    race = race
)