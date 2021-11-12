package tmg.flashback.formula1.model

fun RaceQualifyingRoundDriver.Qualifying.Companion.model(
    driver: DriverConstructor = DriverConstructor.model(),
    lapTime: LapTime? = LapTime.model(),
    position: Int = 1,
): RaceQualifyingRoundDriver.Qualifying = RaceQualifyingRoundDriver.Qualifying(
    driver = driver,
    lapTime = lapTime,
    position = position
)

fun RaceQualifyingRoundDriver.SprintQualifying.Companion.model(
    driver: DriverConstructor = DriverConstructor.model(),
    lapTime: LapTime? = LapTime.model(),
    position: Int = 1,
    finished: Int = 1,
    gridPos: Int = 1,
    points: Double = 1.0,
    status: String = "Finished"
): RaceQualifyingRoundDriver.SprintQualifying = RaceQualifyingRoundDriver.SprintQualifying(
    driver = driver,
    lapTime = lapTime,
    position = position,
    finished = finished,
    gridPos = gridPos,
    points = points,
    status = status
)