package tmg.flashback.statistics.ui.dashboard.calendar

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.repo.SeasonRepository
import tmg.flashback.statistics.repository.HomeRepository
import tmg.flashback.statistics.usecases.DefaultSeasonUseCase
import tmg.flashback.statistics.usecases.FetchSeasonUseCase
import tmg.flashback.statistics.usecases.WeekendOverviewUseCase

interface CalendarViewModelInputs {
    fun refresh()
}

interface CalendarViewModelOutputs {
    val items: LiveData<List<OverviewRace>?>
    val isRefreshing: LiveData<Boolean>
}

class CalendarViewModel(
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val overviewRepository: OverviewRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), CalendarViewModelInputs, CalendarViewModelOutputs {

    val inputs: CalendarViewModelInputs = this
    val outputs: CalendarViewModelOutputs = this

    override val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(false)

    private val season: MutableStateFlow<Int> = MutableStateFlow(defaultSeasonUseCase.defaultSeason)
    override val items: LiveData<List<OverviewRace>?> = season
        .flatMapLatest { season ->
            isRefreshing.postValue(true)
            fetchSeasonUseCase
                .fetch(season)
                .flatMapLatest { hasMadeRequest ->
                    overviewRepository.getOverview(season)
                        .map {
                            isRefreshing.postValue(false)
                            if (!hasMadeRequest) {
                                return@map null
                            }
                            return@map it.overviewRaces
                        }
                }
        }
        .flowOn(ioDispatcher)
        .asLiveData(viewModelScope.coroutineContext)

    override fun refresh() {
        isRefreshing.postValue(true)
        viewModelScope.launch(ioDispatcher) {
            fetchSeasonUseCase.fetchSeason(season.value)
            isRefreshing.postValue(false)
        }
    }
}