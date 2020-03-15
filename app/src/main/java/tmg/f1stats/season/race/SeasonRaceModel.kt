package tmg.f1stats.season.race

import tmg.f1stats.repo.enums.RaceStatus
import tmg.f1stats.repo.models.Driver
import tmg.f1stats.repo.models.DriverOnWeekend
import tmg.f1stats.repo.models.LapTime
import tmg.f1stats.repo.models.QualifyingResult

open class SeasonRaceModel(
    val driver: DriverOnWeekend,
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
    val qualiGridPos: Int
        get() {
            if (q3Pos ?: Int.MAX_VALUE >= 15) {

            }
        }

    val qualiGridTime: Int
        get() {

        }
}