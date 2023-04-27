package tmg.flashback.weekend.ui.sprint

import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.SprintRaceResult
import tmg.flashback.formula1.model.model

fun SprintModel.Result.Companion.model(
    result: SprintRaceResult = SprintRaceResult.model(
        time = LapTime(0, 1, 2, 5)
    )
): SprintModel.Result = SprintModel.Result(
    result = result
)