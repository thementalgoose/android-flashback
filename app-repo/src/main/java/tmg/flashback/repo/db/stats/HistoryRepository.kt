package tmg.flashback.repo.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.stats.History
import tmg.flashback.repo.models.stats.WinnerSeason

interface HistoryRepository {
    fun allHistory(): Flow<List<History>>
    fun historyFor(season: Int): Flow<History?>
    fun allWinners(): Flow<List<WinnerSeason>>
    fun winnersFor(season: Int): Flow<WinnerSeason?>
}