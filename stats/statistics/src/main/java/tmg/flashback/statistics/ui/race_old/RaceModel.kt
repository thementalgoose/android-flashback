package tmg.flashback.statistics.ui.race_old

import androidx.annotation.LayoutRes
import org.threeten.bp.LocalDate
import tmg.flashback.ui.model.AnimationSpeed
import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.model.*
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.race.DisplayPrefs
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
        val driver: DriverConstructor,
        val q1: RaceQualifyingResult_Legacy?,
        val q2: RaceQualifyingResult_Legacy?,
        val q3: RaceQualifyingResult_Legacy?,
        val qSprint: RaceSprintQualifyingResult_Legacy?,
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
        val driver: List<Pair<Driver, Double>>,
        val animationSpeed: AnimationSpeed
    ) : RaceModel(R.layout.view_race_constructor)

    data class ErrorItem(
            val item: SyncDataItem
    ) : RaceModel(item.layoutId)

    object Loading : RaceModel(R.layout.skeleton_race)

    companion object
}


data class SingleRace(
    val points: Double,
    val result: LapTime,
    val pos: Int,
    val gridPos: Int,
    val status: RaceStatus,
    val fastestLap: Boolean
)

