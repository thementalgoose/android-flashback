package tmg.flashback.formula1.model

fun RaceQualifyingRoundDriver.Companion.model(
    driver: DriverConstructor = DriverConstructor.model(),
    lapTime: LapTime? = LapTime.model(),
    position: Int = 1,
): RaceQualifyingRoundDriver = RaceQualifyingRoundDriver(
    driver = driver,
    lapTime = lapTime,
    position = position
)