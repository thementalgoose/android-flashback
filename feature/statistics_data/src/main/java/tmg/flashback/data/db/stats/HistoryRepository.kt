package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.data.models.stats.History

interface HistoryRepository {
    fun historyFor(season: Int): Flow<History?>
}