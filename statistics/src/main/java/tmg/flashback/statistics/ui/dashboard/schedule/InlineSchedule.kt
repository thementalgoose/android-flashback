package tmg.flashback.statistics.ui.dashboard.schedule

import androidx.annotation.LayoutRes
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.statistics.R

sealed class InlineSchedule(
    @LayoutRes
    val layoutId: Int
) {
    data class Day(
        val localDate: LocalDate
    ): InlineSchedule(
        layoutId = R.layout.view_inline_schedule_day
    )

    data class Item(
        val label: String,
        val time: LocalTime,
        val showBell: Boolean
    ): InlineSchedule(
        layoutId = R.layout.view_inline_schedule_item
    )
}