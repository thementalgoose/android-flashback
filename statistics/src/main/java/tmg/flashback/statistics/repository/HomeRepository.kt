package tmg.flashback.statistics.repository

import org.threeten.bp.Year
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.statistics.repository.converters.convert
import tmg.flashback.statistics.repository.json.AllSeasonsJson
import tmg.flashback.statistics.repository.json.BannerJson
import tmg.flashback.statistics.repository.models.Banner

class HomeRepository(
        private val preferenceManager: PreferenceManager,
        private val configManager: ConfigManager
) {

    companion object {

        // Config
        private const val keyDefaultYear: String = "default_year"
        private const val keyDefaultBanner: String = "banner"
        private const val keyDataProvidedBy: String = "data_provided"
        private const val keySupportedSeasons: String = "supported_seasons"
        private const val keySearch: String = "search"

        // Prefs
        private const val keyShowQualifyingDelta: String = "SHOW_QUALIFYING_DELTA"
        private const val keyDefaultToSchedule: String = "DASHBOARD_DEFAULT_TAB_SCHEDULE"
        private const val keyFadeDNF: String = "FADE_DNF"
        private const val keyShowListFavourited: String = "BOTTOM_SHEET_FAVOURITED"
        private const val keyShowListAll: String = "BOTTOM_SHEET_ALL"
        private const val keyShowGridPenaltiesInQualifying: String = "SHOW_GRID_PENALTIES_IN_QUALIFYING"
        private const val keyDashboardAutoscroll: String = "DASHBOARD_AUTOSCROLL"
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
    val banner: Banner?
        get() = configManager
            .getJson(keyDefaultBanner, BannerJson.serializer())
            ?.convert()

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
     * Default to which tab
     */
    var defaultToSchedule: Boolean
        get() = preferenceManager.getBoolean(keyDefaultToSchedule, true)
        set(value) = preferenceManager.save(keyDefaultToSchedule, value)

    /**
     * Show the qualifying delta in the layout
     */
    var showQualifyingDelta: Boolean
        get() = preferenceManager.getBoolean(keyShowQualifyingDelta, false)
        set(value) = preferenceManager.save(keyShowQualifyingDelta, value)

    /**
     * Fade the race results where the driver results in DNF
     */
    var fadeDNF: Boolean
        get() = preferenceManager.getBoolean(keyFadeDNF, true)
        set(value) = preferenceManager.save(keyFadeDNF, value)

    /**
     * Show the favourited bottom sheet section expanded by default
     */
    var showListFavourited: Boolean
        get() = preferenceManager.getBoolean(keyShowListFavourited, true)
        set(value) = preferenceManager.save(keyShowListFavourited, value)

    /**
     * Show the all bottom sheet section expanded by default
     */
    var showListAll: Boolean
        get() = preferenceManager.getBoolean(keyShowListAll, true)
        set(value) = preferenceManager.save(keyShowListAll, value)

    /**
     * Show grid penalties in qualifying
     */
    var showGridPenaltiesInQualifying: Boolean
        get() = preferenceManager.getBoolean(keyShowGridPenaltiesInQualifying, true)
        set(value) = preferenceManager.save(keyShowGridPenaltiesInQualifying, value)

    /**
     * When loading the dashboard, should the app autoscroll to the current season if it's applicable
     */
    var dashboardAutoscroll: Boolean
        get() = preferenceManager.getBoolean(keyDashboardAutoscroll, true)
        set(value) = preferenceManager.save(keyDashboardAutoscroll, value)

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