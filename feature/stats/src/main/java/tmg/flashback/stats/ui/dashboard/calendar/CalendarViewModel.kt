package tmg.flashback.stats.ui.dashboard.calendar

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.di.StatsNavigator
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.stats.usecases.FetchSeasonUseCase

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

class CalendarViewModel(
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val overviewRepository: OverviewRepository,
    private val notificationRepository: NotificationRepository,
    private val statsNavigator: StatsNavigator,
    private val statsNavigationComponent: StatsNavigationComponent,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), CalendarViewModelInputs, CalendarViewModelOutputs {

    val inputs: CalendarViewModelInputs = this
    val outputs: CalendarViewModelOutputs = this

    override val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(false)

    private val season: MutableStateFlow<Int?> = MutableStateFlow(null)
    override val items: LiveData<List<CalendarModel>?> = season
        .filterNotNull()
        .flatMapLatest { season ->
            isRefreshing.postValue(true)
            fetchSeasonUseCase
                .fetch(season)
                .flatMapLatest { hasMadeRequest ->
                    overviewRepository.getOverview(season)
                        .map { overview ->
                            isRefreshing.postValue(false)
                            if (!hasMadeRequest) {
                                return@map listOf(CalendarModel.Loading)
                            }
                            val upcoming = overview.overviewRaces.getLatestUpcoming()
                            return@map overview.overviewRaces
                                .map {
                                    CalendarModel.List(
                                        model = it,
                                        notificationSchedule = notificationRepository.notificationSchedule,
                                        showScheduleList = it == upcoming
                                    )
                                }
                                .sortedBy { it.model.round }
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
        statsNavigator.goToTyreOverview(season)
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
            is CalendarModel.Month -> TODO()
            is CalendarModel.Week -> TODO()
            CalendarModel.Loading -> {}
        }
    }
}

