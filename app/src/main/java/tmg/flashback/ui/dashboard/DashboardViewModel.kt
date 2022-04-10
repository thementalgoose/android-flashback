package tmg.flashback.ui.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tmg.flashback.common.controllers.ReleaseNotesController
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.configuration.usecases.FetchConfigUseCase
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.SeasonConstructorStandings
import tmg.flashback.formula1.model.SeasonDriverStandings
import tmg.flashback.statistics.BuildConfig
import tmg.flashback.statistics.controllers.HomeController
import tmg.flashback.statistics.extensions.updateAllWidgets
import tmg.flashback.statistics.usecases.DefaultSeasonUseCase
import tmg.flashback.statistics.workmanager.WorkerProvider
import tmg.flashback.stats.repository.HomeRepository
import tmg.utilities.lifecycle.Event

//region Inputs

interface DashboardViewModelInputs {
    fun clickTab(tab: DashboardNavItem)
    fun clickSeason(season: Int)

//    fun clickRace(overviewRace: OverviewRace)
//    fun clickDriver(seasonDriverStanding: SeasonDriverStandings)
//    fun clickConstructor(seasonConstructorStanding: SeasonConstructorStandings)
}

//endregion

//region Outputs

interface DashboardViewModelOutputs {
    val currentTab: LiveData<DashboardScreenState>

    val openReleaseNotes: LiveData<Event>
    val openSearch: LiveData<Event>

    val appConfigSynced: LiveData<Event>

    val defaultToSchedule: Boolean
}

//endregion

class DashboardViewModel(
    applicationContext: Context,
    private val workerProvider: WorkerProvider,
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val fetchConfigUseCase: FetchConfigUseCase,
    private val applyConfigUseCase: ApplyConfigUseCase,
    private val homeRepository: HomeRepository,
    private val releaseNotesController: ReleaseNotesController,
): ViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    val inputs: DashboardViewModelInputs = this
    val outputs: DashboardViewModelOutputs = this

    private val defaultTab: DashboardNavItem = DashboardNavItem.CALENDAR

    override val openReleaseNotes: MutableLiveData<Event> = MutableLiveData()
    override val appConfigSynced: MutableLiveData<Event> = MutableLiveData()
    override val openSearch: MutableLiveData<Event> = MutableLiveData()

    override val defaultToSchedule: Boolean
        get() = homeRepository.defaultToSchedule

    override val currentTab: MutableLiveData<DashboardScreenState> = MutableLiveData(DashboardScreenState(
        tab = defaultTab,
        season = defaultSeasonUseCase.defaultSeason
    ))

    init {
        viewModelScope.launch {
            fetchConfigUseCase.fetch()
            val activate = applyConfigUseCase.apply()
            if (BuildConfig.DEBUG) {
                Log.i("Dashboard", "Remote config change detected $activate")
            }
            if (activate) {
                appConfigSynced.value = Event()
                workerProvider.schedule()
                applicationContext.updateAllWidgets()
            }
        }

        if (releaseNotesController.pendingReleaseNotes) {
            openReleaseNotes.value = Event()
        }
    }

    override fun clickSeason(season: Int) {
        currentTab.postValue(DashboardScreenState(
            tab = currentTab.value?.tab ?: defaultTab,
            season = season
        ))
    }

    override fun clickTab(tab: DashboardNavItem) {
        currentTab.postValue(DashboardScreenState(
            tab = tab,
            season = currentTab.value?.season ?: defaultSeasonUseCase.defaultSeason
        ))
    }

}

