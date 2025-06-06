package tmg.flashback.providers.model

import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.SprintQualifyingResult
import tmg.flashback.formula1.model.SprintQualifyingRound
import tmg.flashback.formula1.model.SprintQualifyingType
import tmg.flashback.formula1.model.SprintRaceResult
import tmg.flashback.formula1.model.SprintResult

fun SprintResult.Companion.model(
    qualifying: List<SprintQualifyingRound> = listOf(
        SprintQualifyingRound.model(label = SprintQualifyingType.SQ1, order = 1, results = listOf(
            SprintQualifyingResult.model(lapTime = LapTime.model(0, 1, 2, 1))
        )),
        SprintQualifyingRound.model(label = SprintQualifyingType.SQ2, order = 2, results = listOf(
            SprintQualifyingResult.model(lapTime = LapTime.model(0, 1, 2, 2))
        )),
        SprintQualifyingRound.model(label = SprintQualifyingType.SQ3, order = 3, results = listOf(
            SprintQualifyingResult.model(lapTime = LapTime.model(0, 1, 2, 3))
        ))
    ),
    race: List<SprintRaceResult> = listOf(SprintRaceResult.model())
): SprintResult = SprintResult(
    qualifying = qualifying,
    race = race
)