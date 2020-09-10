package tmg.flashback.repo.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.stats.DriverOverview

interface DriverDB {
    fun getDriverOverview(driverId: String): Flow<DriverOverview?>
}