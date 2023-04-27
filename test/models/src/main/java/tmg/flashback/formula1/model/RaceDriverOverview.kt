package tmg.flashback.formula1.model

fun RaceDriverOverview.Companion.model(
    driver: DriverConstructor = DriverConstructor.model(),
    q1: QualifyingResult? = QualifyingResult.model(),
    q2: QualifyingResult? = QualifyingResult.model(),
    q3: QualifyingResult? = QualifyingResult.model(),
    qSprint: SprintRaceResult? = SprintRaceResult.model(),
    race: RaceResult? = RaceResult.model(),
): RaceDriverOverview = RaceDriverOverview(
    driver = driver,
    q1 = q1,
    q2 = q2,
    q3 = q3,
    qSprint = qSprint,
    race = race
)