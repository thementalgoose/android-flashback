package tmg.flashback.repo.db

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.AppLockout

interface DataDB {
    fun appLockout(): Flow<AppLockout>
}