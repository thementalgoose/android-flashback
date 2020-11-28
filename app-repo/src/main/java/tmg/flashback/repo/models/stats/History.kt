package tmg.flashback.repo.models.stats

import org.threeten.bp.LocalDate

data class History(
    val season: Int,
    val winner: WinnerSeason?,
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