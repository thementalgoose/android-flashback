package tmg.flashback.drivers.presentation.season

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.ui.components.navigation.PipeType


sealed class DriverSeasonModel(
    val key: String
) {
    data class Header(
        val driver: Driver
    ): DriverSeasonModel(
        key = "driver-header-${driver.id}"
    )

    data class Stat(
        val isWinning: Boolean,
        @DrawableRes
        val icon: Int,
        @StringRes
        val label: Int,
        val value: String
    ): DriverSeasonModel(
        key = "stat-$label"
    )
    data class Message(
        @StringRes
        val label: Int,
        val args: List<Any>
    ): DriverSeasonModel(
        key = "message-${label}"
    )

    data class RacedFor(
        val season: Int?, // Null = hide season
        val constructors: Constructor,
        val type: PipeType,
        val isChampionship: Boolean
    ): DriverSeasonModel(
        key = "raced-${constructors.id}"
    )

    data class Result(
        val season: Int,
        val round: Int,
        val raceName: String,
        val circuitName: String,
        val circuitId: String,
        val raceCountry: String,
        val raceCountryISO: String,
        val date: LocalDate,
        val showConstructorLabel: Boolean,
        val constructor: Constructor,
        val qualified: Int?,
        val finished: Int?,
        val raceStatus: RaceStatus,
        val points: Double,
        val maxPoints: Int,
        val isSprint: Boolean
    ): DriverSeasonModel(
        key = "result-s${season}r${round}-$isSprint"
    )

    object ResultHeader: DriverSeasonModel(
        key = "result-header"
    )

    object NetworkError: DriverSeasonModel(
        key = "network-error"
    )
    object InternalError: DriverSeasonModel(
        key = "internal-error"
    )

    object Loading: DriverSeasonModel(
        key = "loading"
    )

    companion object
}