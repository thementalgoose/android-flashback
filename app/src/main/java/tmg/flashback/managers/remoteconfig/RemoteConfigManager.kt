package tmg.flashback.managers.remoteconfig

interface RemoteConfigManager {

    suspend fun update(andActivate: Boolean = false): Boolean
    suspend fun activate(): Boolean

    val requiresRemoteSync: Boolean
    fun setRemoteSyncPerformed()
}