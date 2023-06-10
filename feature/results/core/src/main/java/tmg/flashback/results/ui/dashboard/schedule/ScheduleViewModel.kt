package tmg.flashback.results.ui.dashboard.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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
import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.domain.repo.EventsRepository
import tmg.flashback.domain.repo.OverviewRepository
import tmg.flashback.results.repository.HomeRepository
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.usecases.FetchSeasonUseCase
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.results.contract.ResultsNavigationComponent
import tmg.flashback.results.ui.dashboard.schedule.ScheduleModelBuilder.generateScheduleModel
import tmg.flashback.weekend.contract.Weekend
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.contract.with
import javax.inject.Inject
import kotlin.collections.List
import kotlin.collections.any
import kotlin.collections.filter
import kotlin.collections.firstOrNull
import kotlin.collections.listOf
import kotlin.collections.map
import kotlin.collections.minByOrNull
import kotlin.collections.mutableListOf
import kotlin.collections.sortedBy

interface ScheduleViewModelInputs {
    fun refresh()
    fun load(season: Int)

    fun clickTyre(season: Int)
    fun clickPreseason(season: Int)
    fun clickItem(model: ScheduleModel)
}

interface ScheduleViewModelOutputs {
    val items: StateFlow<List<ScheduleModel>?>
    val isRefreshing: StateFlow<Boolean>
    val showEvents: StateFlow<Boolean>
}

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val overviewRepository: OverviewRepository,
    private val notificationRepository: NotificationsRepositoryImpl,
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
    override val showEvents: MutableStateFlow<Boolean> = MutableStateFlow(false)

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

                        showEvents.value = events.any()

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

    private fun List<OverviewRace>.getLatestUpcoming(): OverviewRace? {
        return this
            .sortedBy { it.date }
            .firstOrNull { it.date >= LocalDate.now() }
    }

    override fun load(season: Int) {
        if (this.season.value != season) {
            this.showCollapsablePlaceholder.value = homeRepository.collapseList
        }
        this.season.value = season
    }

    override fun refresh() {
        val season = season.value ?: return

        showEvents.value = false
        isRefreshing.value = true
        viewModelScope.launch(ioDispatcher) {
            fetchSeasonUseCase.fetchSeason(season)
            isRefreshing.value = false
        }
    }

    override fun clickTyre(season: Int) {
        resultsNavigationComponent.tyres(season)
    }

    override fun clickPreseason(season: Int) {
        resultsNavigationComponent.preseasonEvents(season)
    }

    override fun clickItem(model: ScheduleModel) {
        when (model) {
            is ScheduleModel.EmptyWeek -> {}
            is ScheduleModel.RaceWeek -> navigator.navigate(
                Screen.Weekend.with(
                ScreenWeekendData(
                    season = model.model.season,
                    round = model.model.round,
                    raceName = model.model.raceName,
                    circuitId = model.model.circuitId,
                    circuitName = model.model.circuitName,
                    country = model.model.country,
                    countryISO = model.model.countryISO,
                    date = model.model.date,
                )
            ))
            is ScheduleModel.GroupedCompletedRaces -> {
                showCollapsablePlaceholder.value = false
            }
            is ScheduleModel.Event -> {}
            ScheduleModel.Loading -> {}
        }
    }
}

