package tmg.flashback.results.ui.dashboard.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.domain.repo.EventsRepository
import tmg.flashback.domain.repo.OverviewRepository
import tmg.flashback.results.usecases.FetchSeasonUseCase
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.results.contract.ResultsNavigationComponent
import tmg.flashback.weekend.contract.Weekend
import tmg.flashback.weekend.contract.model.WeekendInfo
import tmg.flashback.weekend.contract.with
import tmg.utilities.extensions.startOfWeek
import javax.inject.Inject

interface CalendarViewModelInputs {
    fun refresh()
    fun load(season: Int)

    fun clickTyre(season: Int)
    fun clickItem(model: CalendarModel)
}

interface CalendarViewModelOutputs {
    val items: LiveData<List<CalendarModel>>
    val isRefreshing: LiveData<Boolean>
}

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val overviewRepository: OverviewRepository,
    private val navigator: Navigator,
    private val resultsNavigationComponent: ResultsNavigationComponent,
    private val eventsRepository: EventsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), CalendarViewModelInputs, CalendarViewModelOutputs {

    val inputs: CalendarViewModelInputs = this
    val outputs: CalendarViewModelOutputs = this


    override val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(false)

    private val season: MutableStateFlow<Int?> = MutableStateFlow(null)
    override val items: LiveData<List<CalendarModel>> = season
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
                            return@combine listOf(CalendarModel.Loading)
                        }

                        val upcoming = overview.overviewRaces.getLatestUpcoming()
                        val upcomingEvents = events.filter { it.date > LocalDate.now() }

                        return@combine calendar(overview, upcomingEvents, upcoming)

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
        resultsNavigationComponent.tyres(season)
    }

    override fun clickItem(model: CalendarModel) {
        when (model) {
            is CalendarModel.Week -> {
                val race = model.race ?: return
                val weekend = WeekendInfo(
                    season = race.season,
                    round = race.round,
                    raceName = race.raceName,
                    circuitId = race.circuitId,
                    circuitName = race.circuitName,
                    country = race.country,
                    countryISO = race.countryISO,
                    date = race.date,
                )
                navigator.navigate(Screen.Weekend.with(weekend))
            }
            CalendarModel.Loading -> {}
        }
    }

    private fun calendar(
        overview: Overview,
        upcomingEvents: List<Event>,
        upcoming: OverviewRace?
    ): List<CalendarModel> {
        val first = LocalDate.of(overview.season, org.threeten.bp.Month.JANUARY, 1).startOfWeek()
        return List(60) { index -> first.plusDays((index * 7).toLong()) }
            .filter { it.year <= overview.season }
            .map { weekBeginning ->
                CalendarModel.Week(
                    season = overview.season,
                    startOfWeek = weekBeginning,
                    race = overview.overviewRaces.firstOrNull {
                        it.date >= weekBeginning && it.date <= weekBeginning.plusDays(
                            6L
                        )
                    }
                )
            }
    }
}