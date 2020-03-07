package tmg.f1stats.repo.models

import tmg.f1stats.repo.enums.RaceStatus

data class RaceResult(
    val driver: DriverOnWeekend,
    val gridPosition: Int,
    val status: RaceStatus,
    val finishPosition: Int,
    val finishPositionText: String,
    val time: LapTime
)