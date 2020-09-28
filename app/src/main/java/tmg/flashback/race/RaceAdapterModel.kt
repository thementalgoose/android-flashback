package tmg.flashback.race

import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.enums.RaceStatus
import tmg.flashback.repo.models.stats.*
import tmg.flashback.shared.viewholders.DataUnavailable

sealed class RaceAdapterModel {
    data class Podium(
        val driverFirst: Single,
        val driverSecond: Single,
        val driverThird: Single
    ) : RaceAdapterModel()

    data class Single(
        val season: Int,
        val round: Int,
        val driver: RoundDriver,
        val q1: RoundQualifyingResult?,
        val q2: RoundQualifyingResult?,
        val q3: RoundQualifyingResult?,
        val race: SingleRace?,
        val qualified: Int?,
        val q1Delta: String?,
        val q2Delta: String?,
        val q3Delta: String?,
        val showQualifying: ShowQualifying
    ) : RaceAdapterModel()

    data class RaceHeader(
        val season: Int,
        val round: Int
    ) : RaceAdapterModel()

    data class QualifyingHeader(
        val showQualifyingDeltas: ShowQualifying
    ) : RaceAdapterModel()

    data class ConstructorStandings(
        val constructor: Constructor,
        val points: Int,
        val driver: List<Pair<Driver, Int>>,
        val barAnimation: BarAnimation
    ) : RaceAdapterModel()

    data class Unavailable(
        val type: DataUnavailable
    ) : RaceAdapterModel()

    object Loading : RaceAdapterModel()

    object NoNetwork : RaceAdapterModel()
}

data class SingleRace(
    val points: Int,
    val result: LapTime,
    val pos: Int,
    val gridPos: Int,
    val status: RaceStatus,
    val fastestLap: Boolean
)

data class ShowQualifying(
    val q1: Boolean,
    val q2: Boolean,
    val q3: Boolean,
    val deltas: Boolean,
    val penalties: Boolean
) {
    val none: Boolean
        get() = !q1 && !q2 && !q3
}