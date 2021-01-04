package tmg.flashback.managers.remoteconfig

interface RemoteConfigManager {
    suspend fun update(andActivate: Boolean = false): Boolean
    suspend fun activate(): Boolean

    var remoteConfigInitialSync: Boolean
}