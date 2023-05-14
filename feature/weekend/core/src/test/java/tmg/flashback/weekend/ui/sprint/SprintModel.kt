package tmg.flashback.weekend.ui.sprint

import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.SprintRaceResult
import tmg.flashback.formula1.model.model

fun SprintModel.DriverResult.Companion.model(
    result: SprintRaceResult = SprintRaceResult.model(
        time = LapTime(1, 2, 3, 4)
    )
): SprintModel.DriverResult = SprintModel.DriverResult(
    result = result
)