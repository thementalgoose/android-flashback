package tmg.flashback.formula1.model

import tmg.flashback.formula1.model.DriverConstructor

data class ConstructorHistorySeasonDriver(
    val driver: DriverConstructor,
    val points: Double,
    val wins: Int,
    val races: Int,
    val podiums: Int,
    val finishesInPoints: Int,
    val polePosition: Int,
    val championshipStanding: Int?
)