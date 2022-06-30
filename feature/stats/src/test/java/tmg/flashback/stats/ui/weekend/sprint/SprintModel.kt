package tmg.flashback.stats.ui.weekend.sprint

import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.RaceSprintResult
import tmg.flashback.formula1.model.model

fun SprintModel.Result.Companion.model(
    result: RaceSprintResult = RaceSprintResult.model(
        time = LapTime(0, 1, 2, 5)
    )
): SprintModel.Result = SprintModel.Result(
    result = result
)