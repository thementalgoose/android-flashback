package tmg.flashback.stats.ui.drivers.overview

import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Constructor

sealed class DriverOverviewModel(
    @LayoutRes val layoutId: Int
) {
    data class Header(
        val driverFirstname: String,
        val driverSurname: String,
        val driverNumber: Int?,
        val driverImg: String,
        val driverBirthday: LocalDate,
        val driverWikiUrl: String,
        val driverNationalityISO: String,
        val constructors: List<Constructor>
    ): DriverOverviewModel(
        R.layout.view_driver_summary_header
    )

    data class Stat(
        @AttrRes
        val tint: Int = R.attr.contentSecondary,
        @DrawableRes
        val icon: Int,
        @StringRes
        val label: Int,
        val value: String
    ): DriverOverviewModel(
        R.layout.view_overview_stat
    )

    data class RacedFor(
        val season: Int,
        val constructors: List<Constructor>,
        val type: PipeType,
        val isChampionship: Boolean
    ): DriverOverviewModel(
        R.layout.view_driver_summary_history
    )

    data class ErrorItem(
        val item: SyncDataItem
    ): DriverOverviewModel(item.layoutId)

    object Loading: DriverOverviewModel(R.layout.view_loading_podium)

    companion object
}


enum class PipeType {
    SINGLE,
    START,
    START_END,
    SINGLE_PIPE,
    END
}