package tmg.flashback.managers.remoteconfig

interface RemoteConfigManager {

    suspend fun update(andActivate: Boolean = false): Boolean
    suspend fun activate(): Boolean

    /**
     * Determine if the app version has changed in a way which
     *   requires a remote sync before loading into the app
     */
    val requiresRemoteSync: Boolean
}