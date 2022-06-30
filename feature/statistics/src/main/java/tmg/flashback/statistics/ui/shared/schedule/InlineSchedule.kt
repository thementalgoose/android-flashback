package tmg.flashback.statistics.ui.shared.schedule

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
        val date: LocalDate,
        val time: LocalTime,
        val showBell: Boolean
    ): InlineSchedule(
        layoutId = R.layout.view_inline_schedule_item
    )

    object Timezone: InlineSchedule(
        layoutId = R.layout.view_inline_schedule_device_time
    )
}