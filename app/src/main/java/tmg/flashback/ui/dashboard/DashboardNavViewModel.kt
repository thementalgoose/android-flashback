package tmg.flashback.ui.dashboard

import android.os.Bundle
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
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.debug.DebugNavigationComponent
import tmg.flashback.debug.model.DebugMenuItem
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
import tmg.flashback.ui.settings.All
import tmg.flashback.usecases.DashboardSyncUseCase
import tmg.flashback.usecases.GetSeasonsUseCase
import javax.inject.Inject

interface DashboardNavViewModelInputs {
    fun clickItem(navigationItem: MenuItem)
    fun clickDebug(debugMenuItem: DebugMenuItem)
}

interface DashboardNavViewModelOutputs {
    val currentlySelectedItem: StateFlow<MenuItem>
    val debugMenuItems: StateFlow<List<DebugMenuItem>>
    val appFeatureItemsList: StateFlow<List<MenuItem>>
    val seasonScreenItemsList: StateFlow<List<MenuItem>>

    val showBottomBar: StateFlow<Boolean>
    val showMenu: StateFlow<Boolean>
}

@HiltViewModel
class DashboardNavViewModel @Inject constructor(
    private val rssRepository: RssRepository,
    private val navigator: Navigator,
    private val applicationNavigationComponent: ApplicationNavigationComponent,
    private val crashlyticsManager: CrashlyticsManager,
    private val dashboardSyncUseCase: DashboardSyncUseCase,
    private val debugNavigationComponent: DebugNavigationComponent,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), DashboardNavViewModelInputs, DashboardNavViewModelOutputs,
    NavController.OnDestinationChangedListener {

    val inputs: DashboardNavViewModelInputs = this
    val outputs: DashboardNavViewModelOutputs = this

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

    init {
        initialiseItems()

        viewModelScope.launch(ioDispatcher) {
            val result = dashboardSyncUseCase.sync()
            crashlyticsManager.log("Dashboard synchronisation complete $result")
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

    override fun clickItem(navigationItem: MenuItem) {
        when (navigationItem) {
            MenuItem.Calendar -> navigator.navigate(Screen.Calendar.with(null))
            MenuItem.Drivers -> navigator.navigate(Screen.Drivers.with(null))
            MenuItem.Constructors -> navigator.navigate(Screen.Constructors.with(null))
            MenuItem.Contact -> applicationNavigationComponent.aboutApp()
            MenuItem.RSS -> navigator.navigate(Screen.RSS)
            MenuItem.Search -> navigator.navigate(Screen.Search)
            MenuItem.Settings -> navigator.navigate(Screen.Settings.All)
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