package tmg.flashback.repo.db

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.AppLockout

interface DataDB {
    suspend fun appLockout(): Flow<AppLockout?>
}