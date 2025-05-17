package tmg.flashback.season.repository

import java.time.Year
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.season.repository.converters.convert
import tmg.flashback.season.repository.json.AllSeasonsJson
import tmg.flashback.season.repository.json.BannerJson
import tmg.flashback.season.repository.models.Banner
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val configManager: ConfigManager,
    private val crashlyticsManager: CrashlyticsManager
) {

    companion object {

        // Config
        private const val keyDefaultYear: String = "default_year"
        private const val keyDefaultBanners: String = "banners"
        private const val keyDataProvidedBy: String = "data_provided"
        private const val keySupportedSeasons: String = "supported_seasons"

        // Prefs
        private const val keyEmptyWeeksInSchedule: String = "empty_weeks_in_schedule"
        private const val keyRecentHighlights: String = "RECENT_HIGHLIGHTS"
        private const val keySeenSeasons: String = "SEASONS_VIEWED"
        private const val keyDashboardCollapseList: String = "DASHBOARD_COLLAPSE_LIST"
        private const val keyRememberSeasonChange: String = "REMEMBER_SEASON_CHANGE"
        private const val keyUserSeasonChange: String = "USER_SEASON_CHANGE"

        private const val keySeasonOnboarding: String = "ONBOARDING_SEASON"
    }

    /**
     * Banner to be displayed at the top of the screen
     */
    val banners: List<Banner>
        get() = configManager
            .getJson(keyDefaultBanners, BannerJson.serializer())
            ?.convert(crashlyticsManager = crashlyticsManager) ?: emptyList()

    /**
     * Banner to be displayed at the top of the screen
     */
    val dataProvidedBy: String?
        get() = configManager.getString(keyDataProvidedBy)


    /**
     * Default year as specified by the server.
     */
    val defaultSeason: Int by lazy {
        configManager.getString(keyDefaultYear)?.toIntOrNull() ?: Year.now().value
    }

    /**
     * Supported seasons
     */
    val supportedSeasons: Set<Int>
        get() = configManager
                .getJson(keySupportedSeasons, AllSeasonsJson.serializer())
                ?.convert()
                ?: emptySet()

    /**
     * All the seasons the user has viewed
     */
    var viewedSeasons: Set<Int>
        get() = preferenceManager.getSet(keySeenSeasons, emptySet())
            .mapNotNull { it.toIntOrNull() }
            .toSet()
        set(value) = preferenceManager.save(keySeenSeasons, value.map { it.toString() }.toSet())

    /**
     * Show empty week indicators
     */
    var emptyWeeksInSchedule: Boolean
        get() = preferenceManager.getBoolean(keyEmptyWeeksInSchedule, false)
        set(value) = preferenceManager.save(keyEmptyWeeksInSchedule, value)

    /**
     * Recent highlights "ie. news"
     */
    var recentHighlights: Boolean
        get() = preferenceManager.getBoolean(keyRecentHighlights, true)
        set(value) = preferenceManager.save(keyRecentHighlights, value)


    /**
     * Remembering a season change
     */
    var keepUserSelectedSeason: Boolean
        get() = preferenceManager.getBoolean(keyRememberSeasonChange, false)
        set(value) = preferenceManager.save(keyRememberSeasonChange, value)

    var userSelectedSeason: Int?
        get() = preferenceManager.getInt(keyUserSeasonChange, -1).takeIf { it != -1 }
        set(value) = preferenceManager.save(keyUserSeasonChange, value ?: -1)

    /**
     * Default to which tab
     */
    var collapseList: Boolean
        get() = preferenceManager.getBoolean(keyDashboardCollapseList, true)
        set(value) = preferenceManager.save(keyDashboardCollapseList, value)

    val hasSeenSeasonOnboarding: Boolean
        get() = preferenceManager.getBoolean(keySeasonOnboarding, false)
    fun setHasSeenSeasonOnboarding() {
        preferenceManager.save(keySeasonOnboarding, true)
    }
}