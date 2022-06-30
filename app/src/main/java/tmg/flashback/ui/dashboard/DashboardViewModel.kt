package tmg.flashback.ui.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.configuration.usecases.FetchConfigUseCase
import tmg.flashback.releasenotes.usecases.NewReleaseNotesUseCase
import tmg.flashback.statistics.BuildConfig
import tmg.flashback.statistics.extensions.updateAllWidgets
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.statistics.workmanager.WorkerProvider
import tmg.utilities.extensions.then
import tmg.utilities.lifecycle.Event

//region Inputs

interface DashboardViewModelInputs {
    fun clickTab(tab: DashboardNavItem)
    fun clickSeason(season: Int)
}

//endregion

//region Outputs

interface DashboardViewModelOutputs {
    val currentTab: LiveData<DashboardScreenState>

    val openReleaseNotes: LiveData<Event>

    val appConfigSynced: LiveData<Event>
}

//endregion

class DashboardViewModel(
    applicationContext: Context,
    private val workerProvider: WorkerProvider,
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val fetchConfigUseCase: FetchConfigUseCase,
    private val applyConfigUseCase: ApplyConfigUseCase,
    private val raceRepository: RaceRepository,
    private val overviewRepository: OverviewRepository,
    private val releaseNotesUseCase: NewReleaseNotesUseCase,
    private val analyticsManager: AnalyticsManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    val inputs: DashboardViewModelInputs = this
    val outputs: DashboardViewModelOutputs = this

    private val defaultTab: DashboardNavItem = DashboardNavItem.CALENDAR

    override val openReleaseNotes: MutableLiveData<Event> = MutableLiveData()
    override val appConfigSynced: MutableLiveData<Event> = MutableLiveData()

    private val _currentTab: MutableStateFlow<DashboardScreenState> = MutableStateFlow(DashboardScreenState(
        tab = defaultTab,
        season = defaultSeasonUseCase.defaultSeason
    ))
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
            fetchConfigUseCase.fetch()
            val activate = applyConfigUseCase.apply()
            if (BuildConfig.DEBUG) {
                Log.i("Dashboard", "Remote config change detected $activate")
            }
            if (activate) {
                appConfigSynced.postValue(Event())
                workerProvider.schedule()
                applicationContext.updateAllWidgets()
            }
        }

        viewModelScope.launch(ioDispatcher) {
            raceRepository.fetchRaces(defaultSeasonUseCase.defaultSeason)
        }
        viewModelScope.launch(ioDispatcher) {
            overviewRepository.fetchOverview(defaultSeasonUseCase.defaultSeason)
        }

        if (releaseNotesUseCase.getNotes().isNotEmpty()) {
            openReleaseNotes.value = Event()
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

