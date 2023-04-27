package tmg.flashback.formula1.model

import tmg.flashback.formula1.enums.RaceStatus

fun SprintRaceResult.Companion.model(
    driver: DriverConstructor = DriverConstructor.model(),
    time: LapTime? = LapTime.model(),
    points: Double = 1.0,
    grid: Int? = 1,
    finish: Int = 1,
    status: RaceStatus = RaceStatus.from("status")
): SprintRaceResult = SprintRaceResult(
    driver = driver,
    time = time,
    points = points,
    grid = grid,
    finish = finish,
    status = status
)