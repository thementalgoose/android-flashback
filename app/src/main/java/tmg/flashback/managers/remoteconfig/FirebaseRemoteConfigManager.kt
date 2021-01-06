package tmg.flashback.managers.remoteconfig

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await
import tmg.flashback.constants.App.currentYear
import tmg.flashback.constants.Migrations
import tmg.flashback.firebase.BuildConfig
import tmg.flashback.firebase.R
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.crash.FirebaseCrashManager
import tmg.flashback.firebase.extensions.toJson
import tmg.flashback.firebase.models.FAllSeasons
import tmg.flashback.firebase.models.FUpNext
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.models.remoteconfig.UpNextSchedule
import tmg.flashback.repo.pref.DeviceRepository
import java.lang.Exception

class FirebaseRemoteConfigManager(
        private val crashManager: FirebaseCrashManager?,
        private val deviceRepository: DeviceRepository
): RemoteConfigRepository(), RemoteConfigManager {

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

    private val keyDefaultYear: String = "default_year"
    private val keyUpNext: String = "up_next"
    private val keyDefaultBanner: String = "banner"
    private val keyRss: String = "rss"
    private val keyDataProvidedBy: String = "data_provided"
    private val keySearch: String = "search"
    private val keySupportedSeasons: String = "supported_seasons"

    init {
        // This file is autogenerated!
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings)
    }

    //region Remote sync initial

    override val requiresRemoteSync: Boolean
        get() = Migrations.remoteConfigSyncCount != deviceRepository.remoteConfigSync

    fun setRemoteSyncPerformed() {
        deviceRepository.remoteConfigSync = Migrations.remoteConfigSyncCount
    }

    //endregion

    //region Variables inside remote config

    override val defaultSeasonRC: Int
        get() = remoteConfig.getString(keyDefaultYear).toIntOrNull() ?: currentYear

    override val upNextRC: List<UpNextSchedule>
        get() = remoteConfig.getString(keyUpNext).toJson<FUpNext>()?.convert() ?: emptyList()

    override val bannerRC: String
        get() = remoteConfig.getString(keyDefaultBanner)

    override val rssRC: Boolean
        get() = remoteConfig.getBoolean(keyRss)

    override val dataProvidedByRC: String?
        get() {
            val text = remoteConfig.getString(keyDataProvidedBy)
            return when {
                text.isEmpty() -> null
                else -> text
            }
        }

    override val searchRC: Boolean
        get() = remoteConfig.getBoolean(keySearch)

    override val supportedSeasonsRC: Set<Int>
        get() = remoteConfig.getString(keySupportedSeasons).toJson<FAllSeasons>()?.convert() ?: emptySet()

    //endregion

    /**
     * Attempt to update the remote config asynchronously.
     */
    override suspend fun update(andActivate: Boolean): Boolean {
        return try {
            when (andActivate) {
                true -> {
                    remoteConfig.fetch(0L).await()
                    val activate = remoteConfig.activate().await()
                    setRemoteSyncPerformed()
                    activate
                }
                false -> {
                    remoteConfig
                            .fetch()
                            .await()
                    false
                }
            }
        } catch (e: FirebaseRemoteConfigException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            false
        } catch (e: FirebaseException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            false
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            } else {
                crashManager?.logError(e, "FirebaseRemoteConfigRepository unsupported exception thrown")
            }
            false
        }
    }

    /**
     * Activate a previously fetched remote config.
     */
    override suspend fun activate(): Boolean {
        return try {
            val result = remoteConfig
                    .activate()
                    .await()
            result
        } catch (e: FirebaseRemoteConfigException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            false
        } catch (e: FirebaseException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            false
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            else {
                crashManager?.logError(e, "FirebaseRemoteConfigRepository unsupported exception thrown")
            }
            false
        }
    }
}