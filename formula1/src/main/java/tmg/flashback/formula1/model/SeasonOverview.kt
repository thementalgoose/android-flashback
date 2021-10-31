package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

data class SeasonOverview(
    val season: Int,
//    val winner: WinnerSeason?,
    val roundOverviews: List<RoundOverview>
) {
    val completed: Int
        get() = roundOverviews.count { it.date < LocalDate.now() }
    val upcoming: Int
        get() = roundOverviews.count { it.date >= LocalDate.now() }
    val scheduledToday: Int
        get() = roundOverviews.count { it.date == LocalDate.now() }

    companion object
}
