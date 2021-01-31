package tmg.flashback.ui.overviews.driver.summary

import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import org.threeten.bp.LocalDate
import tmg.flashback.R
import tmg.flashback.data.models.stats.SlimConstructor
import tmg.flashback.ui.shared.sync.SyncDataItem

sealed class DriverSummaryItem(
        @LayoutRes val layoutId: Int
) {
    data class Header(
            val driverFirstname: String,
            val driverSurname: String,
            val driverNumber: Int,
            val driverImg: String,
            val driverBirthday: LocalDate,
            val driverWikiUrl: String,
            val driverNationalityISO: String
    ): DriverSummaryItem(
            R.layout.view_driver_summary_header
    )

    data class Stat(
            @AttrRes
            val tint: Int = R.attr.f1TextSecondary,
            @DrawableRes
            val icon: Int,
            @StringRes
            val label: Int,
            val value: String
    ): DriverSummaryItem(
            R.layout.view_overview_stat
    )

    data class RacedFor(
            val season: Int,
            val constructors: List<SlimConstructor>,
            val type: PipeType,
            val isChampionship: Boolean
    ): DriverSummaryItem(
            R.layout.view_driver_summary_history
    )

    data class ErrorItem(
            val item: SyncDataItem
    ): DriverSummaryItem(item.layoutId)

    object Loading: DriverSummaryItem(R.layout.view_loading_podium)
}


fun MutableList<DriverSummaryItem>.addError(syncDataItem: SyncDataItem) {
    this.add(
            DriverSummaryItem.ErrorItem(
                    syncDataItem
            )
    )
}

enum class PipeType {
    SINGLE,
    START,
    START_END,
    SINGLE_PIPE,
    END
}