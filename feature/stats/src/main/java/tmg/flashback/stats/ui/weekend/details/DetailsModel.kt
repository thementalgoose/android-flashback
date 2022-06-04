package tmg.flashback.stats.ui.weekend.details

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Schedule
import tmg.utilities.models.StringHolder

sealed class DetailsModel(
    val id: String
) {
    data class ScheduleDay(
        val date: LocalDate,
        val schedules: List<Pair<Schedule, Boolean>> // Schedule, isNotificationSet
    ): DetailsModel(
        id = "${date.dayOfMonth}-${date.month}-${date.year}"
    ) {
        companion object
    }

    data class Link(
        @StringRes
        val label: Int,
        @DrawableRes
        val icon: Int,
        val url: String?,
    ): DetailsModel(
        id = "link-$label"
    ) {
        companion object
    }

    data class Label(
        val label: StringHolder,
        @DrawableRes
        val icon: Int,
    ): DetailsModel(
        id = "label-$label"
    ) {
        companion object
    }
}