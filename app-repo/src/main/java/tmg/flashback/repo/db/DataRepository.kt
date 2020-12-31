package tmg.flashback.repo.db

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.remoteconfig.AppLockout

interface DataRepository {
    fun appLockout(): Flow<AppLockout?>
}