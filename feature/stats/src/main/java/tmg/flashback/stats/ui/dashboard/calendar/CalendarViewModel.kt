package tmg.flashback.stats.ui.dashboard.calendar

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.CalendarMonth
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.temporal.TemporalField
import org.threeten.bp.temporal.WeekFields
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
import java.util.*
import javax.inject.Inject

interface CalendarViewModelInputs {
    fun refresh()
    fun load(season: Int)

    fun clickTyre(season: Int)
    fun clickItem(model: CalendarModel)
}

interface CalendarViewModelOutputs {
    val items: LiveData<List<CalendarModel>?>
    val isRefreshing: LiveData<Boolean>
}

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val overviewRepository: OverviewRepository,
    private val notificationRepository: NotificationRepository,
    private val statsNavigationComponent: StatsNavigationComponent,
    private val eventsRepository: EventsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), CalendarViewModelInputs, CalendarViewModelOutputs {

    val inputs: CalendarViewModelInputs = this
    val outputs: CalendarViewModelOutputs = this

    override val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(false)

    private val calendar: MutableLiveData<Boolean> = MutableLiveData(false)

    private val season: MutableStateFlow<Int?> = MutableStateFlow(null)
    override val items: LiveData<List<CalendarModel>?> = season
        .filterNotNull()
        .flatMapLatest { season ->
            isRefreshing.postValue(true)
            fetchSeasonUseCase
                .fetch(season)
                .flatMapLatest { hasMadeRequest ->
                    combine(
                        overviewRepository.getOverview(season),
                        eventsRepository.getEvents(season),
                        calendar.asFlow()
                    ) { overview, events, isCalendar ->
                        isRefreshing.postValue(false)
                        if (!hasMadeRequest) {
                            return@combine listOf(CalendarModel.Loading)
                        }

                        val upcoming = overview.overviewRaces.getLatestUpcoming()
                        val upcomingEvents = events.filter { it.date > LocalDate.now() }

                        return@combine if (isCalendar) {
                            calendar(overview, upcomingEvents, upcoming)
                        } else {
                            schedule(overview, upcomingEvents, upcoming)
                        }
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

    override fun clickItem(model: CalendarModel) {
        when (model) {
            is CalendarModel.List -> statsNavigationComponent.weekend(WeekendInfo(
                season = model.model.season,
                round = model.model.round,
                raceName = model.model.raceName,
                circuitId = model.model.circuitId,
                circuitName = model.model.circuitName,
                country = model.model.country,
                countryISO = model.model.countryISO,
                date = model.model.date,
            ))
            is CalendarModel.Event -> {}
            is CalendarModel.Week -> {
                val race = model.race ?: return
                statsNavigationComponent.weekend(WeekendInfo(
                    season = race.season,
                    round = race.round,
                    raceName = race.raceName,
                    circuitId = race.circuitId,
                    circuitName = race.circuitName,
                    country = race.country,
                    countryISO = race.countryISO,
                    date = race.date,
                ))
            }
            CalendarModel.Loading -> {}
        }
    }

    private fun calendar(
        overview: Overview,
        upcomingEvents: List<Event>,
        upcoming: OverviewRace?
    ): List<CalendarModel> {
        val first = LocalDate.of(overview.season, Month.JANUARY, 1).startOfWeek()
        return List(60) { index -> first.plusDays((index * 7).toLong()) }
            .filter { it.year <= overview.season }
            .map { weekBeginning ->
                CalendarModel.Week(
                    season = overview.season,
                    startOfWeek = weekBeginning,
                    race = overview.overviewRaces.firstOrNull { it.date >= weekBeginning && it.date <= weekBeginning.plusDays(6L) }
                )
            }
    }

    private fun schedule(
        overview: Overview,
        upcomingEvents: List<Event>,
        upcoming: OverviewRace?
    ): List<CalendarModel> {
        return mutableListOf<CalendarModel>()
            .apply {
                addAll(overview.overviewRaces.map {
                    CalendarModel.List(
                        model = it,
                        notificationSchedule = notificationRepository.notificationSchedule,
                        showScheduleList = it == upcoming
                    )
                })
                addAll(upcomingEvents.map {
                    CalendarModel.Event(it)
                })
            }
            .sortedBy {
                when (it) {
                    is CalendarModel.Event -> it.date
                    is CalendarModel.List -> it.date
                    else -> null
                }
            }
    }
}

