package tmg.flashback.di_old.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.data.db.stats.DriverRepository
import tmg.flashback.formula1.model.DriverOverview

internal object MockDriverRepository: DriverRepository {
    override fun getDriverOverview(driverId: String): Flow<DriverOverview?> = flow {
        when (driverId) {
            mockDriverDaniel.id -> emit(mockDriverDanielOverview)
            else -> emit(null)
        }
    }
}