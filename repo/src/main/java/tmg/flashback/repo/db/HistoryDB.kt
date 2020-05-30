package tmg.flashback.repo.db

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.History
import tmg.flashback.repo.models.WinnerSeason

interface HistoryDB {
    fun allHistory(): Flow<List<History>>
    fun historyFor(season: Int): Flow<History?>
    fun allWinners(): Flow<List<WinnerSeason>>
    fun winnersFor(season: Int): Flow<WinnerSeason?>
}