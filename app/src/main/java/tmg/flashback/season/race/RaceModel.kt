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
        val qualified: Int?,
        val status: RaceStatus,
        val fastestLap: Boolean,
        val q1Delta: String?,
        val q2Delta: String?,
        val q3Delta: String?,
        val showQualifying: ShowQualifying
    ) : RaceModel()

    data class RaceHeader(
        val season: Int,
        val round: Int
    ) : RaceModel()

    data class QualifyingHeader(
        val showQualifyingDeltas: ShowQualifying
    ) : RaceModel()

    object RacePlaceholder : RaceModel()
}

data class ShowQualifying(
    val q1: Boolean,
    val q2: Boolean,
    val q3: Boolean,
    val deltas: Boolean
)