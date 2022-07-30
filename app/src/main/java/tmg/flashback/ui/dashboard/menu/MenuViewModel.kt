package tmg.flashback.ui.dashboard.menu

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import tmg.flashback.BuildConfig
import tmg.flashback.debug.DebugNavigationComponent
import tmg.flashback.debug.debugMenuItemList
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.rss.RssNavigationComponent
import tmg.flashback.formula1.constants.Formula1.decadeColours
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.ui.Home
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import javax.inject.Inject
import kotlin.math.abs

//region Inputs

interface MenuViewModelInputs {
    fun clickSeason(season: Int)

    fun clickButton(button: MenuItems.Button)
    fun clickToggle(toggle: MenuItems.Toggle)
    fun clickFeature(feature: MenuItems.Feature)
}

//endregion

//region Outputs

interface MenuViewModelOutputs {
    val links: LiveData<List<MenuItems>>
    val season: LiveData<List<MenuSeasonItem>>
}

//endregion

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val notificationRepository: NotificationRepository,
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val changeNightModeUseCase: ChangeNightModeUseCase,
    private val styleManager: StyleManager,
    private val rssNavigationComponent: RssNavigationComponent,
    private val navigationComponent: ApplicationNavigationComponent,
    private val statsNavigationComponent: StatsNavigationComponent,
    private val debugNavigationComponent: DebugNavigationComponent
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
                statsNavigationComponent.search()
            }
            is MenuItems.Button.Custom -> {
                debugNavigationComponent.navigateTo(button.id)
            }
        }
        links.value = getLinks()
    }

    override fun clickToggle(toggle: MenuItems.Toggle) {
        when (toggle) {
            is MenuItems.Toggle.DarkMode -> {
                navigationComponent.home()
                if (toggle.isEnabled) {
                    changeNightModeUseCase.setNightMode(NightMode.DAY)
                } else {
                    changeNightModeUseCase.setNightMode(NightMode.NIGHT)
                }
            }
        }
        links.value = getLinks()
    }

    override fun clickFeature(feature: MenuItems.Feature) {
        when (feature) {
            MenuItems.Feature.Notifications -> {
                statsNavigationComponent.featureNotificationOnboarding()
                notificationRepository.seenNotificationOnboarding = true
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
        if (debugMenuItemList.isNotEmpty()) {
            list.add(MenuItems.Divider("z"))
            debugMenuItemList.forEach {
                list.add(MenuItems.Button.Custom(it.label, it.icon, it.id))
            }
        }
        list.add(MenuItems.Divider("a"))
        list.add(MenuItems.Toggle.DarkMode(
            _isEnabled = !styleManager.isDayMode
        ))
        if (!notificationRepository.seenNotificationOnboarding) {
            list.add(MenuItems.Divider("b"))
            list.add(MenuItems.Feature.Notifications)
        }
        return list
    }

    private fun isGap(ref: Int, targetSeason: Int?): Boolean {
        if (targetSeason == null) {
            return true
        }
        return abs(targetSeason - ref) > 1
    }
}