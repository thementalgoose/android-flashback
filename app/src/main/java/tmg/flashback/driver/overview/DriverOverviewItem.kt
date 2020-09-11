package tmg.flashback.driver.overview

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import org.threeten.bp.LocalDate
import tmg.flashback.R
import tmg.flashback.shared.SyncDataItem

sealed class DriverOverviewItem(
        @LayoutRes val layoutId: Int
) {
    data class Header(
            val driverFirstname: String,
            val driverSurname: String,
            val driverNumber: Int,
            val driverImg: String,
            val driverBirthday: LocalDate,
            val driverWikiUrl: String
    ): DriverOverviewItem(
            R.layout.view_driver_overview_header
    )

    data class Stat(
            @DrawableRes
            val icon: Int,
            @StringRes
            val label: Int,
            val value: String
    ): DriverOverviewItem(
            R.layout.view_driver_overview_stat
    )

    data class ErrorItem(
            val item: SyncDataItem
    ): DriverOverviewItem(item.layoutId)
}