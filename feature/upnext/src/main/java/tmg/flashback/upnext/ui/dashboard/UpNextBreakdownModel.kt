package tmg.flashback.upnext.ui.dashboard

import androidx.annotation.LayoutRes
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.upnext.R

sealed class UpNextBreakdownModel(
    @LayoutRes val layoutId: Int
) {
    data class Item(
        val label: String,
        val item: Timestamp
    ): UpNextBreakdownModel(R.layout.view_breakdown_item)

    data class Day(
        val label: String
    ): UpNextBreakdownModel(R.layout.view_breakdown_day)

    object Divider: UpNextBreakdownModel(R.layout.view_breakdown_divider)
}