package tmg.flashback.statistics.ui.weekend.constructor

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver

data class ConstructorModel(
    val constructor: Constructor,
    val position: Int?,
    val points: Double,
    val drivers: List<Pair<Driver, Double>>,
    val maxTeamPoints: Double
)