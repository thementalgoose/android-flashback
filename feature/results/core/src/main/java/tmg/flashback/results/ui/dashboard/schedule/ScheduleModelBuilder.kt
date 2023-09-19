package tmg.flashback.results.ui.dashboard.schedule

import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.results.contract.repository.models.NotificationSchedule
import tmg.utilities.extensions.format
import tmg.utilities.extensions.startOfWeek
import tmg.utilities.utils.LocalDateUtils

internal object ScheduleModelBuilder {

    fun generateScheduleModel(
        overview: Overview,
        events: List<Event>,
        notificationSchedule: NotificationSchedule,
        showCollapsePreviousRaces: Boolean,
        showEmptyWeeks: Boolean,
    ): List<ScheduleModel> {
        val list = mutableListOf<ScheduleModel>()
        val next = overview.overviewRaces.getLatestUpcoming()

        if (overview.completed == 0) {
            list.addAll(events.map {
                ScheduleModel.Event(it)
            })
        }

        if (next == null || overview.upcoming == 0) {
            list.addAll(overview.overviewRaces.map {
                ScheduleModel.RaceWeek(
                    model = it,
                    showScheduleList = false,
                    notificationSchedule = notificationSchedule
                )
            })
            return list
        }

        if (showCollapsePreviousRaces && next.round >= 4) {
            val twoRacesAgo = overview.overviewRaces.first { it.round == next.round - 2 }
            list.add(ScheduleModel.GroupedCompletedRaces(
                first = overview.overviewRaces.minBy { it.round },
                last = twoRacesAgo
            ))
            val oneRaceAgo = overview.overviewRaces.first { it.round == next.round - 1 }
            if (showEmptyWeeks) {
                list.addAll(twoRacesAgo.getWeeksBetween(oneRaceAgo).map { ScheduleModel.EmptyWeek(it) })
            }
            list.add(ScheduleModel.RaceWeek(
                model = oneRaceAgo,
                showScheduleList = false,
                notificationSchedule = notificationSchedule
            ))
            if (showEmptyWeeks) {
                list.addAll(oneRaceAgo.getWeeksBetween(next).map { ScheduleModel.EmptyWeek(it) })
            }
        }

        overview.overviewRaces
            .filter {
                if (!(showCollapsePreviousRaces && next.round >= 4)) {
                    return@filter true
                }
                return@filter it.round >= next.round
            }
            .sortedBy { it.round }
            .forEach { overviewRace ->
                list.add(ScheduleModel.RaceWeek(
                    model = overviewRace,
                    showScheduleList = overviewRace.round == next.round,
                    notificationSchedule = notificationSchedule
                ))
                val nextRace = overview.overviewRaces.firstOrNull { it.round == overviewRace.round + 1 }
                if (showEmptyWeeks && nextRace != null) {
                    list.addAll(overviewRace.getWeeksBetween(nextRace).map { ScheduleModel.EmptyWeek(it) })
                }
            }

        return list
            .sortedBy {
                when (it) {
                    is ScheduleModel.EmptyWeek -> it.monday.format("yyyy-MM-dd")
                    is ScheduleModel.GroupedCompletedRaces -> it.first.date.format("yyyy-MM-dd")
                    is ScheduleModel.Event -> it.date.format("yyyy-MM-dd")
                    is ScheduleModel.RaceWeek -> it.date.format("yyyy-MM-dd")
                    ScheduleModel.Loading -> "0000-00-00"
                }
            }
    }

    private fun OverviewRace.getWeeksBetween(targetRace: OverviewRace): List<LocalDate> {
        val daysBetween = LocalDateUtils.daysBetween(this.date, targetRace.date)
        val weeksToExclude = listOf(this.date.startOfWeek(), targetRace.date.startOfWeek())

        return List(daysBetween) { this.date.plusDays(it.toLong()) }
            .map { it.startOfWeek() }
            .distinct()
            .filter { !weeksToExclude.contains(it) }
    }

    private fun List<OverviewRace>.getLatestUpcoming(): OverviewRace? {
        return this
            .sortedBy { it.date }
            .firstOrNull { it.date >= LocalDate.now() }
    }
}