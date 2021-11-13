package tmg.flashback.statistics.ui.race

import androidx.annotation.LayoutRes
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.ui.model.AnimationSpeed

sealed class RaceItem(
    val id: String,
    @LayoutRes
    val layoutId: Int
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
    ): RaceItem(
        id = "OVERVIEW",
        layoutId = R.layout.view_race_overview
    )

    data class Podium(
        val driverFirst: RaceRaceResult,
        val driverSecond: RaceRaceResult,
        val driverThird: RaceRaceResult
    ): RaceItem(
        id = "PODIUM",
        layoutId = R.layout.view_race_race_podium
    )

    data class RaceResult(
        val race: RaceRaceResult
    ): RaceItem(
        id = race.driver.driver.id,
        layoutId = R.layout.view_race_race_result
    )

    data class SprintQualifyingResult(
        val qSprint: RaceQualifyingRoundDriver.SprintQualifying
    ): RaceItem(
        id = qSprint.driver.driver.id,
        layoutId = R.layout.view_race_sprint_qualifying_result
    )

    data class QualifyingResultQ1Q2Q3(
        val driver: DriverConstructor,
        private val finalQualifyingPosition: Int?,
        val q1: RaceQualifyingRoundDriver.Qualifying?,
        val q2: RaceQualifyingRoundDriver.Qualifying?,
        val q3: RaceQualifyingRoundDriver.Qualifying?,
        val q1Delta: String?,
        val q2Delta: String?,
        val q3Delta: String?,
        val qualified: Int? = q3?.position ?: q2?.position ?: q1?.position ?: finalQualifyingPosition
    ): RaceItem(
        id = driver.driver.id,
        layoutId = R.layout.view_race_qualifying_q1q2q3_result
    )

    data class QualifyingResultQ1Q2(
        val driver: DriverConstructor,
        private val finalQualifyingPosition: Int?,
        val q1: RaceQualifyingRoundDriver.Qualifying?,
        val q2: RaceQualifyingRoundDriver.Qualifying?,
        val q1Delta: String?,
        val q2Delta: String?,
        val qualified: Int? = finalQualifyingPosition
    ): RaceItem(
        id = driver.driver.id,
        layoutId = R.layout.view_race_qualifying_q1q2_result
    )

    data class QualifyingResultQ1(
        val driver: DriverConstructor,
        private val finalQualifyingPosition: Int?,
        val q1: RaceQualifyingRoundDriver.Qualifying?,
        val q1Delta: String?,
        val qualified: Int? = q1?.position ?: finalQualifyingPosition
    ): RaceItem(
        id = driver.driver.id,
        layoutId = R.layout.view_race_qualifying_q1_result
    )

    object RaceHeader: RaceItem(
        id = "RACE_HEADER",
        layoutId = R.layout.view_race_race_header
    )

    data class QualifyingHeader(
        val showQ1: Boolean,
        val showQ2: Boolean,
        val showQ3: Boolean
    ): RaceItem(
        id = "QUALIFYING_HEADER",
        layoutId = R.layout.view_race_qualifying_header
    )

    data class ErrorItem(
        val item: SyncDataItem
    ) : RaceItem(
        id = item.layoutId.toString(),
        layoutId = item.layoutId
    )

    data class Constructor(
        val constructor: tmg.flashback.formula1.model.Constructor,
        val points: Double,
        val driver: List<Pair<Driver, Double>>,
        val animationSpeed: AnimationSpeed,
        val maxTeamPoints: Double
    ): RaceItem(
        id = constructor.id,
        layoutId = R.layout.view_race_constructor
    )

    object PodiumLoading : RaceItem(
        id = "PODIUM_LOADING",
        layoutId = R.layout.skeleton_race
    )
}