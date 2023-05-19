package tmg.flashback.results.repository

import org.threeten.bp.Year
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.crash_reporting.manager.CrashManager
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
        private val crashManager: CrashManager
) {

    companion object {

        // Config
        private const val keyDefaultYear: String = "default_year"
        private const val keyDefaultBanners: String = "banners"
        private const val keyDataProvidedBy: String = "data_provided"
        private const val keySupportedSeasons: String = "supported_seasons"
        private const val keySearch: String = "search"
        private const val keyEmptyWeeksInSchedule: String = "empty_weeks_in_schedule"

        // Prefs
        private const val keyDashboardCollapseList: String = "DASHBOARD_COLLAPSE_LIST"
        private const val keyFavouriteSeasons: String = "FAVOURITE_SEASONS"
        private const val keyDefaultSeason: String = "DEFAULT_SEASON"
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
            ?.convert(crashManager = crashManager) ?: emptyList()

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
     * Default season to be shown in the app
     *  null = no default set
     */
    var defaultSeason: Int?
        get() {
            val value = preferenceManager.getInt(keyDefaultSeason, -1)
            if (value == -1) return null
            return value
        }
        set(value) {
            val valueToSave = when (value) {
                null -> -1
                else -> value
            }
            preferenceManager.save(keyDefaultSeason, valueToSave)
        }

    /**
     * Whether or not the "Data provided by" cell is displayed at the top of lists or not
     */
    var dataProvidedByAtTop: Boolean
        get() = preferenceManager.getBoolean(keyProvidedByAtTop, true)
        set(value) = preferenceManager.save(keyProvidedByAtTop, value)
}