package tmg.flashback.ui.dashboard

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmg.flashback.crash_reporting.manager.CrashManager
import tmg.flashback.debug.DebugNavigationComponent
import tmg.flashback.debug.model.DebugMenuItem
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.rss.RSS
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.stats.Calendar
import tmg.flashback.stats.Constructors
import tmg.flashback.stats.Drivers
import tmg.flashback.stats.Search
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.stats.with
import tmg.flashback.ui.components.navigation.NavigationTimelineItem
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
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
    val currentlySelectedItem: LiveData<MenuItem>
    val debugMenuItems: LiveData<List<DebugMenuItem>>
    val appFeatureItemsList: LiveData<List<MenuItem>>
    val seasonScreenItemsList: LiveData<List<MenuItem>>

    val showBottomBar: LiveData<Boolean>
    val showMenu: LiveData<Boolean>

    val seasonsItemsList: LiveData<List<NavigationTimelineItem>>
    val currentlySelectedSeason: LiveData<Int>
}

@HiltViewModel
class DashboardNavViewModel @Inject constructor(
    private val rssRepository: RSSRepository,
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val navigator: tmg.flashback.navigation.Navigator,
    private val getSeasonUseCase: GetSeasonsUseCase,
    private val applicationNavigationComponent: tmg.flashback.navigation.ApplicationNavigationComponent,
    private val crashManager: CrashManager,
    private val dashboardSyncUseCase: DashboardSyncUseCase,
    private val debugNavigationComponent: DebugNavigationComponent,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), DashboardNavViewModelInputs, DashboardNavViewModelOutputs {

    val inputs: DashboardNavViewModelInputs = this
    val outputs: DashboardNavViewModelOutputs = this

    private val currentDestination = navigator
        .destination
        .asSharedFlow()

    override val currentlySelectedItem: LiveData<MenuItem> = currentDestination
        .map { destination ->
            if (destination == null) return@map null
            val item: MenuItem? = when {
                destination.route.startsWith("results/calendar/") -> MenuItem.Calendar
                destination.route.startsWith("results/drivers/") -> MenuItem.Drivers
                destination.route.startsWith("results/constructors/") -> MenuItem.Constructors
                destination.route.startsWith("settings") -> MenuItem.Settings
                destination.route.startsWith("rss") -> MenuItem.RSS
                destination.route.startsWith("search") -> MenuItem.Search
                else -> null
            }
            return@map item
        }
        .filterNotNull()
        .asLiveData(viewModelScope.coroutineContext)

    override val showMenu: LiveData<Boolean> = currentDestination
        .map { destination ->
            if (destination == null) return@map null

            return@map when {
                destination.route.startsWith("results/") -> true
                destination.route == "settings" -> true
                destination.route == "rss" -> true
                destination.route == "search" -> true
                else -> false
            }
        }
        .filterNotNull()
        .asLiveData(viewModelScope.coroutineContext)

    override val showBottomBar: LiveData<Boolean> = currentDestination
        .map {
            if (it == null) return@map false
            return@map it.route.startsWith("results/")
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val appFeatureItemsList: MutableLiveData<List<MenuItem>> = MutableLiveData()
    override val seasonScreenItemsList: MutableLiveData<List<MenuItem>> = MutableLiveData()
    override val debugMenuItems: LiveData<List<DebugMenuItem>> = MutableLiveData(debugNavigationComponent.getDebugMenuItems())

    override val currentlySelectedSeason: MutableLiveData<Int> = MutableLiveData(defaultSeasonUseCase.defaultSeason)

    override val seasonsItemsList: LiveData<List<NavigationTimelineItem>> = currentlySelectedSeason
        .map { season -> getSeasons(season) }

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
        seasonScreenItemsList.postValue(bottom)
        appFeatureItemsList.postValue(side)
    }

    override fun clickSeason(season: Int) {
        currentlySelectedSeason.value = season
        val current = currentlySelectedItem.value ?: return
        when (current) {
            MenuItem.Calendar -> navigator.navigate(tmg.flashback.navigation.Screen.Calendar.with(season))
            MenuItem.Constructors -> navigator.navigate(tmg.flashback.navigation.Screen.Constructors.with(season))
            MenuItem.Drivers -> navigator.navigate(tmg.flashback.navigation.Screen.Drivers.with(season))
            else -> { /* Do nothing */ }
        }

    }

    override fun clickItem(navigationItem: MenuItem) {
        val currentSeason = currentlySelectedSeason.value

        when (navigationItem) {
            MenuItem.Calendar -> navigator.navigate(tmg.flashback.navigation.Screen.Calendar.with(currentSeason ?: defaultSeasonUseCase.defaultSeason))
            MenuItem.Drivers -> navigator.navigate(tmg.flashback.navigation.Screen.Drivers.with(currentSeason ?: defaultSeasonUseCase.defaultSeason))
            MenuItem.Constructors -> navigator.navigate(tmg.flashback.navigation.Screen.Constructors.with(currentSeason ?: defaultSeasonUseCase.defaultSeason))
            MenuItem.Contact -> applicationNavigationComponent.aboutApp()
            MenuItem.RSS -> navigator.navigate(tmg.flashback.navigation.Screen.RSS)
            MenuItem.Search -> navigator.navigate(tmg.flashback.navigation.Screen.Search)
            MenuItem.Settings -> navigator.navigate(tmg.flashback.navigation.Screen.Settings.All)
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
}