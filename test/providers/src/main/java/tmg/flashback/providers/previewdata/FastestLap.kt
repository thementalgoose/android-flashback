package tmg.flashback.providers.previewdata

import tmg.flashback.formula1.model.FastestLap
import tmg.flashback.formula1.model.LapTime

internal fun FastestLap.Companion.model(
    rank: Int = 1,
    lapTime: LapTime = LapTime.model(0, 1, 1, 1),
): FastestLap = FastestLap(
    rank = rank,
    lapTime = lapTime
)