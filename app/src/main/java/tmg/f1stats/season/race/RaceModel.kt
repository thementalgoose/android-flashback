package tmg.f1stats.season.race

import tmg.f1stats.repo.enums.RaceStatus
import tmg.f1stats.repo.models.LapTime
import tmg.f1stats.repo.models.RoundDriver
import tmg.f1stats.repo.models.RoundQualifyingResult

open class RaceModel(
    val driver: RoundDriver,
    val q1: RoundQualifyingResult?,
    val q2: RoundQualifyingResult?,
    val q3: RoundQualifyingResult?,
    val racePoints: Int,
    val raceResult: LapTime,
    val racePos: Int,
    val gridPos: Int,
    val status: RaceStatus,
    val fastestLap: Boolean
)