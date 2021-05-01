package tmg.configuration.managers

import tmg.configuration.repository.models.ForceUpgrade
import tmg.configuration.repository.models.SupportedSource
import tmg.configuration.repository.models.UpNextSchedule

interface RemoteConfigManager {

    fun initialiseRemoteConfig()

    fun getBoolean(key: String): Boolean
    fun getString(key: String): String

    suspend fun fetch(andActivate: Boolean): Boolean
    suspend fun activate(): Boolean
}