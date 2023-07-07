package tmg.flashback.results.repository

import org.threeten.bp.Year
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.results.repository.converters.convert
import tmg.flashback.results.repository.json.AllSeasonsJson
import tmg.flashback.results.repository.json.BannerJson
import tmg.flashback.results.repository.models.Banner
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
        private const val keySearch: String = "search"


        // Prefs
        private const val keyEmptyWeeksInSchedule: String = "empty_weeks_in_schedule"
        private const val keyDashboardCollapseList: String = "DASHBOARD_COLLAPSE_LIST"
        private const val keyFavouriteSeasons: String = "FAVOURITE_SEASONS"
        private const val keyProvidedByAtTop: String = "PROVIDED_BY_AT_TOP"
    }

    /**
     * Default year as specified by the server.
     */
    val serverDefaultYear: Int by lazy {
        configManager.getString(keyDefaultYear)?.toIntOrNull() ?: Year.now().value
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
     * Supported seasons
     */
    val supportedSeasons: Set<Int>
        get() = configManager
                .getJson(keySupportedSeasons, AllSeasonsJson.serializer())
                ?.convert()
                ?: emptySet()

    /**
     * Is the searching of the statistics functionality enabled server side
     */
    val searchEnabled: Boolean
        get() = configManager.getBoolean(keySearch)

    /**
     * Show empty week indicators
     */
    var emptyWeeksInSchedule: Boolean
        get() = preferenceManager.getBoolean(keyEmptyWeeksInSchedule, false)
        set(value) = preferenceManager.save(keyEmptyWeeksInSchedule, value)


    /**
     * Default to which tab
     */
    var collapseList: Boolean
        get() = preferenceManager.getBoolean(keyDashboardCollapseList, true)
        set(value) = preferenceManager.save(keyDashboardCollapseList, value)

    /**
     * Favourited seasons in the list
     */
    var favouriteSeasons: Set<Int>
        set(value) = preferenceManager.save(keyFavouriteSeasons, value.map { it.toString() }.toSet())
        get() {
            val value = preferenceManager.getSet(keyFavouriteSeasons, setOf())
            return value
                .mapNotNull { it.toIntOrNull() }
                .toSet()
        }

    /**
     * Whether or not the "Data provided by" cell is displayed at the top of lists or not
     */
    var dataProvidedByAtTop: Boolean
        get() = preferenceManager.getBoolean(keyProvidedByAtTop, true)
        set(value) = preferenceManager.save(keyProvidedByAtTop, value)
}