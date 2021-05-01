package tmg.configuration.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import tmg.configuration.BuildConfig
import tmg.configuration.extensions.toJson
import tmg.configuration.firebase.models.*
import tmg.configuration.managers.RemoteConfigManager
import tmg.configuration.repository.models.ForceUpgrade
import tmg.configuration.repository.models.SupportedSource
import tmg.configuration.repository.models.UpNextSchedule

class FirebaseRemoteConfigManager: RemoteConfigManager {

    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    private val remoteConfigSettings = FirebaseRemoteConfigSettings
        .Builder()
        .apply {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) {
                10L // 10s
            } else {
                21600L // 6h
            }
        }
        .build()

    override fun getBoolean(key: String): Boolean {
        return remoteConfig.getBoolean(key)
    }

    override fun getString(key: String): String {
        return remoteConfig.getString(key)
    }

    //region Complex objects supported by remote config

    override fun String.toSupportedSeasons(): Set<Int> {
        return toJson<RemoteConfigAllSeasons>()?.convert() ?: emptySet()
    }

    override fun String.toForceUpgrade(): ForceUpgrade? {
        return toJson<RemoteConfigForceUpgrade>()?.convert()
    }

    override fun String.toUpNext(): List<UpNextSchedule> {
        return toJson<RemoteConfigUpNext>()?.convert() ?: emptyList()
    }

    override fun String.toRssSupportedSources(): List<SupportedSource> {
        return toJson<RemoteConfigSupportedSources>()?.convert() ?: emptyList()
    }

    //endregion
}