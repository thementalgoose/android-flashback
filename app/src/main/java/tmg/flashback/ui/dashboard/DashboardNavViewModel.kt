package tmg.flashback.ui.dashboard

import android.os.Bundle
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmg.flashback.crash_reporting.manager.CrashManager
import tmg.flashback.debug.DebugNavigationComponent
import tmg.flashback.debug.model.DebugMenuItem
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.navigation.Navigator
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.results.Calendar
import tmg.flashback.results.Constructors
import tmg.flashback.results.Drivers
import tmg.flashback.results.usecases.DefaultSeasonUseCase
import tmg.flashback.results.with
import tmg.flashback.ui.components.navigation.NavigationTimelineItem
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.navigation.Screen
import tmg.flashback.rss.contract.RSS
import tmg.flashback.search.contract.Search
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

    override val currentlySelectedItem: LiveData<MenuItem> = currentDestination
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
        .asLiveData(viewModelScope.coroutineContext)

    override val showMenu: LiveData<Boolean> = currentDestination
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
        .asLiveData(viewModelScope.coroutineContext)

    override val showBottomBar: LiveData<Boolean> = currentDestination
        .map {
            if (it == null) return@map false
            return@map it.startsWith("results/")
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

    fun bindNavController() {
        navigator.navController?.addOnDestinationChangedListener(this)
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