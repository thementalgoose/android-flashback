package tmg.flashback.season.presentation.dashboard.races

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.domain.repo.EventsRepository
import tmg.flashback.domain.repo.OverviewRepository
import tmg.flashback.domain.repo.usecases.FetchSeasonUseCase
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.formula1.enums.SeasonTyres
import tmg.flashback.formula1.enums.getBySeason
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.season.contract.ResultsNavigationComponent
import tmg.flashback.season.contract.repository.NotificationsRepository
import tmg.flashback.season.presentation.dashboard.races.RacesModelBuilder.generateScheduleModel
import tmg.flashback.season.presentation.dashboard.shared.seasonpicker.CurrentSeasonHolder
import tmg.flashback.season.repository.HomeRepository
import tmg.flashback.season.usecases.DefaultSeasonUseCase
import tmg.flashback.weekend.contract.Weekend
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.contract.model.ScreenWeekendNav
import tmg.flashback.weekend.contract.with
import javax.inject.Inject

interface RacesViewModelInputs {
    fun refresh()
    fun clickTyre()
    fun clickItem(model: RacesModel)
    fun back()
}

interface RacesViewModelOutputs {
    val uiState: StateFlow<RacesScreenState>
}

@HiltViewModel
class RacesViewModel @Inject constructor(
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val overviewRepository: OverviewRepository,
    private val currentSeasonHolder: CurrentSeasonHolder,
    private val notificationRepository: NotificationsRepository,
    private val homeRepository: HomeRepository,
    private val resultsNavigationComponent: ResultsNavigationComponent,
    private val eventsRepository: EventsRepository,
    private val navigator: Navigator,
    private val networkConnectivityManager: NetworkConnectivityManager,
    private val adsRepository: AdsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), RacesViewModelInputs, RacesViewModelOutputs {

    val inputs: RacesViewModelInputs = this
    val outputs: RacesViewModelOutputs = this

    override val uiState: MutableStateFlow<RacesScreenState> = MutableStateFlow(RacesScreenState(
        season = currentSeasonHolder.currentSeason,
        showAdvert = adsRepository.areAdvertsEnabled && adsRepository.advertConfig.onHomeScreen
    ))

    private var collapseRaces: Boolean = homeRepository.collapseList
    private var showEmptyWeeks: Boolean = homeRepository.emptyWeeksInSchedule

    init {
        viewModelScope.launch(ioDispatcher) {
            currentSeasonHolder.currentSeasonFlow.collectLatest {
                uiState.value = uiState.value.copy(season = it)
                if (!populate(it) || it == currentSeasonHolder.defaultSeason) {
                    refresh()
                }
            }
        }
    }

    override fun refresh() {
        viewModelScope.launch(ioDispatcher) {
            val season = uiState.value.season
            if (uiState.value.items.isNullOrEmpty() || uiState.value.items?.none { it is RacesModel.RaceWeek } == true) {
                populate(season)
            }
            uiState.value = uiState.value.copy(isLoading = true, networkAvailable = networkConnectivityManager.isConnected)
            fetchSeasonUseCase.fetchSeason(season)
            populate(season)
        }
    }

    private suspend fun populate(season: Int): Boolean {
        val overview = overviewRepository.getOverview(season).firstOrNull()
        val events = eventsRepository.getEvents(season).firstOrNull()

        if (overview == null || overview.overviewRaces.isEmpty()) {
            navigator.clearSubNavigation()
            uiState.value = uiState.value.copy(
                items = null,
                isLoading = false,
                networkAvailable = networkConnectivityManager.isConnected,
                currentRace = null
            )
            return false
        }

        val raceList = generateScheduleModel(
            overview = overview,
            events = events ?: emptyList(),
            notificationSchedule = notificationRepository.notificationSchedule,
            showCollapsePreviousRaces = collapseRaces,
            showEmptyWeeks = showEmptyWeeks,
        )

        uiState.value = uiState.value.copy(
            items = raceList,
            isLoading = false,
            networkAvailable = networkConnectivityManager.isConnected
        )
        return true
    }

    override fun clickTyre() {
        resultsNavigationComponent.tyres(uiState.value.season)
    }

    override fun back() {
        navigator.clearSubNavigation()
        uiState.value = uiState.value.copy(
            currentRace = null
        )
    }

    override fun clickItem(model: RacesModel) {
        when (model) {
            is RacesModel.EmptyWeek -> {}
            is RacesModel.RaceWeek -> {
                navigator.setSubNavigation()
                uiState.value = uiState.value.copy(
                    currentRace = model.model
                )
            }
            is RacesModel.GroupedCompletedRaces -> {
                collapseRaces = false
                viewModelScope.launch(ioDispatcher) { populate(uiState.value.season) }
            }
            is RacesModel.Event -> {}
            RacesModel.AllEvents -> {
                resultsNavigationComponent.preseasonEvents(uiState.value.season)
            }
        }
    }

    private fun RacesModel.RaceWeek.getScreenWeekendNav(): ScreenWeekendNav {
        return when {
            model.season > currentSeasonYear -> ScreenWeekendNav.SCHEDULE
            model.season < currentSeasonYear -> ScreenWeekendNav.RACE
            model.hasResults -> ScreenWeekendNav.RACE
            model.hasQualifying -> ScreenWeekendNav.QUALIFYING
            else -> ScreenWeekendNav.SCHEDULE
        }
    }
}

