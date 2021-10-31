package tmg.flashback.di_old.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.data.db.stats.ConstructorRepository
import tmg.flashback.formula1.model.ConstructorOverview

internal object MockConstructorRepository: ConstructorRepository {
    override fun getConstructorOverview(constructorId: String): Flow<ConstructorOverview?> = flow {
        when (constructorId) {
            mockConstructorBlue.id -> emit(mockConstructorBlueOverview)
            else -> emit(null)
        }
    }
}