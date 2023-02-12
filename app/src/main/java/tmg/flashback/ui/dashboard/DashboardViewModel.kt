package tmg.flashback.ui.dashboard

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.eastereggs.model.MenuIcons
import tmg.flashback.eastereggs.usecases.IsMenuIconEnabledUseCase
import tmg.flashback.eastereggs.usecases.IsSnowEnabledUseCase
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.rss.RSS
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.stats.Calendar
import tmg.flashback.stats.Constructors
import tmg.flashback.stats.Drivers
import tmg.flashback.stats.Search
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.stats.with
import tmg.flashback.ui.components.navigation.NavigationTimelineItem
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.ui.permissions.RationaleType
import tmg.flashback.ui.repository.PermissionRepository
import tmg.flashback.ui.settings.All
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import tmg.flashback.usecases.GetSeasonsUseCase
import javax.inject.Inject

interface DashboardViewModelInputs {
    fun clickItem(navigationItem: MenuItem)
    fun clickDarkMode(toState: Boolean)
    fun clickSeason(season: Int)
    fun clickFeaturePrompt(prompt: FeaturePrompt)
}

interface DashboardViewModelOutputs {
    val currentlySelectedItem: LiveData<MenuItem>
    val appFeatureItemsList: LiveData<List<MenuItem>>
    val seasonScreenItemsList: LiveData<List<MenuItem>>

    val showBottomBar: LiveData<Boolean>
    val showMenu: LiveData<Boolean>

    val isDarkMode: LiveData<Boolean>

    val featurePromptsList: LiveData<List<FeaturePrompt>>

    val seasonsItemsList: LiveData<List<NavigationTimelineItem>>
    val currentlySelectedSeason: LiveData<Int>

    val appVersion: LiveData<String>

    // Easter eggs
    val snow: LiveData<Boolean>
    val titleIcon: LiveData<MenuIcons?>
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val rssRepository: RSSRepository,
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val styleManager: StyleManager,
    private val changeNightModeUseCase: ChangeNightModeUseCase,
    private val buildConfigManager: BuildConfigManager,
    private val notificationRepository: NotificationRepository,
    private val permissionRepository: PermissionRepository,
    private val navigator: Navigator,
    private val statsNavigationComponent: StatsNavigationComponent,
    private val permissionManager: PermissionManager,
    private val getSeasonUseCase: GetSeasonsUseCase,
    private val applicationNavigationComponent: ApplicationNavigationComponent,
    isSnowEnabledUseCase: IsSnowEnabledUseCase,
    isMenuIconEnabledUseCase: IsMenuIconEnabledUseCase
): ViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    val inputs: DashboardViewModelInputs = this
    val outputs: DashboardViewModelOutputs = this

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

    override val currentlySelectedSeason: MutableLiveData<Int> = MutableLiveData(defaultSeasonUseCase.defaultSeason)

    override val isDarkMode: MutableLiveData<Boolean> = MutableLiveData()
    override val appVersion: MutableLiveData<String> = MutableLiveData(buildConfigManager.versionName)

    override val featurePromptsList: MutableLiveData<List<FeaturePrompt>> = MutableLiveData()

    override val seasonsItemsList: LiveData<List<NavigationTimelineItem>> = currentlySelectedSeason
        .map { season -> getSeasons(season) }

    override val snow: MutableLiveData<Boolean> = MutableLiveData(isSnowEnabledUseCase())
    override val titleIcon: LiveData<MenuIcons?> = MutableLiveData(isMenuIconEnabledUseCase())

    init {
        initialiseItems()
        initialiseFeatureList()
        initialiseDarkMode()

        // TODO: Make network request for season selection + season change here!
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

    private fun initialiseFeatureList() {
        val list = mutableListOf<FeaturePrompt>().apply {
            if (buildConfigManager.isRuntimeNotificationsSupported &&
                !notificationRepository.seenRuntimeNotifications &&
                !permissionRepository.isRuntimeNotificationsEnabled
            ) {
                add(FeaturePrompt.RuntimeNotifications)
            }
            if (!buildConfigManager.isRuntimeNotificationsSupported && !notificationRepository.seenNotificationOnboarding) {
                add(FeaturePrompt.Notifications)
            }
        }
        featurePromptsList.postValue(list)
    }

    private fun initialiseDarkMode() {
        isDarkMode.value = !styleManager.isDayMode
    }

    override fun clickDarkMode(toState: Boolean) {
        changeNightModeUseCase.setNightMode(when (toState) {
            true -> NightMode.NIGHT
            false -> NightMode.DAY
        })
        initialiseDarkMode()
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

    override fun clickFeaturePrompt(prompt: FeaturePrompt) {
        when (prompt) {
            FeaturePrompt.Notifications -> {
                statsNavigationComponent.featureNotificationOnboarding()
                notificationRepository.seenNotificationOnboarding = true
                initialiseFeatureList()
            }
            FeaturePrompt.RuntimeNotifications -> {
                viewModelScope.launch {
                    permissionManager
                        .requestPermission(RationaleType.RuntimeNotifications)
                        .invokeOnCompletion {
                            notificationRepository.seenRuntimeNotifications = true
                            if (permissionRepository.isRuntimeNotificationsEnabled) {
                                statsNavigationComponent.featureNotificationOnboarding()
                            }
                            initialiseFeatureList()
                        }
                }
            }
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
            else -> {}
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
}