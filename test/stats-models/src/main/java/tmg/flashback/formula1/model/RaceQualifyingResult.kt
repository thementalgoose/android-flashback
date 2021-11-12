package tmg.flashback.formula1.model

fun RaceQualifyingResult_Legacy.Companion.model(
    driver: DriverConstructor = DriverConstructor.model(),
    time: LapTime? = LapTime.model(),
    position: Int = 1,
): RaceQualifyingResult_Legacy = RaceQualifyingResult_Legacy(
    driver = driver,
    time = time,
    position = position
)