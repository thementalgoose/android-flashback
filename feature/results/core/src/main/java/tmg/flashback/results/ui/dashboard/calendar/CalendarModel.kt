package tmg.flashback.results.ui.dashboard.calendar

import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.OverviewRace

sealed class CalendarModel(
    val key: String
) {
    data class Week(
        val season: Int,
        val startOfWeek: LocalDate,
        val race: OverviewRace?
    ): CalendarModel(
        key = "week-${startOfWeek}"
    ) {
        val endOfWeek: LocalDate
            get() = startOfWeek.plusDays(6L)
    }

    object Loading: CalendarModel(
        key = "loading"
    )
}