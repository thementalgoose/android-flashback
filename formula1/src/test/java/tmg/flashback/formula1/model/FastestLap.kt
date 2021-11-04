package tmg.flashback.formula1.model

fun FastestLap.Companion.model(
    rank: Int = 1,
    lapTime: LapTime = LapTime.model(),
): FastestLap = FastestLap(
    rank = rank,
    lapTime = lapTime
)