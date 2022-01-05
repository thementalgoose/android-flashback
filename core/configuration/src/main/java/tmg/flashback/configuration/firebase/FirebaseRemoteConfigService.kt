package tmg.flashback.configuration.firebase

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await
import tmg.flashback.configuration.BuildConfig
import tmg.flashback.configuration.R
import tmg.flashback.configuration.services.RemoteConfigService
import java.lang.Exception

class FirebaseRemoteConfigService: RemoteConfigService {

    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    private val remoteConfigSettings = FirebaseRemoteConfigSettings
        .Builder()
        .apply {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) {
                10L // 10s
            } else {
                10800L // 3h
            }
        }
        .build()

    override suspend fun fetch(andActivate: Boolean): Boolean {
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
            }
            false
        }
    }

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
            false
        }
    }

    override suspend fun reset(): Boolean {
        if (BuildConfig.DEBUG) {
            Log.i("Config", "Config service reset called")
        }
        remoteConfig.reset().await()
        return true
    }

    override fun initialiseRemoteConfig() {
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings)
    }

    override fun getBoolean(key: String): Boolean {
        return remoteConfig.getBoolean(key)
    }

    override fun getString(key: String): String {
        return remoteConfig.getString(key)
    }
}