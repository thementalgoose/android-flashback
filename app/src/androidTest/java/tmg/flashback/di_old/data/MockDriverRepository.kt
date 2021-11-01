package tmg.flashback.di_old.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.formula1.model.DriverHistory

internal object MockDriverRepository: DriverRepository {
    override fun getDriverOverview(driverId: String): Flow<DriverHistory?> = flow {
        when (driverId) {
            MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL.id -> emit(MOCK_DRIVER_DANIEL_HISTORY)
            else -> emit(null)
        }
    }
}