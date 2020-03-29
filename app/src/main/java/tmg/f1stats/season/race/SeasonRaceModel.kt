package tmg.f1stats.season.race

import tmg.f1stats.repo.enums.RaceStatus
import tmg.f1stats.repo.models.LapTime
import tmg.f1stats.repo.models.RoundDriver

open class SeasonRaceModel(
    val driver: RoundDriver,
    val q1: LapTime,
    val q1Pos: Int,
    val q2: LapTime?,
    val q2Pos: Int?,
    val q3: LapTime?,
    val q3Pos: Int?,
    val raceResult: LapTime,
    val racePos: Int,
    val gridPos: Int,
    val status: RaceStatus
) {
    val qualiGridTime: LapTime?
        get() {
            return when {
                q3 != null -> q3
                q2 != null -> q2
                else -> q1
            }
        }
}