package tmg.flashback.weekend.ui.race

import tmg.flashback.formula1.model.RaceRaceResult
import tmg.flashback.formula1.model.model

fun RaceModel.Result.Companion.model(
    result: RaceRaceResult = RaceRaceResult.model()
): RaceModel.Result = RaceModel.Result(
    result = result
)