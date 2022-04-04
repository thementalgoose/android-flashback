package tmg.flashback.statistics.ui.weekend.race

import tmg.flashback.formula1.model.RaceRaceResult

sealed class RaceModel {

    data class Podium(
        val p1: RaceRaceResult,
        val p2: RaceRaceResult,
        val p3: RaceRaceResult
    ): RaceModel()

    data class Result(
        val result: RaceRaceResult
    ): RaceModel()
}