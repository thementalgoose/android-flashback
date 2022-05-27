package tmg.flashback.stats.ui.weekend.constructor

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.model

fun ConstructorModel.Constructor.Companion.model(
    constructor: Constructor = Constructor.model(),
    position: Int? = 1,
    points: Double = 2.0,
    drivers: List<Pair<Driver, Double>> = listOf(
        Driver.model() to 2.0
    ),
    maxTeamPoints: Double = 2.0,
): ConstructorModel.Constructor = ConstructorModel.Constructor(
    constructor = constructor,
    position = position,
    points = points,
    drivers = drivers,
    maxTeamPoints = maxTeamPoints
)