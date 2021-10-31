package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.formula1.model.ConstructorOverview

interface ConstructorRepository {
    fun getConstructorOverview(constructorId: String): Flow<tmg.flashback.formula1.model.ConstructorOverview?>
}