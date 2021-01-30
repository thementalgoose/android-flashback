package tmg.flashback.di.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.data.db.stats.DriverRepository
import tmg.flashback.data.models.stats.DriverOverview

internal object MockDriverRepository: DriverRepository {
    override fun getDriverOverview(driverId: String): Flow<DriverOverview?> = flow {
        when (driverId) {
            mockDriverDaniel.id -> emit(mockDriverDanielOverview)
            else -> emit(null)
        }
    }
}