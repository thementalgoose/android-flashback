package tmg.flashback.repo.db

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.History

interface HistoryDB {
    suspend fun allHistory(): Flow<List<History>>
    suspend fun historyFor(season: Int): Flow<History?>
}