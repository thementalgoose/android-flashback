package tmg.flashback.formula1.model

fun RaceDriverOverview.Companion.model(
    driver: DriverConstructor = DriverConstructor.model(),
    q1: RaceQualifyingRoundDriver.Qualifying? = RaceQualifyingRoundDriver.Qualifying.model(),
    q2: RaceQualifyingRoundDriver.Qualifying? = RaceQualifyingRoundDriver.Qualifying.model(),
    q3: RaceQualifyingRoundDriver.Qualifying? = RaceQualifyingRoundDriver.Qualifying.model(),
    qSprint: RaceQualifyingRoundDriver.SprintQualifying? = RaceQualifyingRoundDriver.SprintQualifying.model(),
    race: RaceRaceResult? = RaceRaceResult.model(),
): RaceDriverOverview = RaceDriverOverview(
    driver = driver,
    q1 = q1,
    q2 = q2,
    q3 = q3,
    qSprint = qSprint,
    race = race
)