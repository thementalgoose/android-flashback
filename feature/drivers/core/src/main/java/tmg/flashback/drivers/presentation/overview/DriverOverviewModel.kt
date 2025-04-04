package tmg.flashback.drivers.presentation.overview

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.time.LocalDate
import tmg.flashback.drivers.contract.model.DriverStatHistoryType
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.ui.components.navigation.PipeType

sealed class DriverOverviewModel(
    val key: String
) {

    data class Message(
        @StringRes
        val label: Int,
        val args: List<Any>
    ): DriverOverviewModel(
        key = "message-${label}"
    )

    data class Stat(
        val isWinning: Boolean,
        @DrawableRes
        val icon: Int,
        @StringRes
        val label: Int,
        val value: String,
        val driverStatHistoryType: DriverStatHistoryType? = null
    ): DriverOverviewModel(
        key = label.toString()
    )

    data class RacedFor(
        val season: Int,
        val constructors: List<Constructor>,
        val type: PipeType,
        val isChampionship: Boolean
    ): DriverOverviewModel(
        key = "raced-${season}"
    )

    companion object
}