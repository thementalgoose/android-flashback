package tmg.flashback.firebase.config

import com.google.firebase.FirebaseException
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await
import tmg.flashback.firebase.BuildConfig
import tmg.flashback.firebase.R
import tmg.flashback.firebase.currentYear
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.db.CrashManager
import java.lang.Exception

/**
 * Read the default values from remote config
 *
 * TODO: Move app lockout over to using this remote config repository!
 */
class FirebaseRemoteConfigRepository(
        val crashManager: CrashManager
): RemoteConfigRepository {

    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    private val configSettings = FirebaseRemoteConfigSettings
            .Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    minimumFetchIntervalInSeconds = 10L
                }
            }
            .build()

    private val keyDefaultYear: String = "default_year"
    private val keyDefaultBanner: String = "banner"

    init {
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    //region Variables inside remote config

    override val defaultYear: Int
        get() = remoteConfig.getString(keyDefaultYear).toIntOrNull() ?: currentYear

    override val banner: String
        get() = remoteConfig.getString(keyDefaultBanner)

    //endregion

    /**
     * Attempt to update the remote config asynchronously.
     */
    override suspend fun update(andActivate: Boolean): Boolean {
        return try {
             when (andActivate) {
                true -> remoteConfig
                        .fetchAndActivate()
                        .await()
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
                crashManager.logError(e, "FirebaseRemoteConfigRepository unsupported exception thrown")
            }
            false
        }
    }

    /**
     * Activate a previously fetched remote config.
     */
    override suspend fun activate(): Boolean {
        return try {
            remoteConfig
                    .activate()
                    .await()
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
                crashManager.logError(e, "FirebaseRemoteConfigRepository unsupported exception thrown")
            }
            false
        }
    }
}