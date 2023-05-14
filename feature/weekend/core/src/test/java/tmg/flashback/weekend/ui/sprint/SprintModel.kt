package tmg.flashback.weekend.ui.sprint

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
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

fun SprintModel.ConstructorResult.Companion.model(
    constructor: Constructor = Constructor.model(),
    position: Int? = 1,
    points: Double = 1.0,
    drivers: List<Pair<Driver, Double>> = listOf(
        Driver.model() to 1.0
    ),
    maxTeamPoints: Double = 15.0,
    highestDriverPosition: Int = 1,
): SprintModel.ConstructorResult = SprintModel.ConstructorResult(
    constructor = constructor,
    position = position,
    points = points,
    drivers = drivers,
    maxTeamPoints = maxTeamPoints,
    highestDriverPosition = highestDriverPosition
)