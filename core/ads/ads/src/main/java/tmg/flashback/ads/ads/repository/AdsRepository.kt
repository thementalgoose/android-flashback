package tmg.flashback.ads.ads.repository

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import tmg.flashback.ads.ads.BuildConfig
import tmg.flashback.ads.ads.repository.converters.convert
import tmg.flashback.ads.ads.repository.json.AdvertConfigJson
import tmg.flashback.ads.ads.repository.model.AdvertConfig
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.utilities.extensions.md5
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdsRepository @Inject constructor(
    private val configManager: ConfigManager,
    private val preferenceManager: PreferenceManager
) {
    companion object {
        private const val keyAdverts: String = "advert_config"

        private const val keyUserPreferences: String = "ADVERT_PREF"
    }

    var userPrefEnabled: Boolean
        get() = preferenceManager.getBoolean(keyUserPreferences, true)
        set(value) {
            preferenceManager.save(keyUserPreferences, value)
        }

    /**
     * Are adverts enabled or not based off the configuration
     */
    val areAdvertsEnabled: Boolean
        get() = when {
            isEnabled && allowUserConfig -> userPrefEnabled
            isEnabled -> true
            else -> false
        }

    val advertConfig: AdvertConfig by lazy {
        return@lazy configManager
            .getJson(keyAdverts, AdvertConfigJson.serializer())
            .convert()
    }

    val isEnabled: Boolean
        get() = advertConfig.isEnabled

    val allowUserConfig: Boolean
        get() = advertConfig.allowUserConfig


    /**
     * Get the current admob device test id
     *  This is the same ID used by admob for test device id!
     *
     * Visible for DebugActivity
     */
    fun getCurrentDeviceId(context: Context): String? {
        if (BuildConfig.DEBUG) {
            @SuppressLint("HardwareIds")
            val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            val deviceId = androidId.md5.uppercase()
            Log.i("Ads", "Current Device Id found to be $deviceId")
            return deviceId
        }
        return null
    }
}