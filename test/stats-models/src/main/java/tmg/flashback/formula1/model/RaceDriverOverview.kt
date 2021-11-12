package tmg.flashback.formula1.model

fun RaceDriverOverview.Companion.model(
    driver: DriverConstructor = DriverConstructor.model(),
    q1: RaceQualifyingResult_Legacy? = RaceQualifyingResult_Legacy.model(),
    q2: RaceQualifyingResult_Legacy? = RaceQualifyingResult_Legacy.model(),
    q3: RaceQualifyingResult_Legacy? = RaceQualifyingResult_Legacy.model(),
    qSprint: RaceSprintQualifyingResult_Legacy? = RaceSprintQualifyingResult_Legacy.model(),
    race: RaceRaceResult? = RaceRaceResult.model()
): RaceDriverOverview = RaceDriverOverview(
    driver = driver,
    q1 = q1,
    q2 = q2,
    q3 = q3,
    qSprint = qSprint,
    race = race
)