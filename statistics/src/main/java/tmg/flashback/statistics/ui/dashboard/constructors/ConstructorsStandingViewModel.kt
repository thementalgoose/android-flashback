package tmg.flashback.statistics.ui.dashboard.constructors

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import tmg.flashback.formula1.model.SeasonConstructorStandingSeason
import tmg.flashback.statistics.repo.SeasonRepository
import tmg.flashback.statistics.usecases.FetchSeasonUseCase

interface ConstructorsStandingViewModelInputs {
    fun refresh()
}

interface ConstructorsStandingViewModelOutputs {
    val items: LiveData<List<SeasonConstructorStandingSeason>?>
    val isRefreshing: LiveData<Boolean>
}

class ConstructorsStandingViewModel(
    private val seasonRepository: SeasonRepository,
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), ConstructorsStandingViewModelInputs, ConstructorsStandingViewModelOutputs {

    val inputs: ConstructorsStandingViewModelInputs = this
    val outputs: ConstructorsStandingViewModelOutputs = this

    override val isRefreshing: MutableLiveData<Boolean> = MutableLiveData()

    private val season: MutableStateFlow<Int> = MutableStateFlow(2022)
    override val items: LiveData<List<SeasonConstructorStandingSeason>?> = season
        .flatMapLatest { season ->
            isRefreshing.postValue(true)
            fetchSeasonUseCase
                .fetch(season)
                .flatMapLatest { hasMadeRequest ->
                    seasonRepository.getConstructorStandings(season)
                        .map {
                            isRefreshing.postValue(false)
                            if (!hasMadeRequest && it == null) {
                                return@map null
                            }
                            return@map it?.standings?.sortedBy { it.championshipPosition }
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