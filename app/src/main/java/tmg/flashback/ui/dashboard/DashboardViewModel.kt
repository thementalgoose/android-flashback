package tmg.flashback.ui.dashboard

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.ui.components.navigation.NavigationTimelineItem
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.permissions.RationaleType
import tmg.flashback.ui.repository.PermissionRepository
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import javax.inject.Inject

interface DashboardViewModelInputs {
    fun clickItem(navigationItem: MenuItem)
    fun clickDarkMode(toState: Boolean)
    fun clickSeason(season: Int)
    fun clickFeaturePrompt(prompt: DashboardFeaturePrompt)
}

interface DashboardViewModelOutputs {
    val selectedItem: LiveData<MenuItem>
    val drawerItems: LiveData<List<MenuItem>>
    val bottomBarItems: LiveData<List<MenuItem>>
    val expandedItems: LiveData<List<MenuItem>>
    val hideBottomBar: LiveData<Boolean>

    val isDarkMode: LiveData<Boolean>
    val featurePrompts: LiveData<List<DashboardFeaturePrompt>>

    val selectedSeason: LiveData<Int>
    val seasons: LiveData<List<NavigationTimelineItem>>
    val appVersion: LiveData<String>
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val rssRepository: RSSRepository,
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val styleManager: StyleManager,
    private val changeNightModeUseCase: ChangeNightModeUseCase,
    private val buildConfigManager: BuildConfigManager,
    private val notificationRepository: NotificationRepository,
    private val permissionRepository: PermissionRepository,
    private val statsNavigationComponent: StatsNavigationComponent,
    private val permissionManager: PermissionManager,
): ViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    val inputs: DashboardViewModelInputs = this
    val outputs: DashboardViewModelOutputs = this

    override val selectedItem: MutableLiveData<MenuItem> = MutableLiveData(MenuItem.Calendar)

    override val drawerItems: MutableLiveData<List<MenuItem>> = MutableLiveData()
    override val bottomBarItems: MutableLiveData<List<MenuItem>> = MutableLiveData()
    override val expandedItems: MutableLiveData<List<MenuItem>> = MutableLiveData()
    override val hideBottomBar: LiveData<Boolean> = selectedItem
        .map {
            when (it) {
                MenuItem.Calendar,
                MenuItem.Constructors,
                MenuItem.Drivers -> true
                else -> false
            }
        }

    override val selectedSeason: MutableLiveData<Int> = MutableLiveData(defaultSeasonUseCase.defaultSeason)

    override val isDarkMode: MutableLiveData<Boolean> = MutableLiveData(styleManager.isDayMode)
    override val appVersion: MutableLiveData<String> = MutableLiveData(buildConfigManager.versionName)

    override val featurePrompts: MutableLiveData<List<DashboardFeaturePrompt>> = MutableLiveData()

    override val seasons: LiveData<List<NavigationTimelineItem>> = selectedSeason
        .map { season -> getSeasons(season) }

    init {
        initializeItems()
        initialiseFeatureList()
    }

    private fun initializeItems() {
        val bottom = listOf(MenuItem.Calendar, MenuItem.Drivers, MenuItem.Constructors)
        val side = mutableListOf<MenuItem>().apply {
            if (homeRepository.searchEnabled) {
                add(MenuItem.Search)
            }
            if (rssRepository.enabled) {
                add(MenuItem.RSS)
            }
            add(MenuItem.Settings)
            add(MenuItem.Contact)
        }
        bottomBarItems.postValue(bottom)
        drawerItems.postValue(side)
        expandedItems.postValue(bottom + side)
    }

    private fun initialiseFeatureList() {
        val list = mutableListOf<DashboardFeaturePrompt>().apply {
            if (buildConfigManager.isRuntimeNotificationsSupported &&
                !notificationRepository.seenRuntimeNotifications &&
                !permissionRepository.isRuntimeNotificationsEnabled
            ) {
                add(DashboardFeaturePrompt.RuntimeNotifications)
            }
            if (!buildConfigManager.isRuntimeNotificationsSupported && !notificationRepository.seenNotificationOnboarding) {
                add(DashboardFeaturePrompt.Notifications)
            }
        }
        featurePrompts.postValue(list)
    }

    override fun clickDarkMode(toState: Boolean) {
        changeNightModeUseCase.setNightMode(when (toState) {
            true -> NightMode.NIGHT
            false -> NightMode.DAY
        })
    }

    override fun clickSeason(season: Int) {
        selectedSeason.value = season
    }

    override fun clickFeaturePrompt(prompt: DashboardFeaturePrompt) {
        when (prompt) {
            DashboardFeaturePrompt.Notifications -> {
                statsNavigationComponent.featureNotificationOnboarding()
                notificationRepository.seenNotificationOnboarding = true
                initialiseFeatureList()
            }
            DashboardFeaturePrompt.RuntimeNotifications -> {
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
        selectedItem.postValue(navigationItem)
    }

    private fun getSeasons(selectedSeason: Int? = null): List<NavigationTimelineItem> {
        val seasons = homeRepository
            .supportedSeasons
            .sortedDescending()

        return seasons
            .mapIndexed { index, it ->
                NavigationTimelineItem(
                    color = Formula1.decadeColours["${it.toString().substring(0, 3)}0"] ?: Color.Gray,
                    label = it.toString(),
                    id = it.toString(),
                    isSelected = selectedSeason == it,
                    pipeType = PipeType.START_END
                )
            }
    }
}

sealed class DashboardFeaturePrompt {
    object RuntimeNotifications: DashboardFeaturePrompt()
    object Notifications: DashboardFeaturePrompt()
}