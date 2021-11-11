package tmg.flashback.formula1.model

import tmg.flashback.formula1.enums.RaceStatus

fun RaceSprintQualifyingResult.Companion.model(
    driver: DriverConstructor = DriverConstructor.model(),
    time: LapTime? = LapTime.model(0,1,2,5),
    points: Double = 1.0,
    grid: Int = 1,
    qualified: Int? = 1,
    finish: Int = 1,
    status: RaceStatus = "status",
): RaceSprintQualifyingResult = RaceSprintQualifyingResult(
    driver = driver,
    time = time,
    points = points,
    grid = grid,
    qualified = qualified,
    finish = finish,
    status = status,
)