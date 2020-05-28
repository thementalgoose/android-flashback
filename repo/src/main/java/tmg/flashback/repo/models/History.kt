package tmg.flashback.repo.models

import org.threeten.bp.LocalDate

data class History(
    val season: Int,
    val rounds: List<HistoryRound>,
    val driversChampion: List<HistoryWinDriver>,
    val constructorsChampion: List<HistoryWinConstructor>
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
    val hasResults: Boolean
)

data class HistoryWinDriver(
    val id: String,
    val name: String,
    val photoUrl: String,
    val points: Int
)

data class HistoryWinConstructor(
    val id: String,
    val name: String,
    val color: String,
    val points: Int
)