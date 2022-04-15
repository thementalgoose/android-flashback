package tmg.flashback.formula1.model

import tmg.flashback.formula1.enums.RaceStatus

fun RaceRaceResult.Companion.model(
    driver: DriverConstructor = DriverConstructor.model(),
    time: LapTime? = LapTime.model(),
    points: Double = 1.0,
    grid: Int = 1,
    qualified: Int? = 1,
    finish: Int = 1,
    status: RaceStatus = "status",
    fastestLap: FastestLap? = FastestLap.model(),
): RaceRaceResult = RaceRaceResult(
    driver = driver,
    time = time,
    points = points,
    grid = grid,
    qualified = qualified,
    finish = finish,
    status = status,
    fastestLap = fastestLap
)