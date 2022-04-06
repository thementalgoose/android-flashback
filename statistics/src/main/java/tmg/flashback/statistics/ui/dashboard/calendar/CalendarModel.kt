package tmg.flashback.statistics.ui.dashboard.calendar

import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import tmg.flashback.formula1.model.OverviewRace

sealed class CalendarModel {
    data class List(
        val model: OverviewRace
    ): CalendarModel()

    data class Month(
        val month: org.threeten.bp.Month
    ): CalendarModel()

    data class Week(
        val weekBeginning: LocalDate,
        val race: OverviewRace?
    ): CalendarModel()
}