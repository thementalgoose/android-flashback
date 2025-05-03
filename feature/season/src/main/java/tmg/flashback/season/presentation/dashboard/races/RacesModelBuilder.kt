package tmg.flashback.season.presentation.dashboard.races

import java.time.LocalDate
import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.notifications.NotificationSchedule
import tmg.utilities.extensions.format
import tmg.utilities.extensions.startOfWeek
import tmg.utilities.utils.LocalDateUtils

internal object RacesModelBuilder {

    fun generateScheduleModel(
        overview: Overview,
        events: List<Event>,
        notificationSchedule: NotificationSchedule,
        showCollapsePreviousRaces: Boolean,
        showEmptyWeeks: Boolean,
    ): List<RacesModel> {
        val list = mutableListOf<RacesModel>()
        val next = overview.overviewRaces.getLatestUpcoming()
        val now = LocalDate.now()

        if (events.any() && overview.completed == 0 && events.any { it.date >= now }) {
            list.addAll(events.map {
                RacesModel.Event(it)
            })
        }

        if (next == null || overview.upcoming == 0) {
            list.addAll(overview.overviewRaces.map {
                RacesModel.RaceWeek(
                    model = it,
                    showScheduleList = false,
                    notificationSchedule = notificationSchedule
                )
            })
            return list
        }
        if (showCollapsePreviousRaces && next.round >= 4) {
            val twoRacesAgo = overview.overviewRaces.first { it.round == next.round - 2 }
            list.add(
                RacesModel.GroupedCompletedRaces(
                    first = overview.overviewRaces.minBy { it.round },
                    last = twoRacesAgo
                )
            )
            val oneRaceAgo = overview.overviewRaces.first { it.round == next.round - 1 }
            if (showEmptyWeeks) {
                list.addAll(twoRacesAgo.getWeeksBetween(oneRaceAgo).map { RacesModel.EmptyWeek(it) })
            }
            list.add(
                RacesModel.RaceWeek(
                    model = oneRaceAgo,
                    showScheduleList = false,
                    notificationSchedule = notificationSchedule
                )
            )
            if (showEmptyWeeks) {
                list.addAll(oneRaceAgo.getWeeksBetween(next).map { RacesModel.EmptyWeek(it) })
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
            .forEachIndexed { index, overviewRace ->
                list.add(
                    RacesModel.RaceWeek(
                        model = overviewRace,
                        showScheduleList = overviewRace.round == next.round,
                        notificationSchedule = notificationSchedule
                    )
                )
                val nextRace = overview.overviewRaces.firstOrNull { it.round == overviewRace.round + 1 }
                if (showEmptyWeeks && nextRace != null) {
                    list.addAll(overviewRace.getWeeksBetween(nextRace).map {
                        RacesModel.EmptyWeek(
                            it
                        )
                    })
                }
            }

        return list
            .sortedBy {
                when (it) {
                    is RacesModel.EmptyWeek -> it.monday.format("yyyy-MM-dd")
                    is RacesModel.GroupedCompletedRaces -> it.first.date.format("yyyy-MM-dd")
                    is RacesModel.Event -> it.date.format("yyyy-MM-dd")
                    is RacesModel.RaceWeek -> it.date.format("yyyy-MM-dd")
                    RacesModel.AllEvents -> "0000-00-00"
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