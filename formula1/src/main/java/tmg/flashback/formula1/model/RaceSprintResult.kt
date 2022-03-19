package tmg.flashback.formula1.model

import tmg.flashback.formula1.enums.RaceStatus

data class RaceSprintResult(
    val driver: DriverConstructor,
    val time: LapTime?,
    val points: Double,
    val grid: Int?,
    val qualified: Int?,
    val finish: Int,
    val status: RaceStatus
) {
    companion object
}