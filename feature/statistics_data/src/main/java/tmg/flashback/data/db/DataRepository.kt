package tmg.flashback.data.db

import kotlinx.coroutines.flow.Flow
import tmg.flashback.data.models.AppLockout

interface DataRepository {
    fun appLockout(): Flow<AppLockout?>
}