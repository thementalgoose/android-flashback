package tmg.flashback.providers.model

import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.formula1.model.FastestLap
import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.RaceResult

fun RaceResult.Companion.model(
    driver: DriverEntry = DriverEntry.model(),
    time: LapTime? = LapTime.model(),
    points: Double = 1.0,
    grid: Int = 1,
    qualified: Int? = 1,
    finish: Int = 1,
    status: RaceStatus = RaceStatus.from("status"),
    fastestLap: FastestLap? = FastestLap.model(),
): RaceResult = RaceResult(
    entry = driver,
    time = time,
    points = points,
    grid = grid,
    qualified = qualified,
    finish = finish,
    status = status,
    fastestLap = fastestLap
)