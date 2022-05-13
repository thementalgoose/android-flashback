package tmg.flashback.ui.dashboard.menu

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import tmg.flashback.RssNavigationComponent
import tmg.flashback.formula1.constants.Formula1.decadeColours
import tmg.flashback.stats.di.StatsNavigator
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import tmg.flashback.ui2.settings.SettingsAllActivity
import kotlin.math.abs

//region Inputs

interface MenuViewModelInputs {
    fun clickSeason(season: Int)

    fun clickButton(button: MenuItems.Button)
    fun clickToggle(toggle: MenuItems.Toggle)
}

//endregion

//region Outputs

interface MenuViewModelOutputs {
    val links: LiveData<List<MenuItems>>
    val season: LiveData<List<MenuSeasonItem>>
}

//endregion

class MenuViewModel(
    private val homeRepository: HomeRepository,
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val changeNightModeUseCase: ChangeNightModeUseCase,
    private val styleManager: StyleManager,
    private val rssNavigationComponent: RssNavigationComponent,
    private val navigationComponent: ApplicationNavigationComponent,
    private val statsNavigator: StatsNavigator
) : ViewModel(), MenuViewModelInputs, MenuViewModelOutputs {

    val inputs: MenuViewModelInputs = this
    val outputs: MenuViewModelOutputs = this

    private val selectedSeason: MutableStateFlow<Int> = MutableStateFlow(defaultSeasonUseCase.defaultSeason)

    override val links: MutableLiveData<List<MenuItems>> = MutableLiveData(getLinks())

    override val season: LiveData<List<MenuSeasonItem>> = selectedSeason
        .map { selectedSeason ->
            val seasons = homeRepository
                .supportedSeasons
                .sortedDescending()

            return@map seasons
                .mapIndexed { index, it ->
                    MenuSeasonItem(
                        colour = decadeColours["${it.toString().substring(0, 3)}0"] ?: Color.Gray,
                        season = it,
                        isSelected = selectedSeason == it,
                        isFirst = index == 0 || isGap(it, seasons.getOrNull(index - 1)),
                        isLast = index == (homeRepository.supportedSeasons.size - 1)  || isGap(it, seasons.getOrNull(index + 1))
                    )
                }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override fun clickSeason(season: Int) {
        this.selectedSeason.value = season
    }

    override fun clickButton(button: MenuItems.Button) {
        when (button) {
            MenuItems.Button.Contact -> {
                navigationComponent.aboutApp()
            }
            MenuItems.Button.Rss -> {
                rssNavigationComponent.rss()
            }
            MenuItems.Button.Settings -> {
                navigationComponent.settings()
            }
            MenuItems.Button.Search -> {
                statsNavigator.goToSearch()
            }
        }
        links.value = getLinks()
    }

    override fun clickToggle(toggle: MenuItems.Toggle) {
        when (toggle) {
            is MenuItems.Toggle.DarkMode -> {
                if (toggle.isEnabled) {
                    changeNightModeUseCase.setNightMode(NightMode.DAY)
                } else {
                    changeNightModeUseCase.setNightMode(NightMode.NIGHT)
                }
            }
        }
        links.value = getLinks()
    }

    private fun getLinks(): List<MenuItems> {
        val list = mutableListOf<MenuItems>()
        list.add(MenuItems.Button.Search)
        list.add(MenuItems.Button.Rss)
        list.add(MenuItems.Button.Settings)
        list.add(MenuItems.Button.Contact)
        list.add(MenuItems.Divider)
        list.add(MenuItems.Toggle.DarkMode(
            _isEnabled = !styleManager.isDayMode
        ))
        return list
    }

    private fun isGap(ref: Int, targetSeason: Int?): Boolean {
        if (targetSeason == null) {
            return true
        }
        return abs(targetSeason - ref) > 1
    }
}