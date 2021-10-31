package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.formula1.model.History

interface HistoryRepository {
    fun historyFor(season: Int): Flow<tmg.flashback.formula1.model.History?>
}