package tmg.flashback.season.race

import tmg.flashback.repo.enums.RaceStatus
import tmg.flashback.repo.models.LapTime
import tmg.flashback.repo.models.RoundDriver
import tmg.flashback.repo.models.RoundQualifyingResult

sealed class RaceModel {
    data class Podium(
        val driverFirst: Single,
        val driverSecond: Single,
        val driverThird: Single
    ) : RaceModel()

    data class Single(
        val season: Int,
        val round: Int,
        val driver: RoundDriver,
        val q1: RoundQualifyingResult?,
        val q2: RoundQualifyingResult?,
        val q3: RoundQualifyingResult?,
        val racePoints: Int,
        val raceResult: LapTime,
        val racePos: Int,
        val gridPos: Int,
        val qualified: Int,
        val status: RaceStatus,
        val fastestLap: Boolean
    ) : RaceModel()

    object QualifyingHeader : RaceModel()
}