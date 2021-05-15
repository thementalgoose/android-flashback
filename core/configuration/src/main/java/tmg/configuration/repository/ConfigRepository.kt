package tmg.configuration.repository

import org.threeten.bp.Year
import tmg.configuration.extensions.toJson
import tmg.configuration.services.RemoteConfigService
import tmg.configuration.repository.json.*
import tmg.configuration.repository.models.ForceUpgrade
import tmg.core.prefs.manager.PreferenceManager

class ConfigRepository(
        private val remoteConfigService: RemoteConfigService,
        private val preferenceManager: PreferenceManager
) {

    companion object {
        private const val keyRemoteConfigSync: String = "REMOTE_CONFIG_SYNC_COUNT"

        private const val keyDefaultYear: String = "default_year"
        private const val keyDefaultBanner: String = "banner"
        private const val keyForceUpgrade: String = "force_upgrade"
        private const val keyDataProvidedBy: String = "data_provided"
        private const val keySupportedSeasons: String = "supported_seasons"
        private const val keyDashboardCalendar: String = "dashboard_calendar"
    }

    //region Values

    //endregion

    //region Shared Prefs

    var remoteConfigSync: Int
        get() = preferenceManager.getInt(keyRemoteConfigSync, 0)
        set(value) = preferenceManager.save(keyRemoteConfigSync, value)

    //endregion

    //region Remote Config values

    /**
     * Get a list of all the seasons to show in the list
     */
    val supportedSeasons: Set<Int>
        get() = remoteConfigService
            .getString(keySupportedSeasons)
            .toJson<AllSeasonsJson>()
            ?.convert()
            ?: emptySet()

    /**
     * What year we should default too when opening the app
     *  This is the one flashback determines is the best to show
     */
    val defaultSeason: Int by lazy {
        remoteConfigService
            .getString(keyDefaultYear).toIntOrNull() ?: Year.now().value
    }

    /**
     * Banner to be shown at the top of the home screen
     */
    /**
     * Banner to be shown at the top of the home screen
     */
    val banner: String
        get() = remoteConfigService.getString(keyDefaultBanner)

    /**
     * Force upgrade message to be shown
     * {
     *   "title": "Upgrade now",
     *   "message": "Message to explain the decision",
     *   "link": "https://www.google.com"
     *   "linkText": "Go to play store"
     * }
     */
    val forceUpgrade: ForceUpgrade?
        get() = remoteConfigService
            .getString(keyForceUpgrade)
            .toJson<ForceUpgradeJson>()
            ?.convert()

    /**
     * Data provided by tag
     *  Text to be displayed on every statistics screen of who the statistics are provided by
     */
    val dataProvidedBy: String
        get() = remoteConfigService.getString(keyDataProvidedBy)

    /**
     * The new calendar tab in the dashboard should be enabled or not
     */
    val dashboardCalendar: Boolean by lazy {
        remoteConfigService.getBoolean(keyDashboardCalendar)
    }

    //endregion
}