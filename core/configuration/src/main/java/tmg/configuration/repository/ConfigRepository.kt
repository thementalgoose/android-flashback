package tmg.configuration.repository

import org.threeten.bp.Year
import tmg.configuration.firebase.models.*
import tmg.configuration.managers.RemoteConfigManager
import tmg.configuration.managers.getObject
import tmg.configuration.repository.models.ForceUpgrade
import tmg.configuration.repository.models.SupportedSource
import tmg.configuration.repository.models.UpNextSchedule

class ConfigRepository(
    private val remoteConfigManager: RemoteConfigManager
) {

    companion object {
        private const val keyDefaultYear: String = "default_year"
        private const val keyUpNext: String = "up_next"
        private const val keyDefaultBanner: String = "banner"
        private const val keyForceUpgrade: String = "force_upgrade"
        private const val keyDataProvidedBy: String = "data_provided"
        private const val keySupportedSeasons: String = "supported_seasons"
        private const val keyDashboardCalendar: String = "dashboard_calendar"
        private const val keyRss: String = "rss"
        private const val keyRssAddCustom: String = "rss_add_custom"
        private const val keyRssSupportedSources: String = "rss_supported_sources"
    }

    val supportedSeasons: Set<Int>
        get() = remoteConfigManager
            .getObject<RemoteConfigAllSeasons>(keySupportedSeasons)
            ?.convert() ?: emptySet<Int>()

    val defaultSeason: Int by lazy {
        remoteConfigManager
            .getString(keyDefaultYear).toIntOrNull() ?: Year.now().value
    }

    val banner: String
        get() = remoteConfigManager.getString(keyDefaultBanner)

    val forceUpgrade: ForceUpgrade? by lazy {
        remoteConfigManager
            .getString(keyForceUpgrade)
            .toForceUpgrade()
    }

    val dataProvidedBy: String
        get() = remoteConfigManager.getString(keyDataProvidedBy)

    val dashboardCalendar: Boolean by lazy {
        remoteConfigManager.getBoolean(keyDashboardCalendar)
    }

    val upNext: List<UpNextSchedule>
        get() = remoteConfigManager
            .getJson(keyUpNext) { source: RemoteConfigUpNext ->
                source.convert()
            }
            ?: emptyList()

    val rss: Boolean by lazy {
        remoteConfigManager.getBoolean(keyRss)
    }

    val rssAddCustom: Boolean by lazy {
        remoteConfigManager.getBoolean(keyRssAddCustom)
    }

    val rssSupportedSources: List<SupportedSource> by lazy {
        remoteConfigManager
            .getJson(keyRssSupportedSources) { source: RemoteConfigSupportedSources ->
                source.convert()
            }
            ?: emptyList()
    }
}