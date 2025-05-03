package tmg.flashback.season.presentation.dashboard.races

import java.time.LocalDate
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.notifications.NotificationSchedule

sealed class RacesModel(
    val key: String
) {

    data class EmptyWeek(
        val monday: LocalDate,
    ): RacesModel(
        key = "empty-$monday"
    )

    data class RaceWeek(
        val model: OverviewRace,
        private val showScheduleList: Boolean = false,
        val notificationSchedule: NotificationSchedule,
        val id: String = model.raceName
    ): RacesModel(
        key = "${model.season} ${model.round} ${model.raceName}"
    ) {
        val date: LocalDate
            get() = model.date

        val shouldShowScheduleList = showScheduleList && model.schedule.isNotEmpty()

        val containsSprintEvent: Boolean by lazy {
            model.schedule.any { it.label.contains("sprint", ignoreCase = true) }
        }

        val fadeItem: Boolean
            get() = model.date.isAfter(LocalDate.now()) && !shouldShowScheduleList
    }

    data class GroupedCompletedRaces(
        val first: OverviewRace,
        val last: OverviewRace?
    ): RacesModel(
        key = "collapsable-${first.round}-${last?.round}"
    )

    data class Event(
        val event: tmg.flashback.formula1.model.Event
    ): RacesModel(
        key = "event-${event.label}-${event.date}"
    ) {
        val date: LocalDate
            get() = event.date
    }

    data object AllEvents: RacesModel("all-events")
}