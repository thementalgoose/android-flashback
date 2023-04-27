package tmg.flashback.weekend.ui.race

import tmg.flashback.formula1.model.RaceResult
import tmg.flashback.formula1.model.model

fun RaceModel.Result.Companion.model(
    result: RaceResult = RaceResult.model()
): RaceModel.Result = RaceModel.Result(
    result = result
)