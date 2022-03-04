package tmg.flashback.ads.repository

import com.google.android.gms.ads.nativead.NativeAd
import tmg.flashback.ads.repository.converters.convert
import tmg.flashback.ads.repository.json.AdvertConfigJson
import tmg.flashback.ads.repository.model.AdvertConfig
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.prefs.manager.PreferenceManager

class AdsRepository(
    private val configManager: ConfigManager,
    private val preferenceManager: PreferenceManager
) {
    companion object {
        private const val keyAdverts: String = "advert_config"

        private const val keyUserPreferences: String = "ADVERT_PREF"
    }

    internal var listOfAds: List<NativeAd> = emptyList()

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

    internal val isEnabled: Boolean
        get() = advertConfig.isEnabled

    val allowUserConfig: Boolean
        get() = advertConfig.allowUserConfig
}