package tmg.flashback.stats.ui.dashboard.calendar

import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.stats.repository.models.NotificationSchedule

sealed class CalendarModel(
    val key: String
) {
    data class List(
        val model: OverviewRace,
        private val showScheduleList: Boolean = false,
        val notificationSchedule: NotificationSchedule,
        val id: String = model.raceName
    ): CalendarModel(
        key = "${model.season} ${model.raceName}"
    ) {
        val date: LocalDate
            get() = model.date

        val shouldShowScheduleList = showScheduleList && model.schedule.isNotEmpty()

        val fadeItem: Boolean
            get() = model.date.isAfter(LocalDate.now()) && !shouldShowScheduleList
    }

    data class Event(
        val event: tmg.flashback.formula1.model.Event
    ): CalendarModel(
        key = "event-${event.label}"
    ) {
        val date: LocalDate
            get() = event.date
    }

    data class Month(
        val month: org.threeten.bp.Month
    ): CalendarModel(
        key = "month-${month.value}"
    )

    data class Week(
        val weekBeginning: LocalDate,
        val month: org.threeten.bp.Month,
        val race: OverviewRace?
    ): CalendarModel(
        key = "week-${weekBeginning}"
    )

    object Loading: CalendarModel(
        key = "loading"
    )
}