package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.data.models.stats.DriverOverview

interface DriverRepository {
    fun getDriverOverview(driverId: String): Flow<DriverOverview?>
}