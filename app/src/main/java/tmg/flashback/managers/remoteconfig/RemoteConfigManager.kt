package tmg.flashback.managers.remoteconfig

import java.util.function.BooleanSupplier

interface RemoteConfigManager {
    suspend fun update(andActivate: Boolean = false): Boolean
    suspend fun activate(): Boolean

    var remoteConfigInitialSync: Boolean
}