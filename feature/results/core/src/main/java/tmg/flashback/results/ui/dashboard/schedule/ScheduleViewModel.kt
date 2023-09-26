package tmg.flashback.results.ui.dashboard.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import tmg.flashback.domain.repo.EventsRepository
import tmg.flashback.domain.repo.OverviewRepository
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.results.contract.ResultsNavigationComponent
import tmg.flashback.results.contract.repository.NotificationsRepository
import tmg.flashback.results.repository.HomeRepository
import tmg.flashback.results.ui.dashboard.schedule.ScheduleModelBuilder.generateScheduleModel
import tmg.flashback.results.usecases.FetchSeasonUseCase
import tmg.flashback.weekend.contract.Weekend
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.contract.model.ScreenWeekendNav
import tmg.flashback.weekend.contract.with
import javax.inject.Inject

interface ScheduleViewModelInputs {
    fun refresh()
    fun load(season: Int)

    fun clickTyre(season: Int)
    fun clickItem(model: ScheduleModel)
}

interface ScheduleViewModelOutputs {
    val items: StateFlow<List<ScheduleModel>?>
    val isRefreshing: StateFlow<Boolean>
}

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val overviewRepository: OverviewRepository,
    private val notificationRepository: NotificationsRepository,
    private val homeRepository: HomeRepository,
    private val navigator: Navigator,
    private val resultsNavigationComponent: ResultsNavigationComponent,
    private val eventsRepository: EventsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), ScheduleViewModelInputs, ScheduleViewModelOutputs {

    val inputs: ScheduleViewModelInputs = this
    val outputs: ScheduleViewModelOutputs = this

    override val isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var showCollapsablePlaceholder: MutableStateFlow<Boolean> = MutableStateFlow(homeRepository.collapseList)

    private val season: MutableStateFlow<Int?> = MutableStateFlow(null)
    override val items: StateFlow<List<ScheduleModel>?> = season
        .filterNotNull()
        .flatMapLatest { season ->
            isRefreshing.value = true
            fetchSeasonUseCase
                .fetch(season)
                .flatMapLatest { hasMadeRequest ->
                    combine(
                        overviewRepository.getOverview(season),
                        eventsRepository.getEvents(season),
                        showCollapsablePlaceholder
                    ) { overview, events, showCollapsible ->
                        isRefreshing.value = false
                        if (!hasMadeRequest) {
                            return@combine listOf(ScheduleModel.Loading)
                        }

                        return@combine generateScheduleModel(
                            overview = overview,
                            events = events,
                            showCollapsePreviousRaces = showCollapsible,
                            notificationSchedule = notificationRepository.notificationSchedule,
                            showEmptyWeeks = homeRepository.emptyWeeksInSchedule
                        )
                    }
                }
        }
        .flowOn(ioDispatcher)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    override fun load(season: Int) {
        if (this.season.value != season) {
            this.showCollapsablePlaceholder.value = homeRepository.collapseList
        }
        this.season.value = season
    }

    override fun refresh() {
        val season = season.value ?: return

        isRefreshing.value = true
        viewModelScope.launch(ioDispatcher) {
            fetchSeasonUseCase.fetchSeason(season)
            isRefreshing.value = false
        }
    }

    override fun clickTyre(season: Int) {
        resultsNavigationComponent.tyres(season)
    }

    override fun clickItem(model: ScheduleModel) {
        when (model) {
            is ScheduleModel.EmptyWeek -> {}
            is ScheduleModel.RaceWeek -> navigator.navigate(
                Screen.Weekend.with(
                    weekendInfo = ScreenWeekendData(
                        season = model.model.season,
                        round = model.model.round,
                        raceName = model.model.raceName,
                        circuitId = model.model.circuitId,
                        circuitName = model.model.circuitName,
                        country = model.model.country,
                        countryISO = model.model.countryISO,
                        date = model.model.date,
                    ),
                    tab = model.getScreenWeekendNav()
                )
            )
            is ScheduleModel.GroupedCompletedRaces -> {
                showCollapsablePlaceholder.value = false
            }
            is ScheduleModel.Event -> {}
            ScheduleModel.Loading -> {}
            ScheduleModel.AllEvents -> {
                season.value?.let {
                    resultsNavigationComponent.preseasonEvents(it)
                }
            }
        }
    }

    private fun ScheduleModel.RaceWeek.getScreenWeekendNav(): ScreenWeekendNav {
        return when {
            model.season > currentSeasonYear -> ScreenWeekendNav.SCHEDULE
            model.season < currentSeasonYear -> ScreenWeekendNav.RACE
            model.hasResults -> ScreenWeekendNav.RACE
            model.hasQualifying -> ScreenWeekendNav.QUALIFYING
            else -> ScreenWeekendNav.SCHEDULE
        }
    }
}

