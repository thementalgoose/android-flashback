package tmg.flashback.repo.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.AppBanner
import tmg.flashback.repo.models.AppLockout

interface DataDB {
    fun appLockout(): Flow<AppLockout?>
    fun appBanner(): Flow<AppBanner?>
}