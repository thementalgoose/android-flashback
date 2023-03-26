package tmg.flashback.weekend.ui.race

import tmg.flashback.formula1.model.RaceRaceResult
import tmg.flashback.formula1.model.model

fun tmg.flashback.weekend.ui.race.RaceModel.Result.Companion.model(
    result: RaceRaceResult = RaceRaceResult.model()
): tmg.flashback.weekend.ui.race.RaceModel.Result = tmg.flashback.weekend.ui.race.RaceModel.Result(
    result = result
)