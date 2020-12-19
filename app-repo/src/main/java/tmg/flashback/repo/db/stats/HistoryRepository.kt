package tmg.flashback.repo.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.stats.History
import tmg.flashback.repo.models.stats.WinnerSeason

interface HistoryRepository {
    @Deprecated("No longer used")
    fun allHistory(): Flow<List<History>>
    fun historyFor(season: Int): Flow<History?>
    @Deprecated("No longer used")
    fun allWinners(): Flow<List<WinnerSeason>>
    @Deprecated("No longer used")
    fun winnersFor(season: Int): Flow<WinnerSeason?>
}