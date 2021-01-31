package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.data.models.stats.ConstructorOverview

interface ConstructorRepository {
    fun getConstructorOverview(constructorId: String): Flow<ConstructorOverview?>
}