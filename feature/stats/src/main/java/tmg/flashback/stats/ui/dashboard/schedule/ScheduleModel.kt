package tmg.flashback.stats.ui.dashboard.schedule

import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.stats.repository.models.NotificationSchedule

sealed class ScheduleModel(
    val key: String
) {
    data class List(
        val model: OverviewRace,
        private val showScheduleList: Boolean = false,
        val notificationSchedule: NotificationSchedule,
        val id: String = model.raceName
    ): ScheduleModel(
        key = "${model.season} ${model.raceName}"
    ) {
        val date: LocalDate
            get() = model.date

        val shouldShowScheduleList = showScheduleList && model.schedule.isNotEmpty()

        val fadeItem: Boolean
            get() = model.date.isAfter(LocalDate.now()) && !shouldShowScheduleList
    }

    data class CollapsableList(
        val first: OverviewRace,
        val last: OverviewRace?
    ): ScheduleModel(
        key = "collapsable-${first.round}-${last?.round}"
    )

    data class Event(
        val event: tmg.flashback.formula1.model.Event
    ): ScheduleModel(
        key = "event-${event.label}"
    ) {
        val date: LocalDate
            get() = event.date
    }

    object Loading: ScheduleModel(
        key = "loading"
    )
}