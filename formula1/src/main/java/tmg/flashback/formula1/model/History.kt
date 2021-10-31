package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

// TODO: Rename this crap to overview

data class History(
    val season: Int,
//    val winner: WinnerSeason?,
    val rounds: List<HistoryRound>
) {
    val completed: Int
        get() = rounds.count { it.date < LocalDate.now() }
    val upcoming: Int
        get() = rounds.count { it.date >= LocalDate.now() }
    val scheduledToday: Int
        get() = rounds.count { it.date == LocalDate.now() }
}

data class HistoryRound(
    val date: LocalDate,
    val time: LocalTime?,
    val season: Int,
    val round: Int,
    val raceName: String,
    val circuitId: String,
    val circuitName: String,
    val country: String,
    val countryISO: String,
    val hasQualifying: Boolean,
    val hasResults: Boolean
)