package tmg.flashback.statistics.ui.race

import androidx.annotation.LayoutRes
import org.threeten.bp.LocalDate
import tmg.core.ui.model.AnimationSpeed
import tmg.flashback.data.enums.RaceStatus
import tmg.flashback.data.models.stats.*
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem

sealed class RaceModel(
        @LayoutRes val layoutId: Int
) {
    data class Overview(
        val raceName: String,
        val country: String,
        val countryISO: String,
        val circuitId: String,
        val circuitName: String,
        val round: Int,
        val season: Int,
        val raceDate: LocalDate?,
        val wikipedia: String?
    ) : RaceModel(R.layout.view_race_overview)

    data class Podium(
        val driverFirst: Single,
        val driverSecond: Single,
        val driverThird: Single
    ) : RaceModel(R.layout.view_race_race_podium)

    data class Single(
        val season: Int,
        val round: Int,
        val driver: ConstructorDriver,
        val q1: RoundQualifyingResult?,
        val q2: RoundQualifyingResult?,
        val q3: RoundQualifyingResult?,
        val qSprint: RoundSprintQualifyingResult?,
        val race: SingleRace?,
        val qualified: Int?,
        val q1Delta: String?,
        val q2Delta: String?,
        val q3Delta: String?,
        val displayPrefs: DisplayPrefs
    ) : RaceModel(0) {

        @LayoutRes
        fun layoutIdByViewType(type: RaceAdapterType): Int {
            return when (type) {
                RaceAdapterType.RACE -> R.layout.view_race_race_result
                RaceAdapterType.QUALIFYING_SPRINT -> R.layout.view_race_sprint_qualifying_result
                RaceAdapterType.QUALIFYING_POS_1 -> R.layout.view_race_qualifying_result
                RaceAdapterType.QUALIFYING_POS_2 -> R.layout.view_race_qualifying_result
                RaceAdapterType.QUALIFYING_POS -> R.layout.view_race_qualifying_result
                else -> 0
            }
        }
    }

    data class RaceHeader(
        val season: Int,
        val round: Int
    ) : RaceModel(R.layout.view_race_race_header)

    data class QualifyingHeader(
        val displayPrefs: DisplayPrefs
    ) : RaceModel(R.layout.view_race_qualifying_header)

    data class ConstructorStandings(
        val constructor: Constructor,
        val points: Double,
        val driver: List<Pair<ConstructorDriver, Double>>,
        val animationSpeed: AnimationSpeed
    ) : RaceModel(R.layout.view_race_constructor)

    data class ErrorItem(
            val item: SyncDataItem
    ) : RaceModel(item.layoutId)

    object Loading : RaceModel(R.layout.skeleton_race)
}

data class SingleRace(
    val points: Double,
    val result: LapTime,
    val pos: Int,
    val gridPos: Int,
    val status: RaceStatus,
    val fastestLap: Boolean
)

data class DisplayPrefs(
    val q1: Boolean,
    val q2: Boolean,
    val q3: Boolean,
    val deltas: Boolean,
    val penalties: Boolean,
    val fadeDNF: Boolean
) {
    val none: Boolean
        get() = !q1 && !q2 && !q3
}

