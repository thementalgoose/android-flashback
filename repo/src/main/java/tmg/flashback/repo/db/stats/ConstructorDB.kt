package tmg.flashback.repo.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.stats.ConstructorOverview

interface ConstructorDB {
    fun getConstructorOverview(constructorId: String): Flow<ConstructorOverview?>
}