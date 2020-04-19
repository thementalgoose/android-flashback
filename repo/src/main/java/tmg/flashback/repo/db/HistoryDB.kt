package tmg.flashback.repo.db

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.History

interface HistoryDB {
    fun allHistory(): Flow<List<History>>
    fun historyFor(season: Int): Flow<History?>
}