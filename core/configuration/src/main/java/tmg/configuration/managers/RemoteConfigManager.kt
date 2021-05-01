package tmg.configuration.managers

import tmg.configuration.repository.models.ForceUpgrade
import tmg.configuration.repository.models.SupportedSource
import tmg.configuration.repository.models.UpNextSchedule

interface RemoteConfigManager {
    fun getBoolean(key: String): Boolean
    fun getString(key: String): String
    fun <T> getObject(key: String, clazz: Class<T>): T?

    // Complex objects in RemoteConfig
    fun String.toSupportedSeasons(): Set<Int>
    fun String.toForceUpgrade(): ForceUpgrade?
    fun String.toUpNext(): List<UpNextSchedule>
    fun String.toRssSupportedSources(): List<SupportedSource>
}

inline fun <reified T> RemoteConfigManager.getObject(key: String): T? {
    return getObject(key, T::class.java)
}