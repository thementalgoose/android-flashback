package tmg.flashback.stats.ui.drivers.overview

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Constructor
import tmg.utilities.models.StringHolder

sealed class DriverOverviewModel(
    val key: String
) {
    data class Header(
        val driverId: String,
        val driverName: String,
        val driverNumber: Int?,
        val driverImg: String,
        val driverBirthday: LocalDate,
        val driverWikiUrl: String,
        val driverNationalityISO: String,
        val constructors: List<Constructor>
    ): DriverOverviewModel(
        key = driverId
    )

    data class Message(
        val message: StringHolder
    ): DriverOverviewModel(
        key = "message-${message}"
    )

    data class Stat(
        val isWinning: Boolean,
        @DrawableRes
        val icon: Int,
        @StringRes
        val label: Int,
        val value: String
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

    object NetworkError: DriverOverviewModel(
        key = "error"
    )
    object InternalError: DriverOverviewModel(
        key = "error"
    )

    object Loading: DriverOverviewModel(
        key = "loading"
    )

    companion object
}


enum class PipeType {
    SINGLE,
    START,
    START_END,
    SINGLE_PIPE,
    END
}