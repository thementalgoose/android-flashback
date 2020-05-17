package tmg.flashback.dashboard.year

import androidx.annotation.LayoutRes
import androidx.core.graphics.toColorInt
import tmg.flashback.R
import tmg.flashback.colours

sealed class DashboardYearItem {
    data class Season(
        val year: Int,
        val numberOfRaces: Int? = null
    ): DashboardYearItem() {
        val colour: Int = colours.random().second.toColorInt()
    }

    data class Banner(
        val message: String
    ): DashboardYearItem()

    object Header: DashboardYearItem()

    object Placeholder: DashboardYearItem()
}

enum class DashboardViewType(
    @LayoutRes val layoutId: Int
) {
    SEASON(R.layout.view_dashboard_year),
    BANNER(R.layout.view_dashboard_banner),
    HEADER(R.layout.view_dashboard_header),
    PLACEHOLDER(R.layout.view_dashboard_skeleton)
}