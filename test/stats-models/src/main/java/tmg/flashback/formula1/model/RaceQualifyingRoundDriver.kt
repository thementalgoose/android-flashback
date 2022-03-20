package tmg.flashback.formula1.model

fun RaceQualifyingResult.Companion.model(
    driver: DriverConstructor = DriverConstructor.model(),
    lapTime: LapTime? = LapTime.model(),
    position: Int = 1,
): RaceQualifyingResult = RaceQualifyingResult(
    driver = driver,
    lapTime = lapTime,
    position = position
)