package tmg.flashback.stats.ui.dashboard.schedule

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.statistics.repo.EventsRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.stats.usecases.FetchSeasonUseCase
import tmg.utilities.extensions.startOfWeek
import javax.inject.Inject

interface ScheduleViewModelInputs {
    fun refresh()
    fun load(season: Int)

    fun clickTyre(season: Int)
    fun clickItem(model: ScheduleModel)
}

interface ScheduleViewModelOutputs {
    val items: LiveData<List<ScheduleModel>?>
    val isRefreshing: LiveData<Boolean>
}

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val overviewRepository: OverviewRepository,
    private val notificationRepository: NotificationRepository,
    private val statsNavigationComponent: StatsNavigationComponent,
    private val eventsRepository: EventsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), ScheduleViewModelInputs, ScheduleViewModelOutputs {

    val inputs: ScheduleViewModelInputs = this
    val outputs: ScheduleViewModelOutputs = this

    override val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(false)

    private val calendar: MutableLiveData<Boolean> = MutableLiveData(false)

    private val season: MutableStateFlow<Int?> = MutableStateFlow(null)
    override val items: LiveData<List<ScheduleModel>?> = season
        .filterNotNull()
        .flatMapLatest { season ->
            isRefreshing.postValue(true)
            fetchSeasonUseCase
                .fetch(season)
                .flatMapLatest { hasMadeRequest ->
                    combine(
                        overviewRepository.getOverview(season),
                        eventsRepository.getEvents(season)
                    ) { overview, events ->
                        isRefreshing.postValue(false)
                        if (!hasMadeRequest) {
                            return@combine listOf(ScheduleModel.Loading)
                        }

                        val upcoming = overview.overviewRaces.getLatestUpcoming()
                        val upcomingEvents = events.filter { it.date > LocalDate.now() }

                        return@combine schedule(overview, upcomingEvents, upcoming)

                    }
                }
        }
        .flowOn(ioDispatcher)
        .asLiveData(viewModelScope.coroutineContext)

    private fun List<OverviewRace>.getLatestUpcoming(): OverviewRace? {
        return this
            .sortedBy { it.date }
            .firstOrNull { it.date >= LocalDate.now() }
    }

    override fun load(season: Int) {
        this.season.value = season
    }

    override fun refresh() {
        val season = season.value ?: return

        isRefreshing.postValue(true)
        viewModelScope.launch(ioDispatcher) {
            fetchSeasonUseCase.fetchSeason(season)
            isRefreshing.postValue(false)
        }
    }

    override fun clickTyre(season: Int) {
        statsNavigationComponent.tyres(season)
    }

    override fun clickItem(model: ScheduleModel) {
        when (model) {
            is ScheduleModel.List -> statsNavigationComponent.weekend(WeekendInfo(
                season = model.model.season,
                round = model.model.round,
                raceName = model.model.raceName,
                circuitId = model.model.circuitId,
                circuitName = model.model.circuitName,
                country = model.model.country,
                countryISO = model.model.countryISO,
                date = model.model.date,
            ))
            is ScheduleModel.Event -> {}
            ScheduleModel.Loading -> {}
        }
    }

    private fun schedule(
        overview: Overview,
        upcomingEvents: List<Event>,
        upcoming: OverviewRace?
    ): List<ScheduleModel> {
        return mutableListOf<ScheduleModel>()
            .apply {
                addAll(overview.overviewRaces.map {
                    ScheduleModel.List(
                        model = it,
                        notificationSchedule = notificationRepository.notificationSchedule,
                        showScheduleList = it == upcoming
                    )
                })
                addAll(upcomingEvents.map {
                    ScheduleModel.Event(it)
                })
            }
            .sortedBy {
                when (it) {
                    is ScheduleModel.Event -> it.date
                    is ScheduleModel.List -> it.date
                    else -> null
                }
            }
    }
}

