package tmg.flashback.statistics.network.models.races

fun Race.Companion.model(
    data: RaceData = RaceData.model(),
    race: Map<String, RaceResult> = mapOf(
        "driverId" to RaceResult.model()
    ),
    qualifying: Map<String, QualifyingResult> = mapOf(
        "driverId" to QualifyingResult.model()
    )
): Race = Race(
    data = data,
    race = race,
    qualifying = qualifying
)