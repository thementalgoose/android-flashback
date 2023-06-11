package tmg.flashback.ui.dashboard

import android.os.Bundle
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tmg.flashback.crash_reporting.manager.CrashManager
import tmg.flashback.debug.DebugNavigationComponent
import tmg.flashback.debug.model.DebugMenuItem
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.results.Calendar
import tmg.flashback.results.Constructors
import tmg.flashback.results.Drivers
import tmg.flashback.results.usecases.DefaultSeasonUseCase
import tmg.flashback.results.with
import tmg.flashback.rss.contract.RSS
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.search.contract.Search
import tmg.flashback.ui.components.navigation.NavigationTimelineItem
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.ui.settings.All
import tmg.flashback.usecases.DashboardSyncUseCase
import tmg.flashback.usecases.GetSeasonsUseCase
import javax.inject.Inject

interface DashboardNavViewModelInputs {
    fun clickItem(navigationItem: MenuItem)
    fun clickSeason(season: Int)
    fun clickDebug(debugMenuItem: DebugMenuItem)
}

interface DashboardNavViewModelOutputs {
    val currentlySelectedItem: StateFlow<MenuItem>
    val debugMenuItems: StateFlow<List<DebugMenuItem>>
    val appFeatureItemsList: StateFlow<List<MenuItem>>
    val seasonScreenItemsList: StateFlow<List<MenuItem>>

    val showBottomBar: StateFlow<Boolean>
    val showMenu: StateFlow<Boolean>

    val seasonsItemsList: StateFlow<List<NavigationTimelineItem>>
    val currentlySelectedSeason: StateFlow<Int>

    val defaultSeason: Int
}

@HiltViewModel
class DashboardNavViewModel @Inject constructor(
    private val rssRepository: RssRepository,
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val navigator: Navigator,
    private val getSeasonUseCase: GetSeasonsUseCase,
    private val applicationNavigationComponent: ApplicationNavigationComponent,
    private val crashManager: CrashManager,
    private val dashboardSyncUseCase: DashboardSyncUseCase,
    private val debugNavigationComponent: DebugNavigationComponent,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), DashboardNavViewModelInputs, DashboardNavViewModelOutputs,
    NavController.OnDestinationChangedListener {

    val inputs: DashboardNavViewModelInputs = this
    val outputs: DashboardNavViewModelOutputs = this

    override val defaultSeason: Int = defaultSeasonUseCase.defaultSeason

    private val currentDestination: MutableStateFlow<String?> = MutableStateFlow(null)

    override val currentlySelectedItem: StateFlow<MenuItem> = currentDestination
        .map { destination ->
            if (destination == null) return@map null
            val item: MenuItem? = when {
                destination.startsWith("results/calendar/") -> MenuItem.Calendar
                destination.startsWith("results/drivers/") -> MenuItem.Drivers
                destination.startsWith("results/constructors/") -> MenuItem.Constructors
                destination.startsWith("settings") -> MenuItem.Settings
                destination.startsWith("rss") -> MenuItem.RSS
                destination.startsWith("search") -> MenuItem.Search
                else -> null
            }
            return@map item
        }
        .filterNotNull()
        .stateIn(viewModelScope, SharingStarted.Lazily, MenuItem.Calendar)

    override val showMenu: StateFlow<Boolean> = currentDestination
        .map { destination ->
            if (destination == null) return@map null

            return@map when {
                destination.startsWith("results/") -> true
                destination == "settings" -> true
                destination == "rss" -> true
                destination == "search" -> true
                else -> false
            }
        }
        .filterNotNull()
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    override val showBottomBar: StateFlow<Boolean> = currentDestination
        .map {
            if (it == null) return@map false
            return@map it.startsWith("results/")
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    override val appFeatureItemsList: MutableStateFlow<List<MenuItem>> = MutableStateFlow(emptyList())
    override val seasonScreenItemsList: MutableStateFlow<List<MenuItem>> = MutableStateFlow(emptyList())
    override val debugMenuItems: MutableStateFlow<List<DebugMenuItem>> = MutableStateFlow(debugNavigationComponent.getDebugMenuItems())

    override val currentlySelectedSeason: MutableStateFlow<Int> = MutableStateFlow(defaultSeasonUseCase.defaultSeason)

    override val seasonsItemsList: StateFlow<List<NavigationTimelineItem>> = currentlySelectedSeason
        .map { season -> getSeasons(season) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        initialiseItems()

        viewModelScope.launch(ioDispatcher) {
            val result = dashboardSyncUseCase.sync()
            crashManager.log("Dashboard synchronisation complete $result")
        }
    }

    private fun initialiseItems() {
        val bottom = listOf(MenuItem.Calendar, MenuItem.Drivers, MenuItem.Constructors)
        val side = mutableListOf<MenuItem>().apply {
            add(MenuItem.Search)
            if (rssRepository.enabled) {
                add(MenuItem.RSS)
            }
            add(MenuItem.Settings)
            add(MenuItem.Contact)
        }
        seasonScreenItemsList.value = bottom
        appFeatureItemsList.value = side
    }

    override fun clickSeason(season: Int) {
        currentlySelectedSeason.value = season
        val current = currentlySelectedItem.value
        when (current) {
            MenuItem.Calendar -> navigator.navigate(Screen.Calendar.with(season))
            MenuItem.Constructors -> navigator.navigate(Screen.Constructors.with(season))
            MenuItem.Drivers -> navigator.navigate(Screen.Drivers.with(season))
            else -> { /* Do nothing */ }
        }

    }

    override fun clickItem(navigationItem: MenuItem) {
        val currentSeason = currentlySelectedSeason.value

        when (navigationItem) {
            MenuItem.Calendar -> navigator.navigate(Screen.Calendar.with(currentSeason ?: defaultSeasonUseCase.defaultSeason))
            MenuItem.Drivers -> navigator.navigate(Screen.Drivers.with(currentSeason ?: defaultSeasonUseCase.defaultSeason))
            MenuItem.Constructors -> navigator.navigate(Screen.Constructors.with(currentSeason ?: defaultSeasonUseCase.defaultSeason))
            MenuItem.Contact -> applicationNavigationComponent.aboutApp()
            MenuItem.RSS -> navigator.navigate(Screen.RSS)
            MenuItem.Search -> navigator.navigate(Screen.Search)
            MenuItem.Settings -> navigator.navigate(Screen.Settings.All)
        }
    }

    private fun getSeasons(selectedSeason: Int? = null): List<NavigationTimelineItem> {
        val allSeasons = getSeasonUseCase.get()
        val seasons = allSeasons
            .keys
            .sortedDescending()

        return seasons
            .mapIndexed { index, it ->
                val (isFirst, isLast) = allSeasons[it]!!
                NavigationTimelineItem(
                    color = Formula1.decadeColours["${it.toString().substring(0, 3)}0"] ?: Color.Gray,
                    label = it.toString(),
                    id = it.toString(),
                    isSelected = selectedSeason == it,
                    pipeType = when {
                        isFirst.value && isLast.value -> PipeType.SINGLE
                        isFirst.value && !isLast.value -> PipeType.START
                        !isFirst.value && isLast.value -> PipeType.END
                        else -> PipeType.START_END
                    }
                )
            }
    }

    override fun clickDebug(debugMenuItem: DebugMenuItem) {
        debugNavigationComponent.navigateTo(debugMenuItem.id)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        currentDestination.value = destination.route
    }
}