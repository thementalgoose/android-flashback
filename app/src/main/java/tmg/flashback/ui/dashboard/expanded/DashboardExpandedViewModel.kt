package tmg.flashback.ui.dashboard.expanded

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.releasenotes.usecases.NewReleaseNotesUseCase
import tmg.flashback.rss.RssNavigationComponent
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.ui.dashboard.MenuSeasonItem
import tmg.flashback.ui.dashboard.compact.DashboardNavItem
import tmg.flashback.ui.dashboard.compact.DashboardScreenState
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.usecases.DashboardSyncUseCase
import tmg.flashback.usecases.GetSeasonsUseCase
import tmg.utilities.extensions.then
import tmg.utilities.lifecycle.Event
import javax.inject.Inject

interface DashboardExpandedViewModelInputs {
    fun clickNavItem(navItem: DashboardExpandedNavItem)
    fun clickSeason(season: Int)
}

interface DashboardExpandedViewModelOutputs {
    val initialTab: DashboardExpandedScreenState
    val currentTab: LiveData<DashboardExpandedScreenState>

    val seasons: LiveData<List<MenuSeasonItem>>

    val openReleaseNotes: LiveData<Event>
    val appConfigSynced: LiveData<Event>
}

@HiltViewModel
class DashboardExpandedViewModel @Inject constructor(
    private val dashboardSyncUseCase: DashboardSyncUseCase,
    private val getSeasonsUseCase: GetSeasonsUseCase,
    private val releaseNotesUseCase: NewReleaseNotesUseCase,
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val analyticsManager: AnalyticsManager,
    private val navigationComponent: ApplicationNavigationComponent,
    private val rssNavigationComponent: RssNavigationComponent,
    private val statsNavigationComponent: StatsNavigationComponent,
    private val raceRepository: RaceRepository,
    private val overviewRepository: OverviewRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), DashboardExpandedViewModelInputs, DashboardExpandedViewModelOutputs {

    val inputs: DashboardExpandedViewModelInputs = this
    val outputs: DashboardExpandedViewModelOutputs = this

    private val defaultTab: DashboardExpandedNavItem = DashboardExpandedNavItem.CALENDAR


    override val openReleaseNotes: MutableLiveData<Event> = MutableLiveData()
    override val appConfigSynced: MutableLiveData<Event> = MutableLiveData()

    override val initialTab: DashboardExpandedScreenState = DashboardExpandedScreenState(
        tab = defaultTab,
        season = defaultSeasonUseCase.defaultSeason
    )
    private val _currentTab: MutableStateFlow<DashboardExpandedScreenState> = MutableStateFlow(initialTab)
    override val currentTab: LiveData<DashboardExpandedScreenState> = _currentTab
        .then {
            analyticsManager.viewScreen("Dashboard", mapOf(
                "season" to it.season.toString(),
                "tab" to it.tab.analyticsName
            ))
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val seasons: LiveData<List<MenuSeasonItem>> = _currentTab
        .map { it.season?.let { season -> getSeasonsUseCase.get(selectedSeason = season) } ?: emptyList() }
        .asLiveData(viewModelScope.coroutineContext)
    init {
        viewModelScope.launch(ioDispatcher) {
            val result = dashboardSyncUseCase.sync()
            if (result) {
                appConfigSynced.postValue(Event())
            }
        }
        if (releaseNotesUseCase.getNotes().isNotEmpty()) {
            openReleaseNotes.postValue(Event())
        }
    }

    override fun clickNavItem(navItem: DashboardExpandedNavItem) {
        when (navItem) {
            DashboardExpandedNavItem.CALENDAR,
            DashboardExpandedNavItem.DRIVERS,
            DashboardExpandedNavItem.CONSTRUCTORS -> {
                _currentTab.value = DashboardExpandedScreenState(
                    tab = navItem,
                    season = currentTab.value?.season ?: defaultSeasonUseCase.defaultSeason
                )
            }
            DashboardExpandedNavItem.SEARCH -> {
                statsNavigationComponent.search()
            }
            DashboardExpandedNavItem.RSS -> {
                rssNavigationComponent.rss()
            }
            DashboardExpandedNavItem.SETTINGS -> {
                navigationComponent.settings()
            }
        }
    }

    override fun clickSeason(season: Int) {
        _currentTab.value = DashboardExpandedScreenState(
            tab = currentTab.value?.tab ?: defaultTab,
            season = season
        )

        if (season == defaultSeasonUseCase.defaultSeason) {
            viewModelScope.launch(ioDispatcher) {
                raceRepository.fetchRaces(season)
            }
            viewModelScope.launch(ioDispatcher) {
                overviewRepository.fetchOverview(season)
            }
        }
    }
}