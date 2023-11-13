package tmg.flashback.presentation.dashboard

import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
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
import tmg.flashback.rss.contract.RSS
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.search.contract.Search
import tmg.flashback.season.contract.ConstructorsStandings
import tmg.flashback.season.contract.DriverStandings
import tmg.flashback.season.contract.Races
import tmg.flashback.presentation.settings.All
import tmg.flashback.usecases.DashboardSyncUseCase
import tmg.utilities.extensions.combinePair
import javax.inject.Inject

interface DashboardNavViewModelInputs {
    fun clickItem(navigationItem: MenuItem)
    fun clickDebug(debugMenuItem: DebugMenuItem)
    fun navigationInRoot(destination: String, inRoot: Boolean)
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
    private val currentDestinationInRoot: MutableStateFlow<Map<String, Boolean>> = MutableStateFlow(mapOf())

    override val currentlySelectedItem: StateFlow<MenuItem> = currentDestination
        .combinePair(currentDestinationInRoot)
        .distinctUntilChanged()
        .map { (destination, inRoot) ->
            if (destination == null) return@map null
            val item: MenuItem? = when {
                destination == Screen.Races.route -> MenuItem.Calendar
                destination == Screen.DriverStandings.route -> MenuItem.Drivers
                destination == Screen.ConstructorsStandings.route -> MenuItem.Constructors
                destination.startsWith("settings") -> MenuItem.Settings
                destination.startsWith("rss") -> MenuItem.RSS
                destination.startsWith("search") -> MenuItem.Search
                destination == Screen.AboutThisApp.route -> MenuItem.Contact
                else -> null
            }
            return@map item
        }
        .filterNotNull()
        .stateIn(viewModelScope, SharingStarted.Lazily, MenuItem.Calendar)

    override val showMenu: StateFlow<Boolean> = currentDestination
        .combinePair(currentDestinationInRoot)
        .distinctUntilChanged()
        .map { (destination, destinationInRoot) ->
            if (destination == null) return@map null
            Log.i("Nav", "showMenu $destination")
            return@map when {
                destination.startsWith("results/") -> destinationInRoot.includes(destination)
                destination == "settings" -> destinationInRoot.includes(destination)
                destination == "rss" -> destinationInRoot.includes(destination)
                destination == "search" -> destinationInRoot.includes(destination)
                else -> false
            }
        }
        .filterNotNull()
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    override val showBottomBar: StateFlow<Boolean> = currentDestination
        .combinePair(currentDestinationInRoot)
        .distinctUntilChanged()
        .map { (destination, destinationInRoot) ->
            Log.i("Nav", "showBottomBar $destination")
            return@map when (destination) {
                Screen.Races.route -> destinationInRoot.includes(destination)
                Screen.ConstructorsStandings.route -> destinationInRoot.includes(destination)
                Screen.DriverStandings.route -> destinationInRoot.includes(destination)
                null -> false
                else -> false
            }
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
            MenuItem.Calendar -> navigator.navigate(Screen.Races)
            MenuItem.Drivers -> navigator.navigate(Screen.DriverStandings)
            MenuItem.Constructors -> navigator.navigate(Screen.ConstructorsStandings)
            MenuItem.Contact -> applicationNavigationComponent.aboutApp()
            MenuItem.RSS -> navigator.navigate(Screen.RSS)
            MenuItem.Search -> navigator.navigate(Screen.Search)
            MenuItem.Settings -> navigator.navigate(Screen.Settings.All)
        }
    }

    override fun clickDebug(debugMenuItem: DebugMenuItem) {
        debugNavigationComponent.navigateTo(debugMenuItem.id)
    }

    private fun Map<String, Boolean>.includes(destination: String) =
        this[destination] ?: true

    override fun navigationInRoot(destination: String, inRoot: Boolean) {
        Log.i("DashboardNav", "navigationInRoot => $destination [$inRoot]")
        val set = currentDestinationInRoot.value.toMutableMap()
        set[destination] = inRoot
        currentDestinationInRoot.value = set.toMap()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        Log.i("DashboardNav", "Destination changed => ${destination.route}")
        currentDestination.value = destination.route
    }
}