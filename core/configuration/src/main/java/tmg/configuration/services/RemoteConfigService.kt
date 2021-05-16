package tmg.configuration.services

interface RemoteConfigService {

    fun initialiseRemoteConfig()

    fun getBoolean(key: String): Boolean
    fun getString(key: String): String

    suspend fun fetch(andActivate: Boolean): Boolean
    suspend fun activate(): Boolean
}