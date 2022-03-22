package tmg.flashback.ui.dashboard.menu

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import tmg.flashback.R
import tmg.flashback.formula1.constants.Formula1.decadeColours
import tmg.flashback.statistics.repository.HomeRepository
import tmg.flashback.statistics.usecases.DefaultSeasonUseCase

//region Inputs

interface MenuViewModelInputs {
    fun clickSeason(season: Int)
}

//endregion

//region Outputs

interface MenuViewModelOutputs {
    val buttons: LiveData<List<MenuButtonItem>>
    val season: LiveData<List<MenuSeasonItem>>
}

//endregion

class MenuViewModel(
    private val homeRepository: HomeRepository,
    private val defaultSeasonUseCase: DefaultSeasonUseCase
) : ViewModel(), MenuViewModelInputs, MenuViewModelOutputs {

    val inputs: MenuViewModelInputs = this
    val outputs: MenuViewModelOutputs = this

    private val selectedSeason: MutableStateFlow<Int> =
        MutableStateFlow(defaultSeasonUseCase.defaultSeason)

    override val buttons: MutableLiveData<List<MenuButtonItem>> = MutableLiveData(
        listOf(
            MenuButtonItem.Button(
                label = R.string.dashboard_season_list_extra_settings_title,
                icon = R.drawable.nav_settings
            ),
            MenuButtonItem.Button(
                label = R.string.dashboard_season_list_extra_rss_title,
                icon = R.drawable.nav_rss
            ),
            MenuButtonItem.Button(
                label = R.string.dashboard_season_list_extra_contact_title,
                icon = R.drawable.ic_contact
            )
        )
    )
    override val season: LiveData<List<MenuSeasonItem>> = selectedSeason
        .map { selectedSeason ->
            homeRepository
                .supportedSeasons
                .sortedDescending()
                .map {
                    MenuSeasonItem(
                        colour = decadeColours["${it.toString().substring(0, 3)}0"] ?: Color.Gray,
                        season = it,
                        isSelected = selectedSeason == it
                    )
                }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override fun clickSeason(season: Int) {
        this.selectedSeason.value = season
    }

}