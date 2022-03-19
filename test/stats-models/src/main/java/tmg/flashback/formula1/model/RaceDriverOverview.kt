package tmg.flashback.formula1.model

fun RaceDriverOverview.Companion.model(
    driver: DriverConstructor = DriverConstructor.model(),
    q1: RaceQualifyingResult.Qualifying? = RaceQualifyingResult.Qualifying.model(),
    q2: RaceQualifyingResult.Qualifying? = RaceQualifyingResult.Qualifying.model(),
    q3: RaceQualifyingResult.Qualifying? = RaceQualifyingResult.Qualifying.model(),
    qSprint: RaceQualifyingResult.SprintQualifying? = RaceQualifyingResult.SprintQualifying.model(),
    race: RaceRaceResult? = RaceRaceResult.model(),
): RaceDriverOverview = RaceDriverOverview(
    driver = driver,
    q1 = q1,
    q2 = q2,
    q3 = q3,
    qSprint = qSprint,
    race = race
)