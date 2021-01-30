package tmg.flashback.data.db

import kotlinx.coroutines.flow.Flow
import tmg.flashback.data.models.remoteconfig.AppLockout

interface DataRepository {
    fun appLockout(): Flow<AppLockout?>
}