package tmg.flashback.di.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.data.db.stats.ConstructorRepository
import tmg.flashback.data.models.stats.ConstructorOverview

internal object MockConstructorRepository: ConstructorRepository {
    override fun getConstructorOverview(constructorId: String): Flow<ConstructorOverview?> = flow {
        when (constructorId) {
            mockConstructorBlue.id -> emit(mockConstructorBlueOverview)
            else -> emit(null)
        }
    }
}