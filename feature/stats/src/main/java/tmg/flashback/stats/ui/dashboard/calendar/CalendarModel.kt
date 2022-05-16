package tmg.flashback.stats.ui.dashboard.calendar

import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import tmg.flashback.formula1.model.OverviewRace

sealed class CalendarModel {
    data class List(
        val model: OverviewRace,
        private val showScheduleList: Boolean = false,
        val id: String = model.raceName
    ): CalendarModel() {

        val shouldShowScheduleList = showScheduleList && model.schedule.isNotEmpty()

        val fadeItem: Boolean
            get() = model.date.isAfter(LocalDate.now()) && !shouldShowScheduleList
    }

    data class Month(
        val month: org.threeten.bp.Month
    ): CalendarModel()

    data class Week(
        val weekBeginning: LocalDate,
        val month: org.threeten.bp.Month,
        val race: OverviewRace?
    ): CalendarModel()

    object Loading: CalendarModel()
}