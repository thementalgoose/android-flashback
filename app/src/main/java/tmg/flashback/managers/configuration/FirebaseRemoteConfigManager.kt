package tmg.flashback.managers.configuration

import com.google.firebase.FirebaseException
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await
import tmg.configuration.firebase.models.RemoteConfigAllSeasons
import tmg.configuration.firebase.models.RemoteConfigSupportedSources
import tmg.flashback.constants.App.currentYear
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.core.managers.ConfigurationManager
import tmg.flashback.core.repositories.ConfigurationRepository
import tmg.flashback.firebase.BuildConfig
import tmg.flashback.R
import tmg.flashback.core.model.ForceUpgrade
import tmg.flashback.firebase.extensions.toJson
import tmg.flashback.core.model.SupportedSource
import tmg.flashback.core.model.UpNextSchedule
import java.lang.Exception

class FirebaseRemoteConfigManager(
        private val crashController: CrashController?
): ConfigurationRepository, ConfigurationManager {

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
    private val keyForceUpgrade: String = "force_upgrade"
    private val keyDataProvidedBy: String = "data_provided"
    private val keySupportedSeasons: String = "supported_seasons"
    private val keyDashboardCalendar: String = "dashboard_calendar"
    private val keyRss: String = "rss"
    private val keyRssAddCustom: String = "rss_add_custom"
    private val keyRssSupportedSources: String = "rss_supported_sources"

    override fun setDefaults() {
        // This file is autogenerated from the build.gradle file!
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings)
    }

    //region Remote sync initial

    //endregion

    //region Variables inside remote config

    override val defaultSeason: Int
        get() = remoteConfig.getString(keyDefaultYear).toIntOrNull() ?: currentYear

    override val upNext: List<UpNextSchedule>
        get() = remoteConfig.getString(keyUpNext).toJson<RemoteConfigUpNext>()?.convert() ?: emptyList()

    override val banner: String
        get() = remoteConfig.getString(keyDefaultBanner)

    override val forceUpgrade: ForceUpgrade?
        get() = remoteConfig.getString(keyForceUpgrade).toJson<RemoteConfigForceUpgrade>()?.convert()

    override val dataProvidedBy: String?
        get() {
            val text = remoteConfig.getString(keyDataProvidedBy)
            return when {
                text.isEmpty() -> null
                else -> text
            }
        }

    override val supportedSeasons: Set<Int>
        get() = remoteConfig.getString(keySupportedSeasons).toJson<RemoteConfigAllSeasons>()?.convert() ?: emptySet()

    override val dashboardCalendar: Boolean
        get() = remoteConfig.getBoolean(keyDashboardCalendar)

    //endregion

    //region Variables inside remote config - RSS

    override val rss: Boolean
        get() = remoteConfig.getBoolean(keyRss)

    override val rssAddCustom: Boolean
        get() = remoteConfig.getBoolean(keyRssAddCustom)

    override val rssSupportedSources: List<SupportedSource>
        get() = remoteConfig.getString(keyRssSupportedSources).toJson<RemoteConfigSupportedSources>()?.convert() ?: emptyList()

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
                crashController?.logError(e, "FirebaseRemoteConfigRepository unsupported exception thrown")
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
                crashController?.logError(e, "FirebaseRemoteConfigRepository unsupported exception thrown")
            }
            false
        }
    }
}