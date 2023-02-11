package tmg.flashback.ui.dashboard

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tmg.flashback.R
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.eastereggs.model.MenuIcons
import tmg.flashback.eastereggs.usecases.IsMenuIconEnabledUseCase
import tmg.flashback.eastereggs.usecases.IsSnowEnabledUseCase
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.stats.Circuit
import tmg.flashback.stats.ConstructorSeason
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.ui.components.navigation.NavigationTimelineItem
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.ui.permissions.RationaleType
import tmg.flashback.ui.repository.PermissionRepository
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import tmg.flashback.usecases.GetSeasonsUseCase
import javax.inject.Inject

interface DashboardViewModelInputs {
    fun clickItem(navigationItem: MenuItem)
    fun clickDarkMode(toState: Boolean)
    fun clickSeason(season: Int)
    fun clickFeaturePrompt(prompt: FeaturePrompt)
    fun routeUpdated(route: String)
}

interface DashboardViewModelOutputs {
    val currentlySelectedItem: LiveData<MenuItem>
    val appFeatureItemsList: LiveData<List<MenuItem>>
    val seasonScreenItemsList: LiveData<List<MenuItem>>
    val hideBottomBar: LiveData<Boolean>

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
    private val statsNavigationComponent: StatsNavigationComponent,
    private val permissionManager: PermissionManager,
    private val getSeasonUseCase: GetSeasonsUseCase,
    private val applicationNavigationComponent: ApplicationNavigationComponent,
    isSnowEnabledUseCase: IsSnowEnabledUseCase,
    isMenuIconEnabledUseCase: IsMenuIconEnabledUseCase
): ViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    val inputs: DashboardViewModelInputs = this
    val outputs: DashboardViewModelOutputs = this

    override val currentlySelectedItem: MutableLiveData<MenuItem> = MutableLiveData(MenuItem.Calendar)

    override val appFeatureItemsList: MutableLiveData<List<MenuItem>> = MutableLiveData()
    override val seasonScreenItemsList: MutableLiveData<List<MenuItem>> = MutableLiveData()
    override val hideBottomBar: LiveData<Boolean> = currentlySelectedItem
        .map {
            when (it) {
                MenuItem.Calendar,
                MenuItem.Constructors,
                MenuItem.Drivers -> true
                else -> false
            }
        }

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

    override fun routeUpdated(route: String) {
        when (route) {
            Screen.Circuit.route -> {
                currentlySelectedItem.value = MenuItem.Drivers
            }
        }
    }

    override fun clickItem(navigationItem: MenuItem) {
        currentlySelectedItem.postValue(navigationItem)

        when (navigationItem) {
//            MenuItem.Calendar -> statsNavigationComponent.schedule
//            MenuItem.Drivers -> TODO()
//            MenuItem.Constructors -> TODO()
            MenuItem.Contact -> applicationNavigationComponent.aboutApp()
//            MenuItem.RSS -> rssNavigationComponent.
            MenuItem.Search -> statsNavigationComponent.search()
            MenuItem.Settings -> applicationNavigationComponent.settings()
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