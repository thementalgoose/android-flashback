package tmg.flashback.ui.dashboard2.compact

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.configuration.usecases.FetchConfigUseCase
import tmg.flashback.releasenotes.usecases.NewReleaseNotesUseCase
import tmg.flashback.BuildConfig
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.stats.usecases.ScheduleNotificationsUseCase
import tmg.flashback.usecases.DashboardSyncUseCase
import tmg.flashback.widgets.updateAllWidgets
import tmg.utilities.extensions.then
import tmg.utilities.lifecycle.Event
import javax.inject.Inject

//region Inputs

interface DashboardViewModelInputs {
    fun clickTab(tab: DashboardNavItem)
    fun clickSeason(season: Int)
}

//endregion

//region Outputs

interface DashboardViewModelOutputs {
    val initialTab: DashboardScreenState
    val currentTab: LiveData<DashboardScreenState>

    val openReleaseNotes: LiveData<Event>

    val appConfigSynced: LiveData<Event>
}

//endregion

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val raceRepository: RaceRepository,
    private val overviewRepository: OverviewRepository,
    private val releaseNotesUseCase: NewReleaseNotesUseCase,
    private val analyticsManager: AnalyticsManager,
    private val dashboardSyncUseCase: DashboardSyncUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    val inputs: DashboardViewModelInputs = this
    val outputs: DashboardViewModelOutputs = this

    private val defaultTab: DashboardNavItem = DashboardNavItem.CALENDAR

    override val openReleaseNotes: MutableLiveData<Event> = MutableLiveData()
    override val appConfigSynced: MutableLiveData<Event> = MutableLiveData()

    override val initialTab: DashboardScreenState = DashboardScreenState(
        tab = defaultTab,
        season = defaultSeasonUseCase.defaultSeason
    )
    private val _currentTab: MutableStateFlow<DashboardScreenState> = MutableStateFlow(initialTab)
    override val currentTab: LiveData<DashboardScreenState> = _currentTab
        .then {
            analyticsManager.viewScreen("Dashboard", mapOf(
                "season" to it.season.toString(),
                "tab" to it.tab.analyticsName
            ))
        }
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

    override fun clickSeason(season: Int) {
        _currentTab.value = DashboardScreenState(
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

    override fun clickTab(tab: DashboardNavItem) {
        _currentTab.value = DashboardScreenState(
            tab = tab,
            season = currentTab.value?.season ?: defaultSeasonUseCase.defaultSeason
        )
    }

}

