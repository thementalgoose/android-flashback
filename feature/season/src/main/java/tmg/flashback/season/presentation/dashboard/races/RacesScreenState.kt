package tmg.flashback.season.presentation.dashboard.races

import tmg.flashback.formula1.enums.SeasonTyres
import tmg.flashback.formula1.enums.getBySeason
import tmg.flashback.weekend.navigation.ScreenWeekendData


data class RacesScreenState(
    val season: Int,
    val items: List<RacesModel>? = listOf(),
    val isLoading: Boolean = false,
    val networkAvailable: Boolean = true,
    val currentRace: ScreenWeekendData? = null,
    val showAdvert: Boolean = false,
    val showEvents: Boolean = false
) {
    val showTyres: Boolean
        get() = SeasonTyres.getBySeason(season) != null
}