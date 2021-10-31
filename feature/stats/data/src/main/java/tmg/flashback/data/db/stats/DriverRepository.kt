package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.formula1.model.DriverOverview

interface DriverRepository {
    fun getDriverOverview(driverId: String): Flow<tmg.flashback.formula1.model.DriverOverview?>
}