package tmg.flashback.formula1.model

fun Race.Companion.model(
    raceInfo: RaceInfo = RaceInfo.model(),
    q1: Map<String, RaceQualifyingResult> = mapOf(
        "driverId" to RaceQualifyingResult.model(time = LapTime.model(0, 1, 2, 1))
    ),
    q2: Map<String, RaceQualifyingResult> = mapOf(
        "driverId" to RaceQualifyingResult.model(time = LapTime.model(0, 1, 2, 2))
    ),
    q3: Map<String, RaceQualifyingResult> = mapOf(
        "driverId" to RaceQualifyingResult.model(time = LapTime.model(0, 1, 2, 3))
    ),
    qSprint: Map<String, RaceSprintQualifyingResult> = mapOf(
        "driverId" to RaceSprintQualifyingResult.model()
    ),
    race: Map<String, RaceRaceResult> = mapOf(
        "driverId" to RaceRaceResult.model()
    ),
    schedule: List<Schedule> = listOf(
        Schedule.model()
    )
): Race = Race(
    raceInfo = raceInfo,
    q1 = q1,
    q2 = q2,
    q3 = q3,
    qSprint = qSprint,
    race = race,
    schedule = schedule
)