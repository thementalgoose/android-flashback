package tmg.flashback.statistics.repository

import tmg.core.device.repository.SharedPreferenceRepository

class StatisticsRepository(
    private val sharedPreferenceRepository: SharedPreferenceRepository
) {

    companion object {
        private const val keyShowQualifyingDelta: String = "SHOW_QUALIFYING_DELTA"
        private const val keyFadeDNF: String = "FADE_DNF"
        private const val keyShowListFavourited: String = "BOTTOM_SHEET_FAVOURITED"
        private const val keyShowListAll: String = "BOTTOM_SHEET_ALL"
        private const val keyShowGridPenaltiesInQualifying: String = "SHOW_GRID_PENALTIES_IN_QUALIFYING"
        private const val keyFavouriteSeasons: String = "FAVOURITE_SEASONS"
        private const val keyDefaultSeason: String = "DEFAULT_SEASON"
        private const val keyWidgetOpenApp: String = "WIDGET_OPEN_BEHAVIOR"
    }

    /**
     * Show the qualifying delta in the layout
     */
    var showQualifyingDelta: Boolean
        get() = sharedPreferenceRepository.getBoolean(keyShowQualifyingDelta, false)
        set(value) = sharedPreferenceRepository.save(keyShowQualifyingDelta, value)

    /**
     * Fade the race results where the driver results in DNF
     */
    var fadeDNF: Boolean
        get() = sharedPreferenceRepository.getBoolean(keyFadeDNF, false)
        set(value) = sharedPreferenceRepository.save(keyFadeDNF, value)

    /**
     * Show the favourited bottom sheet section expanded by default
     */
    var showListFavourited: Boolean
        get() = sharedPreferenceRepository.getBoolean(keyShowListFavourited, false)
        set(value) = sharedPreferenceRepository.save(keyShowListFavourited, value)

    /**
     * Show the all bottom sheet section expanded by default
     */
    var showListAll: Boolean
        get() = sharedPreferenceRepository.getBoolean(keyShowListAll, false)
        set(value) = sharedPreferenceRepository.save(keyShowListAll, value)

    /**
     * Show grid penalties in qualifying
     */
    var showGridPenaltiesInQualifying: Boolean
        get() = sharedPreferenceRepository.getBoolean(keyShowGridPenaltiesInQualifying, false)
        set(value) = sharedPreferenceRepository.save(keyShowGridPenaltiesInQualifying, value)

    /**
     * Favourited seasons in the list
     */
    var favouriteSeasons: Set<Int>
        set(value) = sharedPreferenceRepository.save(keyFavouriteSeasons, value.map { it.toString() }.toSet())
        get() {
            val value = sharedPreferenceRepository.getSet(keyFavouriteSeasons, setOf())
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
            val value = sharedPreferenceRepository.getInt(keyDefaultSeason, -1)
            if (value == -1) return null
            return value
        }
        set(value) {
            val valueToSave = when (value) {
                null -> -1
                else -> value
            }
            sharedPreferenceRepository.save(keyDefaultSeason, valueToSave)
        }

    /**
     * What should happen when the widget is clicked
     *  true = Open the app
     *  false = Refresh the widget
     */
    var widgetOpenApp: Boolean
        get() = sharedPreferenceRepository.getBoolean(keyWidgetOpenApp, false)
        set(value) = sharedPreferenceRepository.save(keyWidgetOpenApp, value)

}