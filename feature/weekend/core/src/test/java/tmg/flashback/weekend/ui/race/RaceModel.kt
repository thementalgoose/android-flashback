package tmg.flashback.weekend.ui.race

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.RaceResult
import tmg.flashback.formula1.model.model

fun RaceModel.DriverResult.Companion.model(
    result: RaceResult = RaceResult.model()
): RaceModel.DriverResult = RaceModel.DriverResult(
    result = result
)

fun RaceModel.ConstructorResult.Companion.model(
    constructor: Constructor = Constructor.model(),
    position: Int = 1,
    points: Double = 2.0,
    drivers: List<Pair<Driver, Double>> = listOf(
        Driver.model() to 2.0
    ),
    maxTeamPoints: Double = 2.0
): RaceModel.ConstructorResult = RaceModel.ConstructorResult(
    constructor = constructor,
    position = position,
    points = points,
    drivers = drivers,
    maxTeamPoints = maxTeamPoints
)