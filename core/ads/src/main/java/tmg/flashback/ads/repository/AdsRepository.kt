package tmg.flashback.ads.repository

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
        private const val keyAdverts: String = "adverts"

        private const val keyUserPreferences: String = "ADVERT_PREF"
    }

    var userPrefEnabled: Boolean
        get() = preferenceManager.getBoolean(keyUserPreferences, false)
        set(value) {
            preferenceManager.save(keyUserPreferences, value)
        }

    val advertConfig: AdvertConfig by lazy {
        return@lazy configManager
            .getJson<AdvertConfigJson>(keyAdverts)
            .convert()
    }

    val isEnabled: Boolean
        get() = advertConfig.isEnabled

    val allowUserConfig: Boolean
        get() = advertConfig.allowUserConfig
}